package pl.tscript3r.photogram.api.v1.services.beans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import pl.tscript3r.photogram.infrastructure.exception.InternalErrorPhotogramException;
import pl.tscript3r.photogram.infrastructure.mapper.*;
import pl.tscript3r.photogram.user.api.v1.UserMapper;
import pl.tscript3r.photogram.user.role.RoleMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@DisplayName("Mapper service")
@ExtendWith(MockitoExtension.class)
public class MapperServiceBeanTest {

    public static MapperService getInstance() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        MapperServiceBean result = new MapperServiceBean();
        var beanMap = new HashMap<String, Mapper>();
        var roleMapper = new RoleMapper();
        beanMap.put("userMapper", new UserMapper(roleMapper));
        beanMap.put("roleMapper", new RoleMapper());
        when(applicationContext.getBeansOfType(Mapper.class)).thenReturn(beanMap);
        result.setApplicationContext(applicationContext);
        verify(applicationContext, times(1)).getBeansOfType(Mapper.class);
        return result;
    }

    @Mock
    ApplicationContext applicationContext;

    @Mock
    Mapper mapper;

    @Mock
    CollectionMapper collectionMapper;

    private MapperServiceBean mapperService;

    @BeforeEach
    void setUp() {
        mapperService = new MapperServiceBean();
        HashMap<String, Object> mappers = new HashMap<>();
        mappers.put("mapper", mapper);
        mappers.put("collectionMapper", collectionMapper);
        when(applicationContext.getBeansOfType(any())).thenReturn(mappers);
        mapperService.setApplicationContext(applicationContext);
    }

    @Test
    @DisplayName("Mapping domains -> dto")
    void mapDomainToDto() {
        when(mapper.compatible(any())).thenReturn(true);
        when(mapper.map(any(), any())).thenReturn(new Dto() {
        });
        Dto dto = mapperService.map(new DataStructure() {
        }, Dto.class);
        assertNotNull(dto);
        verify(mapper, times(2)).compatible(any());
        verify(mapper, times(1)).map(any(), any());
    }

    @Test
    @DisplayName("Mapping without mapper for source")
    void mapWithoutMapperForSource() {
        when(mapper.compatible(any())).thenReturn(false);
        Assertions.assertThrows(InternalErrorPhotogramException.class, () ->
                mapperService.map(new DataStructure() {
                }, DataStructure.class)
        );
        verify(mapper, times(1)).compatible(any());
    }

    @Test
    @DisplayName("Mapping without mapper for target")
    void mapWithoutMapperForTarget() {
        when(mapper.compatible(any())).thenReturn(true);
        when(mapper.compatible(DataStructure.class)).thenReturn(false);
        Assertions.assertThrows(InternalErrorPhotogramException.class, () ->
                mapperService.map(new Dto() {
                }, DataStructure.class)
        );
        verify(mapper, times(2)).compatible(any());
    }

    @Test
    @DisplayName("List mapping domains -> dto")
    void listMappingDomainToDto() {
        var domains = Arrays.asList(new DataStructure() {
        }, new DataStructure() {
        });
        List<Dto> dtos = Arrays.asList(new Dto() {
        }, new Dto() {
        });
        when(collectionMapper.compatible(any())).thenReturn(true);
        when(collectionMapper.map(domains, Dto.class)).thenReturn(dtos);
        var result = mapperService.map(domains, Dto.class);
        assertEquals(dtos, result);
        verify(collectionMapper, times(1)).compatible(any());
        verify(collectionMapper, times(1)).map(anyCollection(), any());
    }

    @Test
    @DisplayName("Empty list mapping")
    void listMappingWithEmptySet() {
        var emptyList = new HashSet<DataStructure>();
        var result = mapperService.map(emptyList, DataStructure.class);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("List mapping without collectionMapper")
    void listMappingWithoutExtendedMapper() {
        var domains = Arrays.asList(new DataStructure() {
        }, new DataStructure() {
        });
        when(mapper.compatible(any())).thenReturn(true);
        Assertions.assertThrows(InternalErrorPhotogramException.class, () ->
                mapperService.map(domains, DataStructure.class)
        );
    }

    @Test
    @DisplayName("Map ClassCastException handling")
    void mapClassCastExceptionHandling() {
        when(mapper.compatible(any())).thenReturn(true);
        when(mapper.map(any(), any())).thenThrow(new ClassCastException());
        Assertions.assertThrows(InternalErrorPhotogramException.class, () ->
                mapperService.map(new DataStructure() {
                }, Dto.class)
        );
    }

    @Test
    @DisplayName("Collection map ClassCastException handling")
    void collectionMapClassCastExceptionHandling() {
        when(collectionMapper.compatible(any())).thenReturn(true);
        when(collectionMapper.map(anyCollection(), any())).thenThrow(new ClassCastException());
        Assertions.assertThrows(InternalErrorPhotogramException.class, () ->
                mapperService.map(Collections.singletonList(new DataStructure() {
                }), Dto.class)
        );
    }

}