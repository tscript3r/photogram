package pl.tscript3r.photogram2.api.v1.mappers;

import pl.tscript3r.photogram2.domains.DataStructure;

import javax.validation.constraints.NotNull;

public interface Mapper {

    <T extends DataStructure> Boolean compatible(@NotNull final Class<T> t);

    <T extends DataStructure, S extends DataStructure> T map(@NotNull final S source, @NotNull final Class<T> target);

}
