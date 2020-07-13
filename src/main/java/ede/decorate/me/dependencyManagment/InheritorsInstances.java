package ede.decorate.me.dependencyManagment;


import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;

/**
 * List with inheritors instances of parent Interface that are under ede.decorate.me package.
 * Exactly one instance for each implementation.
 * @param <T> - parent Interface
 */
public class InheritorsInstances<T> implements Iterable<T> {
    private final List<T> inheritorsInstances;

    /**
     * ctor searches for all classes, that implement ifc and are under ede.decorate.me package,
     * creates one instance for each inheritor and adds it to {@link #inheritorsInstances}.
     *
     * @param ifc - parent Interface
     */
    public InheritorsInstances(Class<T> ifc) {
        Reflections scanner = new Reflections("ede.decorate.me");
        Set<Class<? extends T>> inheritors = scanner.getSubTypesOf(ifc);
        inheritorsInstances = inheritors.stream().map(this::newInstance).collect(Collectors.toUnmodifiableList());

    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return inheritorsInstances.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return inheritorsInstances.spliterator();
    }

    @SneakyThrows
    private T newInstance(Class<? extends T> inheritor) {
        return inheritor.getDeclaredConstructor().newInstance();
    }
}
