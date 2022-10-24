import enigma.console.TextAttributes;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Scanner;

public class Column_game {
    SingleLinkedList box = new SingleLinkedList();
    DoubleLinkedList highScoreList = new DoubleLinkedList();
    MultiLinkedList columns = new MultiLinkedList();

    int winysize = 52; // winysize = 52
    int winxsize = 50;
    int fontsize = 15; // fontsize 15

    // ------ Standard variables for mouse and keyboard ------
    public int mousepr;          // mouse pressed?
    public int mousex, mousey;   // mouse text coords.
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    // ----------------------------------------------------

    enigma.console.Console cn = Enigma.getConsole("Columns", winxsize, winysize, fontsize);
    public TextMouseListener tmlis = new TextMouseListener() {
        public void mouseClicked(TextMouseEvent arg0) {}
        public void mousePressed(TextMouseEvent arg0) {
            if(mousepr==0) {
                mousepr=1;
                mousex=arg0.getX();
                mousey=arg0.getY();
            }
        }
        public void mouseReleased(TextMouseEvent arg0) {}
    };
    public KeyListener klis = new KeyListener() {
        public void keyTyped(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {
            if(keypr==0) {
                keypr=1;
                rkey=e.getKeyCode();
            }
        }
        public void keyReleased(KeyEvent e) {}
    };

    //display variables
    int trans_num = 0;
    float score = 0;

    //game variables
    int cursor_Col =1; int cursor_item =1;  //cursorx = cursor_Col and cursory = cursor_item
    int cursorx = 1; int cursory = -6;  //holds cursor coordinates 
    int selected_Col = -1, selected_item=-1;  //hold selected value's location
    boolean isBoxSelected = false;
    int column_amount = 5;
    int distribute_amount = 6; // necessary to first implementation
    int drawnCard = -1;
    int selectedNum=-1;
    int orderedSet = 0;
    boolean isScored = false;

    public Column_game() throws InterruptedException, FileNotFoundException {
        // ------ Standard code for mouse and keyboard ------ Do not change
        cn.getTextWindow().addTextMouseListener(tmlis);
        cn.getTextWindow().addKeyListener(klis);
        // ----------------------------------------------------
        play();}

    public void play() throws InterruptedException, FileNotFoundException {


        readHighscore();
        box.prepareBox(column_amount);
        CleanScreen();
        distributeElements(); //first distribution

        while (orderedSet<5){ //game loop
            PrintScreen(); // Printing main template
            CheckSets();
            PlayerSelection();
            cn.getTextWindow().setCursorPosition(cursorx,cursory);
        }
        float endGameScore;
        if(trans_num==0)
            endGameScore=0;
        else
            endGameScore= 100*orderedSet+(score/trans_num);

        highScoreList.addPlayer(new Player("You", "",endGameScore));
        PrintHighScoreTable(endGameScore);
        PrintRestartQues();
    }

    //Print procedures #################################################
    public void PrintScreen(){
        print(0,0,String.format(" C1  C2  C3  C4  C5       Transfer:%10d ",trans_num),att_white);
        print(0,1,String.format(" --  --  --  --  --       Score   :%10d ",(int) score),att_white);

        print(26,4,"Box",att_white);
        print(26,5,"+--+",att_white);
        print(26,6,"|  |",att_white);
        print(26,7,"+--+",att_white);

        if(drawnCard!=-1)
            print(27,6,String.format("%2d",drawnCard),att_white);
        else
            print(27,6,"  ",att_white);

        columns.display(cn,selectedNum,cursor_Col,cursor_item,selected_Col,selected_item); //displays the numbers
        cn.getTextWindow().setCursorPosition(0,0);
    }

    public void PrintHighScoreTable(float endGameScore) throws InterruptedException {
        CleanScreen();
        print(19,0,"Game is Over",att_white);
        print(16,1,String.format("%s%.2f","Total Score : ",endGameScore),att_white);

        print(17,3,"High Score Table",att_white);
        print(12,4,"Name-Surname         Score",att_white);
        print(12,5,"------------         -----",att_white);
        int lineNum = 6;
        NodeD temp = highScoreList.getHead();
        while (temp!=null){
            String name_surname = ((Player) temp.getData()).getName()+" "+((Player) temp.getData()).getSurname();
            float scoreRd = ((Player) temp.getData()).getScore(); //scoreReaded
            String line = String.format("%-22s %.2f", name_surname,scoreRd);
            print(12,lineNum,line,att_white);
            temp=temp.getNext();
            lineNum++;
        }
        print(14,lineNum+1, "(Press enter to continue)",att_white);

        while (true){

            if(keypr==1) {  // if mouse button pressed
                if(rkey==KeyEvent.VK_ENTER) {
                    keypr = 0;
                    return;
                }
            }
            keypr=0;     // last action
            Thread.sleep(1);
        }
    }

    public void PrintRestartQues() throws InterruptedException, FileNotFoundException {
        Thread.sleep(1);
        CleanScreen();
        print(20,0,"Play Again?",att_white);
        print(16,1,"(Press related keys)",att_white);
        print(24,2,"y/n",att_white);

        while (true){
            if(keypr==1) {  // if key pressed
                if(rkey==KeyEvent.VK_Y) {
                    keypr=0;
                    new Column_game();
                    return;
                }
                if(rkey==KeyEvent.VK_N) {
                    keypr=0;
                    CleanScreen();
                    print(22,0,"Bye-Bye!",att_white);
                    return;
                }
            }
            keypr=0;     // last action
            Thread.sleep(1);
        }
    }

    public void CleanFromColumn(int deletedAmount){ //after the transfer operations this procedure is necessary to clean from screen
        int x=(selected_Col-1)*4;
        int y=selected_item+1;
        //y-=8; //enigma line shift
        for(int i = 0 ; i<deletedAmount;i++){
            print(x,y+i,"    ",att_white);
        }
    }

    public void CleanScreen(){ //to clean enigma screen completely
        for(int i = 0; i<winysize;i++){
            for(int j = 0 ; j<winxsize;j++){
                cn.getTextWindow().output(j,i,' ');
            }
        }
    } //to clean enigma screen

    public void UpdateCursor(TextAttributes att){ //colorization
        int num = FindElement(cursorx,cursory);
        if(num==-1)
            print(cursorx-1,cursory+8,"    ",att); //+8 from enigma line shift
    }

    TextAttributes att_fullBlack = new TextAttributes(Color.BLACK, Color.BLACK);
    TextAttributes att_fullWhite = new TextAttributes(Color.WHITE, Color.WHITE);
    TextAttributes att_white = new TextAttributes(Color.WHITE, Color.BLACK);
    public void print(int x, int y, String str, TextAttributes att) {// string printer
        for (int i = 0; i < str.length(); i++) {
            cn.getTextWindow().output(x + i, y, str.charAt(i),att);
        }
    } //String printer

    //File operation ###################################################
    public void readHighscore() throws FileNotFoundException { //file reading
        Scanner scanner = new Scanner(new FileReader("highscore.txt"));

        while (scanner.hasNextLine()){
            String name =scanner.next();
            String surname = scanner.next();
            float score = Float.parseFloat(scanner.next());
            highScoreList.addPlayer(new Player(name, surname,score));
            if(!scanner.hasNextLine())
                return;
            scanner.nextLine();
        }
    } //file reading

    //game procedures ###################################################
    public void distributeElements(){ // first distribution
        for(int i = 1 ; i<=column_amount;i++)
        {
            columns.addColumns(String.format("C%d",i));
            for(int j = 1;j<=distribute_amount;j++)
                columns.distribute(String.format("C%d",i),box.drawCard());
        }
    }

    public void CheckSets(){ //checks scored cases
        int gainedScore = columns.gainedScore(cn,cursorx,cursory);  //returns gained score if player scored, cleans an ordered set from enigma screen
        int deletedSetAmount = gainedScore/1000;
        orderedSet+= deletedSetAmount;
        score+=gainedScore;
        isScored=true;
    }

    public void PlayerSelection() throws InterruptedException {

        if(keypr==1){
            if(rkey==KeyEvent.VK_LEFT && cursor_Col-1 >=1){
                cursor_Col--;
                UpdateCursor(att_fullBlack);
                cursorx-=4;
                UpdateCursor(att_fullWhite);

                isScored=false;

                keypr=0;
                return;
            }

            if(rkey==KeyEvent.VK_RIGHT && cursor_Col+1 <=columns.sizeColumns()){
                cursor_Col++;
                UpdateCursor(att_fullBlack);// colorization
                cursorx+=4;
                UpdateCursor(att_fullWhite);
                keypr=0;
                return;
            }

            if(rkey==KeyEvent.VK_UP && cursor_item-1 >=1){
                cursor_item--;
                UpdateCursor(att_fullBlack);
                cursory--;
                UpdateCursor(att_fullWhite);
                keypr=0;
                return;
            }

            if(rkey==KeyEvent.VK_DOWN && cursor_item<50){
                //enigma screen is limited according to the max item number a column can have.
                // The box has only 50 card so that cursor_item value can be 50 max.
                cursor_item++;
                UpdateCursor(att_fullBlack);
                cursory++;
                UpdateCursor(att_fullWhite);
                keypr=0;
                return;
            }

            if(rkey==KeyEvent.VK_Z && 1<=cursor_Col && cursor_Col<=5 && 2<=cursory+8 && cursory+8 <= columns.findColumnSize(cursor_Col)+1){
                int num = FindElement(cursorx,cursory);
                if(num != -1) {
                    selectedNum = num;
                    isBoxSelected=false;
                    selected_Col=cursor_Col;
                    selected_item=cursor_item;
                    
                    keypr=0;
                    return;
                }
                keypr=0;     // last action
                Thread.sleep(1);
            }

            if(rkey==KeyEvent.VK_B){
                if(drawnCard==-1){
                    int num =box.drawCard();
                    if(num!=-1){
                        drawnCard = num;
                        keypr =0;
                        return;
                    }
                    else
                        print(26,9,"No Cards Left!",att_white);
                }else {
                    selectedNum=drawnCard;
                    isBoxSelected=true;
                    selected_Col=-1;
                    selected_item=-1;

                    keypr=0;
                    return;
                }
                keypr=0;     // last action
                Thread.sleep(1);
            }

            if(rkey==KeyEvent.VK_X){
                if(isBoxSelected){
                    int firstSize = columns.findColumnSize(cursor_Col);
                    columns.appendNum(cursor_Col,selectedNum);
                    int secondSize = columns.findColumnSize(cursor_Col);

                    if(secondSize>firstSize){  //enters if the number was appended
                        isBoxSelected=false;
                        drawnCard=-1;
                        selectedNum=-1;
                        selected_Col=-1;
                        selected_item=-1;
                    }

                    keypr=0;
                    return;
                }else if(selected_Col!=cursor_Col){  //enters if the selection satisfied by "from" columns
                    int lastNum = columns.findLastItem(cursor_Col);
                    if(selected_item != -1 && selected_Col!=-1 && lastNum==-1){
                        if(selectedNum==1 || selectedNum==10){
                            int firstSize = columns.findColumnSize(selected_Col);
                            columns.cutColumn(selected_Col, selected_item);
                            int finalSize = columns.findColumnSize(selected_Col);
                            columns.appendList(cursor_Col);
                            CleanFromColumn(firstSize-finalSize);
                            selected_Col=-1;
                            selected_item=-1;
                            selectedNum=-1;
                            trans_num++;
                        }
                    }
                    else if(selected_item != -1 && selected_Col!=-1 && Math.abs(selectedNum-lastNum)<=1) {
                        int firstSize = columns.findColumnSize(selected_Col);
                        columns.cutColumn(selected_Col, selected_item);
                        int finalSize = columns.findColumnSize(selected_Col);
                        columns.appendList(cursor_Col);
                        CleanFromColumn(firstSize-finalSize);
                        selected_Col=-1;
                        selected_item=-1;
                        selectedNum=-1;
                        trans_num++;
                    }
                    keypr=0;
                    return;
                }
            }

            if(rkey==KeyEvent.VK_E){
                orderedSet+=5; // game loop is checked according to the orderedSet number
                keypr=0;
                return;
            }
        }
        keypr=0;     // last action
        Thread.sleep(1);
    }

    // Other methods ###################################################
    public int FindElement(int x, int y){ //send cursorx and cursory

        int column;
        int line;
        y+=8; // enigma line shift
        if(x==1 || x==2)
            column = 1;
        else if(x==5 || x==6)
            column = 2;
        else if(x==9 || x==10)
            column = 3;
        else if(x==13 || x==14)
            column = 4;
        else if(x==17 || x==18)
            column = 5;
        else
            column = -1;

        line = y-2;

        return columns.returnElement(column,line);
    }
}
