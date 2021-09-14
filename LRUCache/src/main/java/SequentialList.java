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

    private void assertBelongsToCycle(LinkedNode<T> node) {
        assert node != null : "Node must exist";
        assert node.getNext() != null && node.getPrev() != null &&
            node.getNext().getPrev() == node && node.getPrev().getNext() == node
            : "The node must belongs to the cycle list";
    }

    @Override
    public LinkedNode<T> addFirst(T value) {
        Node<T> newNode = new Node<>(value, head.prev, head);

        assertBelongsToCycle(head);

        head.getPrev().setNext(newNode);
        head.setPrev(newNode);

        assertBelongsToCycle(newNode);

        size++;
        return newNode;
    }

    @Override
    public LinkedNode<T> removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        }
        LinkedNode<T> last = head.getNext();

        assertBelongsToCycle(last);

        last.getNext().setPrev(head);
        head.setNext(last.getNext());

        size--;
        return last;
    }

    @Override
    public boolean remove(T value) {
        LinkedNode<T> curNode = head.getNext();

        while (curNode != head) {
            assertBelongsToCycle(curNode);
            if (Objects.equals(value, curNode.getValue())) {
                curNode.getPrev().setNext(curNode.getNext());
                curNode.getNext().setPrev(curNode.getPrev());

                assertBelongsToCycle(curNode.getNext());
                assertBelongsToCycle(curNode.getPrev());

                size--;
                return true;
            }
            curNode = curNode.getNext();
        }
        return false;
    }

    @Override
    public void remove(LinkedNode<T> node) {
        assertBelongsToCycle(node);

        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());

        assertBelongsToCycle(node);
    }

    @Override
    public int size() {
        assert size >= 0 : "Size must be non-negative.";
        return size;
    }
}
