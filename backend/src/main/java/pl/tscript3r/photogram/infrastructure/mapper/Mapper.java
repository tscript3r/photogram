package pl.tscript3r.photogram.infrastructure.mapper;

import javax.validation.constraints.NotNull;

public interface Mapper {

    <T extends DataStructure> Boolean compatible(@NotNull final Class<T> t);

    <T extends DataStructure, S extends DataStructure> T map(@NotNull final S source, @NotNull final Class<T> target);

}
