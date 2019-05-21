package pl.tscript3r.photogram2.api.v1.mappers;

import pl.tscript3r.photogram2.domains.DataStructure;
import pl.tscript3r.photogram2.exceptions.MapperPhotogramException;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

@SuppressWarnings("unchecked")
abstract class AbstractMapper<E extends DataStructure, F extends DataStructure> implements Mapper {

    private final Class<E> first;
    private final Class<F> second;

    public AbstractMapper() {
        this.first = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.second = (Class<F>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Override
    public <T extends DataStructure> Boolean compatible(@NotNull final Class<T> t) {
        return t == first || t == second;
    }

    @Override
    public <T extends DataStructure, S extends DataStructure> T map(@NotNull final S source,
                                                                    @NotNull final Class<T> target) {
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

    private <T extends DataStructure> Boolean isCompatible(final DataStructure source, final Class<T> target) {
        return (compatible(source.getClass()) && compatible(target) && source.getClass() != target);
    }

    protected abstract F firstToSecond(@NotNull final E source);

    protected abstract E secondToFirst(@NotNull final F source);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMapper<?, ?> that = (AbstractMapper<?, ?>) o;
        return Objects.equals(first, that.first) &&
                Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

}
