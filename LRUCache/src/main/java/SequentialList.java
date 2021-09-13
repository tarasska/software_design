import java.util.NoSuchElementException;
import java.util.Objects;

public class SequentialList<T> implements DoublyLinkedList<T> {
    private static class Node<E> implements LinkedNode<E> {
        private final E value;

        private LinkedNode<E> prev;
        private LinkedNode<E> next;

        Node() {
            this.value = null;
            this.next = this;
            this.prev = this;
        }

        Node(E value, LinkedNode<E> prev, LinkedNode<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public LinkedNode<E> getNext() {
            return next;
        }

        @Override
        public LinkedNode<E> getPrev() {
            return prev;
        }

        @Override
        public void setNext(LinkedNode<E> next) {
            this.next = next;
        }

        @Override
        public void setPrev(LinkedNode<E> prev) {
            this.prev = prev;
        }

        @Override
        public E getValue() {
            return value;
        }
    }

    private int size = 0;
    private final Node<T> head = new Node<>();

    @Override
    public LinkedNode<T> addFirst(T value) {
        Node<T> newNode = new Node<T>(value, head.prev, head);

        assert head.prev != null && head.prev.getNext() != null
                : "There are no null pointers in the cyclic list";
        head.getPrev().setNext(newNode);
        head.setPrev(newNode);

        size++;
        return newNode;
    }

    @Override
    public LinkedNode<T> removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        }
        LinkedNode<T> last = head.getNext();

        assert last.getNext() != null && last.getNext().getPrev() != null
            : "There are no null pointers in the cyclic list";
        last.getNext().setPrev(head);
        head.setNext(last.getNext());

        size--;
        return last;
    }

    @Override
    public boolean remove(T value) {
        LinkedNode<T> curNode = head.getNext();

        while (curNode != head) {
            assert curNode != null : "There are no null pointers in the cyclic list";
            if (Objects.equals(value, curNode.getValue())) {
                assert curNode.getPrev() != null && curNode.getPrev().getNext() != null
                    : "There are no null pointers in the cyclic list";
                curNode.getPrev().setNext(curNode.getNext());
                curNode.getNext().setPrev(curNode.getPrev());

                size--;
                return true;
            }
            curNode = curNode.getNext();
        }
        return false;
    }

    @Override
    public void remove(LinkedNode<T> node) {
        Objects.requireNonNull(node);
        Objects.requireNonNull(node.getNext());
        Objects.requireNonNull(node.getPrev());

        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
    }

    @Override
    public int size() {
        return size;
    }
}
