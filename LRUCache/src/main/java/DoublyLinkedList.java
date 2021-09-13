public interface DoublyLinkedList<T> {
    LinkedNode<T> addFirst(T value);
    LinkedNode<T> removeLast();
    boolean remove(T value);
    int size();
}
