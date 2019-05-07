package pl.tscript3r.photogram2.api.v1.mappers;

import pl.tscript3r.photogram2.domains.DataStructure;
import pl.tscript3r.photogram2.exceptions.mappers.MapperPhotogramException;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;

@SuppressWarnings("unchecked")
abstract class AbstractMapper<E extends DataStructure, F extends DataStructure> implements Mapper {

    private Class<E> first;
    private Class<F> second;

    public AbstractMapper() {
        this.first = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.second = (Class<F>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Override
    public <T extends DataStructure> Boolean compatible(@NotNull Class<T> t) {
        return t == first || t == second;
    }

    @Override
    public <T extends DataStructure, S extends DataStructure> T map(@NotNull S source, @NotNull Class<T> target) {
        try {
            if (isCompatible(source, target))
                if (first.isInstance(source))
                    return (T) firstToSecond((E) source);
                else if (second.isInstance(source))
                    return (T) secondToFirst((F) source);
            throw new MapperPhotogramException("Object [" + source.getClass().getName() + "] could not be mapped to ["
                    + target.getName() + "]");
        } catch (ClassCastException e) {
            throw new MapperPhotogramException("Class cast exception by mapping from [" + source.getClass().getName()
                    + "] to [" + target.getName() + "]", e);
        }
    }

    private <T extends DataStructure> Boolean isCompatible(DataStructure source, Class<T> target) {
        return (compatible(source.getClass()) && compatible(target) && source.getClass() != target);
    }

    protected abstract F firstToSecond(E source);

    protected abstract E secondToFirst(F source);

}
