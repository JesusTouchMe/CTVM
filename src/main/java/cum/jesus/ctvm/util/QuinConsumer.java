package cum.jesus.ctvm.util;

@FunctionalInterface
public interface QuinConsumer<A, B, C, D, E> {
    void accept(A a, B b, C c, D d, E e);
}
