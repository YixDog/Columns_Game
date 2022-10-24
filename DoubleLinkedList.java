public class DoubleLinkedList {
    NodeD head;
    NodeD tail;

    public DoubleLinkedList() {
        this.head = null;
        this.tail = null;
    }

    public void addPlayer(Object data){
        NodeD nodeD = new NodeD(data);
        if (head==null && tail==null){
            head=nodeD;
            tail=nodeD;
        }else if( ((Player) nodeD.getData()).getScore() > ((Player) head.getData()).getScore()) {
            nodeD.setNext(head);
            head.setPrevious(nodeD);
            head=nodeD;
        }else if(((Player) nodeD.getData()).getScore() <= ((Player) tail.getData()).getScore()){
            nodeD.setPrevious(tail);
            tail.setNext(nodeD);
            tail=nodeD;
        } else {
            NodeD temp = head;
            NodeD previous = null;
            while (((Player) data).getScore() <= ((Player) temp.getData()).getScore()){
                previous=temp;
                temp=temp.getNext();
            }
            previous.setNext(nodeD);
            nodeD.setPrevious(previous);
            nodeD.setNext(temp);
            temp.setPrevious(nodeD);

        }
    }

    public NodeD getHead() {
        return head;
    }

    public void setHead(NodeD head) {
        this.head = head;
    }

    public NodeD getTail() {
        return tail;
    }

    public void setTail(NodeD tail) {
        this.tail = tail;
    }
}
