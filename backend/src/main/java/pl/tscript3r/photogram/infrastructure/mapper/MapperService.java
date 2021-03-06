package pl.tscript3r.photogram.infrastructure.mapper;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface MapperService {

    <E extends DataStructure, T extends DataStructure> E map(@NotNull final T source, @NotNull final Class<E> convertTo);

    <E extends DataStructure, T extends DataStructure, F extends Collection<E>>
    F map(@NotNull final Collection<T> source, @NotNull final Class<E> convertTo);


}
