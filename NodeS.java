public class NodeS { //Node for single linked list
    Object data;
    NodeS link;

    public NodeS(Object data) {
        this.data = data;
        this.link = null;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public NodeS getLink() {
        return link;
    }

    public void setLink(NodeS link) {
        this.link = link;
    }
}
