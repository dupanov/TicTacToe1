
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JApplet implements ActionListener {
    JButton squares[];
    JButton newGameButton;
    JLabel score;
    JLabel winCounter;
    JLabel lossCounter;
    int wins=0; int loss=0;
    int emptySquaresLeft=9;

    /*init - конструктор апплета

     */

    public void init(){
        //Встановлюємо менеджер розташування аплету
        this.setLayout(new BorderLayout());
        this.setBackground(Color.CYAN);

    //змінюємо шрифт аплету на жирний
        Font appletFont=new Font("Monospased", Font.BOLD, 20);
        this.setFont(appletFont);

        //створюємо слухач дії і кнопку нова гра
        newGameButton=new JButton("Нова гра");
        newGameButton.addActionListener(this);
        winCounter=new JLabel();
        lossCounter=new JLabel();
        Panel topPanel=new Panel();
        topPanel.setLayout(new GridLayout(1, 3));
        topPanel.add(lossCounter);
        topPanel.add(newGameButton);
        topPanel.add(winCounter);
        this.add(topPanel, "North");


        Panel centerPanel=new Panel();
        centerPanel.setLayout(new GridLayout(3, 3));
        this.add(centerPanel, "Center");
        score=new JLabel("Your turn!");

        this.add(score,"South");
        squares=new JButton[9];
        // creates buttons
        for(int i=0; i<9; i++){
            squares[i]=new JButton();
            squares[i].addActionListener(this);
            squares[i].setBackground(Color.ORANGE);
            centerPanel.add(squares[i]);
        }
    }

   
//обробник всіх подій


    public void actionPerformed(ActionEvent e) {
        JButton theButton= (JButton) e.getSource();
        String clickedButton=theButton.getText();
        if(!clickedButton.equals("") && (theButton!=newGameButton)){
      return;
    }

        //це кнопка нова гра?
        if(theButton == newGameButton){

            for(int i=0;i<9;i++){
                squares[i].setEnabled(true);
                squares[i].setText("");
                squares[i].setBackground(Color.ORANGE);
            }
            emptySquaresLeft=9;
            score.setText("Your turn");
            newGameButton.setEnabled(false);
            return;
        }
        String winner = "";
        //Is this one of cells?
        for(int i=0; i<9; i++){
            if (theButton == squares[i]){
                squares[i].setLabel("X");
                winner = lookForWinner();
                if(!"".equals(winner)){
                    endTheGame(winner);
                } else{
                    computerMove();
                    winner = lookForWinner();
                    if(!"".equals(winner)){
                        endTheGame(winner);
                    }
                }
                break;
            }
        } //end of for cycle
        if(winner.equals("X")){
            score.setText("You win!");
            wins++;
            winCounter.setText(" Вы перемагали " + wins + " разів.");
        } else if (winner.equals("0")){
            score.setText("You lose");
            loss++;
            lossCounter.setText(" Ви програвали " + loss + " разів.");
        } else if(winner.equals("T")){
            score.setText("Draw");
        }

        }//кінець методу actionPerformed



    /**
 * Цей метод викликається після кожного ходу, щоб дізнатись,
 * чи є переможець. Він перевіряє кожен ряд, колонку та
 * діагональ, щоб знайти три клітинки з однаковими написами
 * (не пустими)
 * return "X", "O", "T" – нічия, "" - ще немає переможця
 */

    String lookForWinner() {
        String theWinner = "";
        emptySquaresLeft--;

        if (emptySquaresLeft == 0) {
            return ("T");
        }
        if (!squares[0].getLabel().equals("") &&
                squares[0].getLabel().equals(squares[1].getLabel()) &&
                squares[0].getLabel().equals(squares[2].getLabel())) {
            theWinner = squares[0].getLabel();
            highlightWinner(0, 1, 2);
        } else if (!squares[3].getLabel().equals("") &&
                squares[3].getLabel().equals(squares[4].getLabel()) &&
                squares[3].getLabel().equals(squares[5].getLabel())) {
            theWinner = squares[3].getLabel();
            highlightWinner(3, 4, 5);
        } else if (!squares[6].getLabel().equals("") &&
                squares[6].getLabel().equals(squares[7].getLabel()) &&
                squares[6].getLabel().equals(squares[8].getLabel())) {
            theWinner = squares[6].getLabel();
            highlightWinner(6, 7, 8);
        } else if (!squares[0].getLabel().equals("") &&
                squares[0].getLabel().equals(squares[3].getLabel()) &&
                squares[0].getLabel().equals(squares[6].getLabel())) {
            theWinner = squares[0].getLabel();
            highlightWinner(0, 3, 6);
        } else if (!squares[1].getLabel().equals("") &&
                squares[1].getLabel().equals(squares[4].getLabel()) &&
                squares[1].getLabel().equals(squares[7].getLabel())) {
            theWinner = squares[1].getLabel();
            highlightWinner(1, 4, 7);
        } else if (!squares[2].getLabel().equals("") &&
                squares[2].getLabel().equals(squares[5].getLabel()) &&
                squares[2].getLabel().equals(squares[8].getLabel())) {
            theWinner = squares[2].getLabel();
            highlightWinner(2, 5, 8);
        } else if (!squares[0].getLabel().equals("") &&
                squares[0].getLabel().equals(squares[4].getLabel()) &&
                squares[0].getLabel().equals(squares[8].getLabel())) {
            theWinner = squares[0].getLabel();
            highlightWinner(0, 4, 8);
        } else if (!squares[2].getLabel().equals("") &&
                squares[2].getLabel().equals(squares[4].getLabel()) &&
                squares[2].getLabel().equals(squares[6].getLabel())) {
            theWinner = squares[2].getLabel();
            highlightWinner(2, 4, 6);
        }
        return theWinner;
    }
    /**
     * Цей метод застосовує набір правил, щоб знайти
     * кращий комп’ютерний хід. Якщо гарний хід
     * не знайдено, вибирається випадкова клітинка.
     */

    void computerMove(){
        int selectedSquare;
        //Спочатку комп'ютер намагається знайти порожню клітинку
        //поряд з двома клітинками з нуликами, щоб виграти
        selectedSquare=findEmptySquare("0");

        if(selectedSquare == -1){
            selectedSquare=findEmptySquare("X");
        }

        //якщо selectedSquare все ще дорівнює -1, то
        //спробує зайняти центральну клітинку
        if((selectedSquare == -1)&& (squares[4].getLabel().equals(""))){
            selectedSquare=4;
        }
        //не пощастило з центральною клітинкою ...
        //просто займаємо випадкову клітинку
        if(selectedSquare == -1){
            selectedSquare = getRandomSquare();
        }
        squares[selectedSquare].setLabel("0");


    }

    int getRandomSquare() {
        boolean gotEmptySquare = false;
        int selectedSquare;
        do{
            selectedSquare = (int) (Math.random()*9);
            if(squares[selectedSquare].getLabel().equals("")){
                gotEmptySquare = true;
            }
        } while (!gotEmptySquare);
        return selectedSquare;
    }

    /**
     * Цей метод перевіряє кожен ряд, колонку і діагональ
     * щоб дізнатися, чи є в ній дві клітинки
     * З однаковими написами і порожньою клітинкою.
     * param передається X - для користувача і O - для компа
     * return кількість вільних клітинок,
     * або -1, якщо не знайдено дві клітинки
     * з однаковими написами
     */

    int findEmptySquare(String player){
        int weight[] = new int[9];

        for(int i=0; i<9; i++){
            if(squares[i].getLabel().equals("0")){
                weight[i] = -1;
            } else if(squares[i].getLabel().equals("X")){
                weight[i] = 1;
            } else weight[i] = 0;
        }

        int twoWeights = player.equals("0") ? -2 : 2;
        //Перевіримо, чи є в ряду 1 дві однакові клітинки і одна порожня.

        if(weight[0]+weight[1]+weight[2] == twoWeights){
            if (weight[0] == 0)
                return 0;
            else if (weight[1] == 0)
                return 1;
            else
                return 2;
        }
        //Перевіримо, чи є в ряду 2 дві однакові клітинки і одна порожня.
        if(weight[3]+weight[4]+weight[5] == twoWeights){
            if (weight[3] == 0)
                return 3;
            else if (weight[4] == 0)
                return 4;
            else
                return 5;
        }
        if(weight[6]+weight[7]+weight[8] == twoWeights){
            if (weight[6] == 0)
                return 6;
            else if (weight[7] == 0)
                return 7;
            else
                return 8;
        }
        if(weight[0]+weight[3]+weight[6] == twoWeights){
            if (weight[0] == 0)
                return 0;
            else if (weight[3] == 0)
                return 3;
            else
                return 6;
        }
        if(weight[1]+weight[4]+weight[7] == twoWeights){
            if (weight[1] == 0)
                return 1;
            else if (weight[4] == 0)
                return 4;
            else
                return 7;
        }
        if(weight[2]+weight[5]+weight[8] == twoWeights){
            if (weight[2] == 0)
                return 2;
            else if (weight[5] == 0)
                return 5;
            else
                return 8;
        }
        if(weight[0]+weight[4]+weight[8] == twoWeights){
            if (weight[0] == 0)
                return 0;
            else if (weight[4] == 0)
                return 4;
            else
                return 8;
        }
        if(weight[2]+weight[4]+weight[6] == twoWeights){
            if (weight[2] == 0)
                return 2;
            else if (weight[4] == 0)
                return 4;
            else
                return 6;
        }
        return -1;

    }

    /**
     * Цей метод виділяє виграшну лінію.
     * param преша, друга і третя клітинки для виділення
     */

    void highlightWinner(int win1, int win2, int win3){
        squares[win1].setBackground(Color.CYAN);
        squares[win2].setBackground(Color.CYAN);
        squares[win3].setBackground(Color.CYAN);
    }


    void endTheGame(String winner) {
        wins=wins++;
        newGameButton.setEnabled(true);
        for(int i=0;i<9;i++){
            squares[i].setEnabled(false);
        }
    }

}
