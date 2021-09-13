public interface LinkedNode<T> {
    LinkedNode<T> getNext();
    LinkedNode<T> getPrev();
    void setNext(LinkedNode<T> next);
    void setPrev(LinkedNode<T> prev);
    T getValue();
}
