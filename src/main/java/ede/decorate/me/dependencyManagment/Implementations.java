package ede.decorate.me.dependencyManagment;


import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;

/**
 * List with inheritors instances of parent Interface that are under ede.decorate.me package.
 * Exactly one instance for each implementation.
 * @param <T> - parent Interface
 */
public final class Implementations<T> implements Iterable<T> {
    private final List<T> implementations;

    /**
     * searches for all classes, that implement given interface,
     * creates an instance for each implementation
     * and stores them in {@link #implementations}.
     *
     * @param ifc - supertype
     */
    public Implementations(Class<T> ifc) {
           implementations = new Reflections(ifc.getPackageName())
                .getSubTypesOf(ifc)
                .stream()
                .map(this::newInstance)
                .collect(Collectors.toUnmodifiableList());
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return implementations.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return implementations.spliterator();
    }

    @SneakyThrows
    private T newInstance(Class<? extends T> inheritor) {
        return inheritor.getDeclaredConstructor().newInstance();
    }
}
