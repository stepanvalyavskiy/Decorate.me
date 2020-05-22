package decorate.me;


import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;

public class InheritorsInstances<T> implements Iterable<T> {
    private final List<T> subclassToSingleton;

    public InheritorsInstances(Class<T> ifc) {
        Reflections scanner = new Reflections("decorate.me");
        Set<Class<? extends T>> inheritors = scanner.getSubTypesOf(ifc);
        subclassToSingleton = inheritors.stream().map(this::newInstance).collect(Collectors.toUnmodifiableList());

    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return subclassToSingleton.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return subclassToSingleton.spliterator();
    }

    @SneakyThrows
    private T newInstance(Class<? extends T> inheritor) {
        return inheritor.getDeclaredConstructor().newInstance();
    }
}
