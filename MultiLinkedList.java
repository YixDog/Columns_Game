import enigma.console.TextAttributes;

import java.awt.*;

public class MultiLinkedList {
    private NodeColumn head;
    private SingleLinkedList cuttenList = new SingleLinkedList();// temporary list head (transfer operation)

    public void addColumns(String columnName){ // used for column append (column amount only)
        NodeColumn nodeColumn = new NodeColumn(columnName);
        if(head==null)
            head=nodeColumn;
        else {
            NodeColumn temp = head;
            while (temp.getDown()!=null){
                temp=temp.getDown();
            }
            temp.setDown(nodeColumn);
        }
    }

    public void distribute(String columnName, int number){ // used for first distribution only

        if(head==null)
            System.out.println("Add a column first");
        else {
            NodeColumn temp = head;
            while (temp!=null){
                if(temp.getColumnName().equals(columnName)){
                    NodeS nodeS = new NodeS(number);

                    NodeS temp2 = temp.getRight();
                    if(temp2==null)
                        temp.setRight(nodeS);
                    else {
                        while (temp2.getLink()!=null){
                            temp2=temp2.getLink();
                        }
                        temp2.setLink(nodeS);
                    }
                }
                temp=temp.getDown();
            }
        }
    }

    public void appendNum(int cursor_Col, int number){ //append num drawn from box to the end of the column
        String columnName = String.format("C%d",cursor_Col);
        if(head==null){
            System.out.println("Add a column first");
        }else {
            NodeColumn temp = head;
            while (temp!=null){
                if(temp.getColumnName().equals(columnName)){
                    NodeS nodeS = new NodeS(number);

                    NodeS temp2 = temp.getRight();
                    if(temp2==null) {
                        if((int)nodeS.getData()==10 ||(int)nodeS.getData()==1)  //transfer conditions
                            temp.setRight(nodeS);
                    }
                    else {
                        while (temp2.getLink()!=null){
                            temp2=temp2.getLink();
                        }
                        if(Math.abs((int)temp2.getData()-number)<=1)
                            temp2.setLink(nodeS);
                    }
                }
                temp=temp.getDown();
            }
        }
    }

    public void appendList(int cursor_Col){ //appending cutten temp list to the end of the selected column
        String columnName = String.format("C%d",cursor_Col);
        if(head==null)
            System.out.println("Add a column first");
        else{
            NodeColumn temp = head;
            while (temp!=null){
                if(temp.getColumnName().equals(columnName)){
                    if(temp.getRight()==null){
                        temp.setRight(cuttenList.getHead());
                        return;
                    }
                    NodeS temp2 = temp.getRight();
                    while (temp2.getLink()!=null){
                        temp2=temp2.getLink();
                    }
                    if(Math.abs( (int)temp2.getData() - (int)cuttenList.getHead().getData())<=1){
                        temp2.setLink(cuttenList.getHead());
                        cuttenList.setHead(null);
                    }
                }
                temp=temp.getDown();
            }
        }
    }

    public void cutColumn(int cursor_Col, int cursor_item){ //cut operations
        String columnName = String.format("C%d",cursor_Col);
        if(head==null){
            System.out.println("Add a column first");
        }else {
            NodeColumn temp = head;
            while (temp!=null){
                if(temp.getColumnName().equals(columnName)){
                    NodeS temp2 = temp.getRight();
                    int count = 1;
                    if(count==cursor_item){
                        cuttenList.setHead(temp.getRight());
                        temp.setRight(null);
                        return;
                    }
                    NodeS previous = null;
                    while (temp2!=null){
                        if(count==cursor_item)
                            break;
                        count++;
                        previous=temp2;
                        temp2=temp2.getLink();
                    }
                    if(temp2!=null){ //enters if count and cursor_item equals to each other
                        cuttenList.setHead(previous.getLink());
                        previous.setLink(null);
                        return;
                    }

                }
                temp=temp.getDown();
            }
        }

    }

    public int gainedScore(enigma.console.Console cn, int cursorx, int cursory){ //checks score gain cases
        int gainedScore = 0;
        if(head==null){
            {
                System.out.println("Add a column first");
            }
        }else {
            NodeColumn temp = head;
            while (temp!=null){
                if(temp.getRight()==null) {
                    temp=temp.getDown();
                    continue;
                }
                NodeS temp2 = temp.getRight();
                if( (int) temp2.getData() !=1 && (int) temp2.getData() !=10) {
                    temp=temp.getDown();
                    continue;
                }

                if(isOneToTen_set(temp2) || isTenToOne_set(temp2)){
                    temp.setRight(null);
                    gainedScore+=1000;
                    cleanColumn(cn,temp.getColumnName(), cursorx, cursory);
                }

                temp=temp.getDown();
            }
        }
        return gainedScore;
    }

    public boolean isOneToTen_set(NodeS temp2){ //checks one to ten case
        for(int i = 1 ; i<=10;i++){
            if(temp2==null)
                return false;
            if( (int)temp2.getData() != i) {
                return false;
            }
            temp2=temp2.getLink();
        }
        if(temp2!=null)
            return false;

        return true;
    }

