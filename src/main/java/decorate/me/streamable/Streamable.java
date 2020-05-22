package decorate.me.streamable;

import java.util.stream.Stream;

public interface Streamable<E> {
    Stream<E> stream();

}
