import java.util.Random;

public class SingleLinkedList {
    NodeS head;

    public void add(int data){ //this procedure include shuffling
        NodeS nodeS = new NodeS(data);
        if (head==null){
            head = nodeS;
        }else {
            Random rnd = new Random();
            // explanation of random card addition to box
            // if "*" patterns correspond to nodes and "-" patterns correspond to connection
            // for the -*- list
            // a node can be connected 2 (size+1) different lines
            // for the -*-*- list
            // a node can be connected 3 (size+1) different lines

            int line = rnd.nextInt(0,size()+1); // nodes with x amount can have x+1 connections. We randomly select one of these interval.

            if(line==0){ // add to the head of the list
                nodeS.setLink(head);
                head=nodeS;
            }else if(line==size()){ // add to the tail of the list
                NodeS temp = head;
                NodeS previous = null;
                while (temp!=null){
                    previous=temp;
                    temp=temp.getLink();
                }
                temp=previous;
                temp.setLink(nodeS);
            }else{ // Add to middle of the list
                int count = 0;
                NodeS temp = head;
                NodeS previous = null;
                while (temp!=null && count<line) {
                    count++;
                    previous=temp;
                    temp = temp.getLink();
                }
                previous.setLink(nodeS);
                nodeS.setLink(temp);
            }
        }
    }

    public void prepareBox(int column_amount){
        for (int i = 0;i<column_amount;i++)
            for (int j = 1 ; j<=10;j++) //1,2,3,4,5,6,7,8,9,10
                add(j);
    }

    public int size(){
        if(head==null)
            return 0;
        else {
            NodeS temp = head;
            int count = 0;
            while (temp!=null){
                count++;
                temp=temp.getLink();
            }
            return count;
        }
    }

    public int drawCard(){
        if(head==null){
            return -1;
        }
        else {
            NodeS temp = head;
            head=head.getLink();
            return (int) temp.getData();
        }
    }

    public NodeS getHead() {
        return head;
    }

    public void setHead(NodeS head) {
        this.head = head;
    }
}
