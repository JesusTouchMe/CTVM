package cum.jesus.ctvm.util;

import java.util.AbstractSequentialList;
import java.util.List;
import java.util.ListIterator;

public final class CircularLinkedList<T> extends AbstractSequentialList<T> implements List<T> {
    private int size = 0;

    private Node<T> head = null;

    public void insertFront(T data) {
        linkFront(data);
    }

    public void insertBack(T data) {
        linkBack(data);
    }

    /**
     * Doesn't put it in the exact middle, but some approximation
     * @param data Data to insert
     */
    public void insertMiddle(T data) {
        Node<T> middleApprox = node(size >> 1);
        linkBefore(data, middleApprox);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new CircularIterator(index);
    }

    @Override
    public int size() {
        return 0;
    }

    private class CircularIterator implements ListIterator<T> {
        Node<T> lastReturned;
        Node<T> next;
        int index;

        CircularIterator(int index) {
            next = (index == size) ? null : node(index);
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            lastReturned = next;
            next = next.next;
            index++;
            return lastReturned.item;
        }

        @Override
        public boolean hasPrevious() {
            return next != null;
        }

        @Override
        public T previous() {
            lastReturned = next = (next == null) ? head.prev : next.prev;
            index--;
            return lastReturned.item;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            Node<T> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned) {
                next = lastNext;
            } else {
                index--;
            }
            lastReturned = null;
        }

        @Override
        public void set(T t) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            lastReturned.item = t;
        }

        @Override
        public void add(T t) {
            lastReturned = null;
            if (next == null) {
                linkBack(t);
            } else {
                linkBefore(t, next);
            }
            index++;
        }
    }

    private static class Node<T> {
        Node<T> prev;
        T item;
        Node<T> next;

        public Node(Node<T> prev, T item, Node<T> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }

        public Node(T item) {
            this.item = item;
        }
    }

    private Node<T> node(int index) {
        if (index < (size >> 1)) {
            Node<T> x = head;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            Node<T> x = head.prev;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
    }

    private void linkFront(T t) {
        Node<T> newNode = new Node<>(t);
        if (head == null) {
            head = newNode;
            head.prev = head;
            head.next = head;
        } else {
            newNode.next = head;
            newNode.prev = head.prev;
            head.prev.next = newNode;
            head.prev = newNode;
            head = newNode;
        }
        size++;
        modCount++;
    }

    private void linkBack(T t) {
        Node<T> newNode = new Node<>(t);
        if (head == null) {
            head = newNode;
            head.prev = head;
            head.next = head;
        } else {
            newNode.prev = head.prev;
            newNode.next = head;
            head.prev.next = newNode;
            head.prev = newNode;
        }
        size++;
        modCount++;
    }

    private void linkBefore(T t, Node<T> succ) {
        Node<T> pred = succ.prev;
        Node<T> newNode = new Node<>(pred, t, succ);
        succ.prev = newNode;
        pred.next = newNode;
        size++;
    }

    private T unlink(Node<T> node) {
        if (node == head && size == 1) {
            head = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            if (node == head) {
                head = node.next;
            }
        }
        size--;
        modCount++;
        return node.item;
    }
}
