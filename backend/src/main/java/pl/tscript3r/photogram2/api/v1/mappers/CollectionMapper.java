package pl.tscript3r.photogram2.api.v1.mappers;

import pl.tscript3r.photogram2.domains.DataStructure;
import pl.tscript3r.photogram2.exceptions.mappers.MapperPhotogramException;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CollectionMapper extends Mapper {

    @SuppressWarnings("unchecked")
    default <T extends DataStructure, S extends DataStructure, F extends Collection<T>>
    F map(@NotNull Collection<S> source, @NotNull Class<T> target) {
        Stream<T> mappedStream = getMappedStream(source, target);

        if (source instanceof LinkedHashSet)
            return (F) mappedStream.collect(Collectors.toCollection(LinkedHashSet::new));
        if (source instanceof HashSet)
            return (F) mappedStream.collect(Collectors.toCollection(HashSet::new));
        if (source instanceof ArrayList)
            return (F) mappedStream.collect(Collectors.toCollection(ArrayList::new));

        throw new MapperPhotogramException(
                String.format("Given collection %s has been not implemented, consider refactor", source.getClass()));
    }

    private <T extends DataStructure, S extends DataStructure> Stream<T>
    getMappedStream(Collection<S> source, Class<T> target) {
        return source.stream()
                .map(data -> map(data, target));
    }

}
