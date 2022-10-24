public class NodeD { //Node for double linked list
    NodeD previous;
    NodeD next;
    Object data;

    public NodeD(Object data) {
        this.previous = null;
        this.next = null;
        this.data = data;
    }

    public NodeD getPrevious() {
        return previous;
    }

    public void setPrevious(NodeD previous) {
        this.previous = previous;
    }

    public NodeD getNext() {
        return next;
    }

    public void setNext(NodeD next) {
        this.next = next;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
