import java.util.Iterator;
import java.util.ListIterator;

class TwoWayLinkedList<E> implements MyList<E> {
    private Node<E> head, tail;
    private int size = 0;

    // Inner node class
    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> previous;

        public Node(E element) {
            this.element = element;
        }
    }

    // Add an element to the beginning of the list
    public void addFirst(E e) {
        Node<E> newNode = new Node<>(e);
        newNode.next = head;
        if (head != null) {
            head.previous = newNode;
        }
        head = newNode;
        size++;
        if (tail == null) {
            tail = head;
        }
    }

    // Add an element to the end of the list
    public void addLast(E e) {
        Node<E> newNode = new Node<>(e);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.previous = tail;
            tail = tail.next;
        }
        size++;
    }

    // Add a new element at the specified index
    public void add(int index, E e) {
        if (index == 0) {
            addFirst(e);
        } else if (index >= size) {
            addLast(e);
        } else {
            Node<E> current = head;
            for (int i = 1; i < index; i++) {
                current = current.next;
            }
            Node<E> newNode = new Node<>(e);
            newNode.next = current.next;
            newNode.previous = current;
            current.next.previous = newNode;
            current.next = newNode;
            size++;
        }
    }

    // Remove the first element and return its value
    public E removeFirst() {
        if (size == 0) return null;
        Node<E> temp = head;
        head = head.next;
        size--;
        if (head != null) {
            head.previous = null;
        } else {
            tail = null;
        }
        return temp.element;
    }

    // Remove the last element and return its value
    public E removeLast() {
        if (size == 0) return null;
        Node<E> temp = tail;
        tail = tail.previous;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return temp.element;
    }

   public E remove(int index) {
    if (index < 0 || index >= size) return null;
    if (index == 0) return removeFirst();
    if (index == size - 1) return removeLast();

    Node<E> current = head;
    for (int i = 0; i < index; i++) {
        current = current.next;
    }

    Node<E> prevNode = current.previous;
    Node<E> nextNode = current.next;
    if (prevNode != null) {
        prevNode.next = nextNode;
    }
    if (nextNode != null) {
        nextNode.previous = prevNode;
    }
    size--;
    return current.element;
}
    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        head = tail = null;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = head;
        while (current != null) {
            if (current.element.equals(o)) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) return null;
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.element;
    }

    @Override
    public int indexOf(Object o) {
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            if (current.element.equals(o)) return i;
            current = current.next;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> current = tail;
        for (int i = size - 1; i >= 0; i--) {
            if (current.element.equals(o)) return i;
            current = current.previous;
        }
        return -1;
    }

    @Override
    public E set(int index, E e) {
        if (index < 0 || index >= size) return null;
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        E oldValue = current.element;
        current.element = e;
        return oldValue;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) return false;
        remove(indexOf(o));
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    public ListIterator<E> listIterator() {
        return new LinkedListIterator();
    }

    public ListIterator<E> listIterator(int index) {
        return new LinkedListIterator(index);
    }

    private class LinkedListIterator implements ListIterator<E> {
        private Node<E> current = head;
        private int nextIndex = 0;

        public LinkedListIterator() {}

        public LinkedListIterator(int index) {
            if (index < 0 || index > size) throw new IndexOutOfBoundsException();
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            E e = current.element;
            current = current.next;
            nextIndex++;
            return e;
        }

        @Override
        public boolean hasPrevious() {
            return current.previous != null;
        }

        @Override
        public E previous() {
            if (current == null) current = tail;
            else current = current.previous;
            nextIndex--;
            return current.element;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (current.previous != null) {
                current.previous.next = current.next;
            } else {
                head = current.next;
            }
            if (current.next != null) {
                current.next.previous = current.previous;
            } else {
                tail = current.previous;
            }
            current = current.next;
            size--;
            nextIndex--;
        }

        @Override
        public void set(E e) {
            current.element = e;
        }

        @Override
        public void add(E e) {
            Node<E> newNode = new Node<>(e);
            if (head == null) {
                head = tail = newNode;
            } else {
                if (current == null) {
                    tail.next = newNode;
                    newNode.previous = tail;
                    tail = newNode;
                } else {
                    newNode.previous = current.previous;
                    newNode.next = current;
                    if (current.previous != null) {
                        current.previous.next = newNode;
                    } else {
                        head = newNode;
                    }
                    current.previous = newNode;
                }
            }
            size++;
            nextIndex++;
        }
    }
}