package pl.tscript3r.photogram2.api.v1.services;

import pl.tscript3r.photogram2.domains.DataStructure;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface MapperService {

    <E extends DataStructure, T extends DataStructure> E map(@NotNull T source, @NotNull Class<E> convertTo);

    <E extends DataStructure, T extends DataStructure, F extends Collection<E>>
    F map(@NotNull Collection<T> source, @NotNull Class<E> convertTo);


}
