import java.util.NoSuchElementException;
import java.util.Objects;

public class SequentialList<T> implements DoublyLinkedList<T> {
    private static class Node<E> {
        private final E value;

        private Node<E> prev;
        private Node<E> next;

        Node() {
            this.value = null;
            this.next = this;
            this.prev = this;
        }

        Node(E value, Node<E> prev, Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private int size = 0;
    private final Node<T> head = new Node<>();

    @Override
    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value, head.prev, head);

        assert head.prev != null && head.prev.next != null
                : "There are no null pointers in the cyclic list";
        head.prev.next = newNode;
        head.prev = newNode;

        size++;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        }
        Node<T> last = head.next;

        assert last.next != null && last.next.prev != null
            : "There are no null pointers in the cyclic list";
        last.next.prev = head;
        head.next = last.next;

        size--;
        return last.value;
    }

    @Override
    public boolean remove(T value) {
        Node<T> curNode = head.next;

        while (curNode != head) {
            assert curNode != null : "There are no null pointers in the cyclic list";
            if (Objects.equals(value, curNode.value)) {
                assert curNode.prev != null && curNode.prev.next != null
                    : "There are no null pointers in the cyclic list";
                curNode.prev.next = curNode.next;
                curNode.next.prev = curNode.prev;

                size--;
                return true;
            }
            curNode = curNode.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }
}
