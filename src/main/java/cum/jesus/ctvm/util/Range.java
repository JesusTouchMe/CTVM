package cum.jesus.ctvm.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * A simple range between 2 numbers (inclusive)
 *
 * @param <T> The number type of the range
 */
public final class Range<T extends Number & Comparable<T>> implements Iterable<T> {
    private final T from;
    private final T to;

    public Range(T from, T to) {
        this.from = from;
        this.to = to;
    }

    public T from() {
        return from;
    }

    public T to() {
        return to;
    }

    /**
     * Checks if a number is within the range
     *
     * @param number The number to check
     * @return True if `number` is within the range
     */
    public boolean contains(T number) {
        return number.compareTo(from) >= 0 && number.compareTo(to) <= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new RangeIterator();
    }

    private final class RangeIterator implements Iterator<T> {
        private T current = from;

        @Override
        public boolean hasNext() {
            return current.compareTo(to) <= 0;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Range out of elements");
            }

            T next = current;
            if (current instanceof Integer) {
                current = (T) Integer.valueOf(current.intValue() +  1);
            } else if (current instanceof Double) {
                current = (T) Double.valueOf(current.doubleValue() + 1);
            } else if (current instanceof Float) {
                current = (T) Float.valueOf(current.floatValue() + 1);
            } else if (current instanceof Long) {
                current = (T) Long.valueOf(current.longValue() + 1);
            } else if (current instanceof Short) {
                current = (T) Short.valueOf((short) (current.shortValue() + 1));
            } else if (current instanceof Byte) {
                current = (T) Byte.valueOf((byte) (current.byteValue() + 1));
            } else {
                throw new UnsupportedOperationException("Unsupported number type");
            }
            return next;
        }
    }
}
