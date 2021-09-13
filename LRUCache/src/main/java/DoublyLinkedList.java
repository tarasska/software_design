public interface DoublyLinkedList<T> {
    void addFirst(T value);
    T removeLast();
    boolean remove(T value);
    int size();
}
