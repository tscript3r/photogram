package pl.tscript3r.photogram2.api.v1.mappers;

import org.hibernate.collection.internal.PersistentSet;
import pl.tscript3r.photogram2.domains.DataStructure;
import pl.tscript3r.photogram2.exceptions.MapperPhotogramException;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CollectionMapper extends Mapper {

    @SuppressWarnings("unchecked")
    default <T extends DataStructure, S extends DataStructure, F extends Collection<T>>
    F map(@NotNull final Collection<S> source, @NotNull final Class<T> target) {
        Stream<T> mappedStream = getMappedStream(source, target);

        if (source instanceof LinkedHashSet)
            return (F) mappedStream.collect(Collectors.toCollection(LinkedHashSet::new));
        if (source instanceof HashSet || source instanceof PersistentSet)
            return (F) mappedStream.collect(Collectors.toCollection(HashSet::new));
        if (source instanceof ArrayList || isUnmodifiableList(source))
            return (F) mappedStream.collect(Collectors.toCollection(ArrayList::new));

        throw new MapperPhotogramException(
                String.format("Given collection %s has been not implemented, consider refactor", source.getClass()));
    }

    private <T extends DataStructure, S extends DataStructure> Stream<T>
    getMappedStream(final Collection<S> source, final Class<T> target) {
        return source.stream()
                .map(data -> map(data, target));
    }

    // is private in Collections (why?) :(
    private boolean isUnmodifiableList(final Collection o) {
        return Collections.unmodifiableList(Collections.EMPTY_LIST)
                .getClass()
                .isAssignableFrom(o.getClass());
    }

}
