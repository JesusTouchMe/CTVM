package cum.jesus.ctvm.bytecode;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Abstract representation of the sections within the bytecode in the form of an immutable subarray
 */
public final class ByteCode implements Iterable<Byte> {
    private byte[] bytes;
    private int start;
    private int end;

    public ByteCode(byte[] bytes, int start, int end) {
        if (start < 0 || end >= bytes.length || start > end) {
            throw new IllegalArgumentException("Invalid subarray range");
        }

        this.bytes = bytes;
        this.start = start;
        this.end = end;
    }

    public ByteCode(byte[] bytes, int start) {
        this(bytes, start, bytes.length - 1);
    }

    public byte get(int index) {
        if (index < 0 || index >= end) {
            throw new IndexOutOfBoundsException("Index out of bounds. " + index + " < 0 || " + index + " >= " + end + " - " + start);
        }
        return bytes[start + index];
    }

    public int size() {
        return end - start;
    }

    @Override
    public Iterator<Byte> iterator() {
        return new ByteCodeIterator(0);
    }

    public Iterator<Byte> iterator(int offset) {
        return new ByteCodeIterator(offset);
    }

    @Override
    public void forEach(Consumer<? super Byte> action) {
        for (int i = start; i < end; i++) {
            action.accept(bytes[i]);
        }
    }

    private final class ByteCodeIterator implements Iterator<Byte> {
        private int index;

        private ByteCodeIterator(int offset) {
            index = start + offset;
        }

        @Override
        public boolean hasNext() {
            return index < end;
        }

        @Override
        public Byte next() {
            if (!hasNext()) {
                throw new IndexOutOfBoundsException("No more elements in iterator");
            }
            return bytes[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
