public interface DoublyLinkedList<T> {
    LinkedNode<T> addFirst(T value);
    LinkedNode<T> removeLast();
    boolean remove(T value);
    void remove(LinkedNode<T> node);
    int size();
}
