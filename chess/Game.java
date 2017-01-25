package chess;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JLabel;

public class Game{
    private final static int ROW = 8;
    private final static int COL = 5;
    private int turn;
    private Player p1;
    private Player p2;
    private Player currentPlayer;
    
    private Box now;
    private Box destination;
    
    private Box[] board = new Box[ROW * COL];
    private JLabel label = new JLabel();
    
    public Game(){
        turn = 0;
        p1 = new Player("White", true);
        p2 = new Player("Black", false);
        
        initBoard();
        reset();
    }
    
    private void initBoard(){
        for(int i = 0; i < ROW * COL; i++){
            board[i] = new Box(null);
            
            int row = getRow(i);
            int col = getCol(i);
            
            board[i].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    int nowIndex = getIndex(row, col);
                    
                    if (now == null){
                        if (board[nowIndex].hasPiece() && board[nowIndex].isThisPlayer(currentPlayer)){
                            now = board[nowIndex];
                        
                            ArrayList<RowCol> legals = now.getPiece().legals(col, row);

                            for(int k = 0; k < legals.size(); k++){
                                int legalRow = legals.get(k).getRow();
                                int legalCol = legals.get(k).getCol();
                                int legalId = getIndex(legalRow, legalCol);                            

                                if (0 <= legalId && legalId < ROW * COL){
                                    if (board[legalId].hasPiece()){
                                        if (board[legalId].isThisPlayer(currentPlayer) == false){
                                            board[legalId].setColor(Color.BLUE); // edible
                                        }
                                    } else {
                                        board[legalId].setColor(Color.GREEN); // moveable
                                    }
                                }
                            }

                            destination = null;
                        }                        
                    } else if (destination == null){
                        destination = board[nowIndex];                        
                        
                        if (destination.getColor() != null){
                            // edible / moveable, count as turn
                            if (destination.hasPiece()){
                                Piece piece = destination.getPiece();
                                if (piece.toString().equals("Heart")){
                                    //JOptionPane.showMessageDialog(null, "Winner: " + currentPlayer.getName() + "!\nPress 'OK' to restart a new Game.", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                                    Chess.showSimpleDialog("Winner: " + currentPlayer.getName() + "!\nPress 'OK' to restart a new Game.", "GAME OVER");
                                    reset();
                                    return;
                                }
                            }
                            
                            destination.setPiece(now.getPiece());
                            now.deset();
                            
                            turnComplete();
                        }
                        
                        for(int k = 0; k < ROW * COL; k++){
                            board[k].setColor(null);
                        }
                        
                        now = destination = null;
                    }
                }
            });
        }
    }
    
    public void reset(){
        // this will randomly pick who starts the move first
        Random rnd = new Random(System.currentTimeMillis());
        int val = rnd.nextInt(10);
        
        if (val > 4){
            currentPlayer = p2;
        } else {
            currentPlayer = p1;
        }
        
        // reset
        turn = 0;
        
        now = destination = null;
        
        //clear all the pieces from the board
        for(int i = 0; i < ROW * COL; i++){
            board[i].deset();
            board[i].setColor(null);
        }
        
        // initialize the position of the pieces
        board[getIndex(0, 0)].setPiece(new Star(p1));
        board[getIndex(0, 1)].setPiece(new Cross(p1));
        board[getIndex(0, 2)].setPiece(new Heart(p1));
        board[getIndex(0, 3)].setPiece(new Cross(p1));
        board[getIndex(0, 4)].setPiece(new Star(p1));
        
        board[getIndex(1, 1)].setPiece(new Arrow(p1, true));
        board[getIndex(1, 2)].setPiece(new Arrow(p1, true));
        board[getIndex(1, 3)].setPiece(new Arrow(p1, true));
        
        board[getIndex(6, 1)].setPiece(new Arrow(p2, false));
        board[getIndex(6, 2)].setPiece(new Arrow(p2, false));
        board[getIndex(6, 3)].setPiece(new Arrow(p2, false));
        
        board[getIndex(7, 0)].setPiece(new Star(p2));
        board[getIndex(7, 1)].setPiece(new Cross(p2));
        board[getIndex(7, 2)].setPiece(new Heart(p2));
        board[getIndex(7, 3)].setPiece(new Cross(p2));
        board[getIndex(7, 4)].setPiece(new Star(p2));
        
        label.setText("Turn: " + currentPlayer.getName() + ", Turn Count: " + turn);
    }
    
    public JLabel getLabel(){
        return label;
    }
    
    public Box getBox(int index){
        return board[index];
    }
    
    public String getCurrentPlayerName(){
        return currentPlayer.getName();
    }
    
    public int getTurn(){
        return this.turn;
    }
    
    /**
     * Called when completing a move.
     */
    public void turnComplete(){
        turn++;
                            
        // swap star with cross and vice versa, for each 4 turns
        if (turn % 4 == 0){
            swapStarCross(p1);
            swapStarCross(p2);
        }

        // swap turns between players
        if (currentPlayer.equals(p1)){
            currentPlayer = p2;
        } else {
            currentPlayer = p1;
        }      
        
        label.setText("Turn: " + currentPlayer.getName() + ", Turn Count: " + turn);
    }
    
    /**
     * Swap the Star with the Cross, and the Cross with the Star based on the passed Player
     * 
     * @param player who owns the piece.
     */
    public void swapStarCross(Player player){
        for(int i = 0; i < ROW * COL; i++){
            if (board[i].hasPiece()){
                Piece piece = board[i].getPiece();
                Player owner = piece.getOwner();
                String type = piece.toString();

                if (owner.equals(player)){
                    if (type.equals("Star")){
                        board[i].setPiece(new Cross(player));
                    } else if (type.equals("Cross")){
                        board[i].setPiece(new Star(player));
                    }
                }
            }
        }
    }
    
    /**
     * Get the index integer based on given row and column.
     * @param drow desired row used for index calculation
     * @param dcol desired column used for index calculation
     * @return the calculated index.
     */
    public int getIndex(int drow, int dcol){
        return drow * COL + dcol;
    }
    
    /**
     * Get the row based on given index number.
     * @param index the index of the object.
     * @return the row of the object at the index inside the grid.
     */
    public int getRow(int index){
        return index / COL;
    }
    
    /**
     * Get the column based on given index number.
     * @param index the index of the object
     * @return the column of the object at the index inside the grid.
     */
    public int getCol(int index){
        return index % COL;
    }
}