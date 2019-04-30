package pl.tscript3r.photogram2.api.v1.mappers;

import pl.tscript3r.photogram2.domain.DataStructure;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CollectionMapper extends Mapper {

    default <T extends DataStructure, S extends DataStructure> List<T>
    map(@NotNull Collection<S> source, @NotNull Class<T> target) {
        return getMappedStream(source, target)
                .collect(Collectors.toList());
    }

    private <T extends DataStructure, S extends DataStructure> Stream<T>
    getMappedStream(Collection<S> source, Class<T> target) {
        return source.stream()
                .map(data -> map(data, target));
    }

}
