package pl.tscript3r.photogram2.api.v1.services.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import pl.tscript3r.photogram2.api.v1.mappers.CollectionMapper;
import pl.tscript3r.photogram2.api.v1.mappers.Mapper;
import pl.tscript3r.photogram2.api.v1.services.MapperService;
import pl.tscript3r.photogram2.domain.DataStructure;
import pl.tscript3r.photogram2.exceptions.services.MapperServicePhotogramException;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class MapperServiceImpl implements MapperService, ApplicationContextAware {

    private Set<Mapper> mappers = new HashSet<>();

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) {
        mappers.addAll(applicationContext.getBeansOfType(Mapper.class)
                .values());
    }

    @Override
    public <E extends DataStructure, T extends DataStructure> E map(@NotNull T source, @NotNull Class<E> convertTo) {
        try {
            Mapper mapper = getMapper(source);
            if (mapper.compatible(convertTo))
                return mapper.map(source, convertTo);
            throw new MapperServicePhotogramException("Type [" + source.getClass().getName()
                    + "] cannot be converted to [" + convertTo.getName() + "]");
        } catch (ClassCastException e) {
            throw getCustomException(e, source.getClass(), convertTo);
        }
    }

    private MapperServicePhotogramException getCustomException(ClassCastException e, Class source, Class target) {
        String message = "Class cast exception by %s of [%s] -> [%s]";
        if (isCollection(source))
            message = String.format(message, "collection mapping", source.getName(), target.getName());
        else
            message = String.format(message, "mapping", source.getName(), target.getName());
        return new MapperServicePhotogramException(message, e);
    }

    private boolean isCollection(Object o) {
        return o instanceof Collection || o instanceof Map;
    }

    private Mapper getMapper(DataStructure source) {
        for (Mapper mapper : mappers)
            if (mapper.compatible(source.getClass()))
                return mapper;
        throw new MapperServicePhotogramException("Mapper for [" +
                source.getClass().getName() + "] not found");
    }

    @Override
    public <E extends DataStructure, T extends DataStructure> List<E> map(@NotNull Collection<T> source,
                                                                          @NotNull Class<E> convertTo) {
        try {
            if (source.isEmpty())
                return new ArrayList<>();
            return getExtendedMapper(source).map(source, convertTo);
        } catch (ClassCastException e) {
            throw getCustomException(e, source.getClass(), convertTo);
        }
    }

    private <T extends DataStructure> CollectionMapper getExtendedMapper(Collection<T> source) {
        Mapper mapper = getMapper(source.iterator().next());
        if (mapper instanceof CollectionMapper)
            return (CollectionMapper) mapper;
        throw new MapperServicePhotogramException("Mapper for [" +
                source.getClass().getName() + "] is not an CollectionMapper, consider refactor");
    }

}