    public boolean isTenToOne_set(NodeS temp2){ //checks ten to one case
        for(int i = 10 ; i>=1;i--){
            if(temp2==null)
                return false;
            if( (int)temp2.getData() != i) {
                return false;
            }
            temp2=temp2.getLink();
        }
        if(temp2!=null)
            return false;

        return true;
    }

    public int findLastItem(int column_Num){ //returns last item of the related column
        String columnName = String.format("C%d",column_Num);
        if(head==null){
            System.out.println("Add a column first");
        }else {
            NodeColumn temp = head;
            while (temp!=null){
                if(temp.getColumnName().equals(columnName)){

                    NodeS temp2= temp.getRight();
                    while (temp2!=null && temp2.getLink()!=null){
                        temp2=temp2.getLink();
                    }
                    if(temp2==null)
                        return -1;
                    return (int)temp2.getData();
                }
                temp=temp.getDown();
            }
            System.out.println("Column named:"+columnName+" could not found");
        }
        return -1;
    }

    public int findColumnSize(int column_Num){
        if(head==null) {
            System.out.println("There is no column");
            return -1;
        }
        else {
            int count = 1;
            int countSize = 0;
            NodeColumn temp = head;
            while (temp!=null){
                if(count == column_Num){

                    NodeS temp2 = temp.getRight();
                    while (temp2!=null){
                        countSize++;
                        temp2=temp2.getLink();
                    }
                    return countSize;

                }
                count++;
                temp=temp.getDown();
            }
            System.out.println("No column matched with the number given");
            return -1;
        }
    }

    public int sizeColumns(){
        int count = 0;
        if(head==null)
            System.out.println("There is no Columns");
        else {
            NodeColumn temp = head;
            while (temp!=null){
                count++;
                temp=temp.getDown();
            }
        }
        return count;
    }

    //text colorizations
    TextAttributes att_cursor = new TextAttributes(Color.BLACK, Color.WHITE);
    TextAttributes att_green = new TextAttributes(Color.GREEN, Color.BLACK);
    TextAttributes att_greenground = new TextAttributes(Color.BLACK, Color.green);
    TextAttributes att_white = new TextAttributes(Color.WHITE, Color.BLACK);
    TextAttributes att_fullwhite = new TextAttributes(Color.WHITE, Color.WHITE);
    public void display(enigma.console.Console cn, int selectedNum, int cursor_Col, int cursor_item,int selected_Col,int selected_item){ //display columns
        if(head==null)
            System.out.println("There is no column");
        else {
            NodeColumn temp = head;
            int coorX = 0;
            int count_Col = 1;
            while (temp!=null){
                int coorY = 2; int count_item = 1;
                NodeS temp2 = temp.getRight();

                while (temp2!=null){
                    if(cursor_Col==count_Col && cursor_item==count_item)
                        print((coorX*4), (coorY), String.format(" %2s ", temp2.getData()), cn, att_cursor);
                    else if(Math.abs(selectedNum-(int)temp2.getData())<=1 && temp2.getLink()==null && count_Col!=selected_Col)
                        print((coorX*4), (coorY), String.format(" %2s ", temp2.getData()), cn, att_green);
                    else if(count_Col==selected_Col && count_item==selected_item)
                        print((coorX*4), (coorY), String.format(" %2s ", temp2.getData()), cn, att_greenground);
                    else
                        print((coorX*4), (coorY), String.format(" %2s ", temp2.getData()), cn, att_white);

                    count_item++;
                    coorY++;
                    temp2=temp2.getLink();
                }

                count_Col++;
                coorX++;
                temp=temp.getDown();
            }
        }
    }

    public int returnElement(int column_Num, int item_Num){ //uses column name to work on asked column. Uses rank to control traversal.
        String columnName = String.format("C%d",column_Num);
        if(head==null) {
            return -1;
        }
        else {
            NodeColumn temp = head;
            while (temp!=null){
                if(temp.getColumnName().equals(columnName)){
                    NodeS temp2 = temp.getRight();
                    int count = 0;
                    while (temp2!=null){
                        if(count==item_Num){
                            return (int)temp2.getData();
                        }
                        count++;
                        temp2=temp2.getLink();
                    }
                    return -1; //returns -1 if the parameter named "item_Num" bigger than column size

                }
                temp=temp.getDown();
            }
            return -1;
        }
    }

    public void print(int x, int y, String str, enigma.console.Console cn, TextAttributes att) {// string printer
        for (int i = 0; i < str.length(); i++) {
            cn.getTextWindow().output(x + i, y, str.charAt(i), att);
        }
    } //String printer

    public void cleanColumn(enigma.console.Console cn, String columnName, int cursorx, int cursory){ //cleans only an ordered set from enigma screen
        int x;
        int columnNum;
        switch (columnName){
            case "C1":
                x=0;
                columnNum=1;
                break;
            case "C2":
                x=4;
                columnNum=2;
                break;
            case "C3":
                x=8;
                columnNum=3;
                break;
            case "C4":
                x=12;
                columnNum=4;
                break;
            case "C5":
                x=16;
                columnNum=5;
                break;
            default:
                x=-1;
                columnNum=-1;
                break;
        }
        for (int i=2;i<12;i++){
            print(x,i,"    ",cn,att_white);
        }
        if(x==cursorx-1)
            print(cursorx-1,cursory+8,"    ",cn,att_fullwhite);

    }

}
