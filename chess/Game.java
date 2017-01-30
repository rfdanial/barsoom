
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 * Provides information on current game, and logic/rule for the game.
 * 
 * @author rfd lab
 */
public class Game{
    private final static int ROW = 8;
    private final static int COL = 5;
    private final static String SAVEFILE = "save.txt";
    
    private int turn;
    private Player white;
    private Player black;
    private Player currentPlayer;
    
    private Box now;
    private Box destination;
    
    private Box[] board = new Box[ROW * COL];
    private JLabel label = new JLabel();
    
    /**
     * Creates a new Game.
     */
    public Game(){
        turn = 0;
        white = new Player("White", true);
        black = new Player("Black", false);
        
        initBoard();
        reset();
    }
    
    /**
     * Save the current progress of the game.
     */
    public String saveGame(){
        now = destination = null;
        
        File file = new File(SAVEFILE);
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write("" + this.turn + "\n");
            writer.write(white.getName() + "\n");
            writer.write(black.getName() + "\n");
            writer.write(currentPlayer.getName() + "\n");
            
            for (int i = 0; i < ROW * COL; i++){
                if (board[i].hasPiece()){
                    Piece piece = board[i].getPiece();
                    
                    writer.write(piece.getOwner().getName() + "\n");
                    writer.write(piece.toString() + "\n");
                    writer.write("" + i + "\n");
                }
                
            }
            
            writer.flush();
            writer.close();
            return "Current progress saved!";
        } catch (IOException ex) {
            return ex.toString();
        }
    }
    
    /**
     * Load the previous progress of the game.
     * @return true if there's a previous progress, false if there's none
     */
    public String loadGame(){
        now = destination = null;
        
        try{
            File file = new File(SAVEFILE);
            Scanner scanner = new Scanner(file);
            
            // turn
            // white's name
            // black's name
            // current turn
            this.turn = scanner.nextInt();
            
            System.out.println("turn: " + turn);
            
            this.white = new Player(scanner.next(), true);
            this.black = new Player(scanner.next(), false);
            
            System.out.println(white.getName() + " vs " + black.getName() + "\n");
            
            if (scanner.next().equals(white.getName())){
                currentPlayer = white;
            } else {
                currentPlayer = black;
            }
            
            System.out.println("CurrentPlayer: " + currentPlayer.getName());
            
            for(int i = 0; i < ROW * COL; i++){
                board[i].deset();
            }
            
            while (scanner.hasNext()){
                //owner's name
                //piece type
                //index
                String ownerName = scanner.next();
                String pieceType = scanner.next();
                int index = scanner.nextInt();
                
                Player owner;
                
                if (ownerName.equals(white.getName())){
                    owner = white;
                } else {
                    owner = black;
                }
                
                
                if (pieceType.equals("Arrow")){
                    board[index].setPiece(new Arrow(owner));
                } else if (pieceType.equals("Heart")){
                    board[index].setPiece(new Heart(owner));
                } else if (pieceType.equals("Star")){
                    board[index].setPiece(new Star(owner));
                } else if (pieceType.equals("Cross")){
                    board[index].setPiece(new Cross(owner));
                }
            }
            
            updateLabel();
            
            return "Previous progress loaded!";
        } catch (Exception ex){
            ex.printStackTrace();
            return ex.toString();
        }
    }
    
    /**
     * Prepares the board for the game.
     */
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
    
    /**
     * Resets the turn to 0, and randomly pick who will start the game, and arrange the pieces for the game.
     */
    public void reset(){
        Player nextPlayer;
        
        // this will randomly pick who starts the move first
        Random rnd = new Random(System.currentTimeMillis());
        int val = rnd.nextInt(10);
        
        if (val > 4){
            currentPlayer = black;
            nextPlayer = white;
        } else {
            currentPlayer = white;
            nextPlayer = black;
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
        board[getIndex(0, 0)].setPiece(new Star(nextPlayer));
        board[getIndex(0, 1)].setPiece(new Cross(nextPlayer));
        board[getIndex(0, 2)].setPiece(new Heart(nextPlayer));
        board[getIndex(0, 3)].setPiece(new Cross(nextPlayer));
        board[getIndex(0, 4)].setPiece(new Star(nextPlayer));
        
        board[getIndex(1, 1)].setPiece(new Arrow(nextPlayer));
        board[getIndex(1, 2)].setPiece(new Arrow(nextPlayer));
        board[getIndex(1, 3)].setPiece(new Arrow(nextPlayer));
        
        board[getIndex(6, 1)].setPiece(new Arrow(currentPlayer));
        board[getIndex(6, 2)].setPiece(new Arrow(currentPlayer));
        board[getIndex(6, 3)].setPiece(new Arrow(currentPlayer));
        
        board[getIndex(7, 0)].setPiece(new Star(currentPlayer));
        board[getIndex(7, 1)].setPiece(new Cross(currentPlayer));
        board[getIndex(7, 2)].setPiece(new Heart(currentPlayer));
        board[getIndex(7, 3)].setPiece(new Cross(currentPlayer));
        board[getIndex(7, 4)].setPiece(new Star(currentPlayer));
        
        updateLabel();
    }
    
    /**
     * This will get the label that will be displaying the current turn and the turn count.
     * @return a JLabel object will display necessary information about the current game.
     */
    public JLabel getLabel(){
        return label;
    }
    
    /**
     * This will get the Box at the specified index to be bind to a JPanel
     * @param index the index of the desired Box
     * @return a Box object to be added to a JPanel
     */
    public Box getBox(int index){
        return board[index];
    }
    
    /**
     * Called when completing a move.
     */
    private void turnComplete(){
        turn++;
                            
        // swap star with cross and vice versa, for each 4 turns
        if (turn % 4 == 0){
            swapStarCross(white);
            swapStarCross(black);
        }

        // swap turns between players
        if (currentPlayer.equals(white)){
            currentPlayer = black;
        } else {
            currentPlayer = white;
        }
        
        rotateBoard();
        updateLabel();
    }
    
    /**
     * For each complete turn, this will be called to rotate the board 180 degree.
     */
    private void rotateBoard(){
        int size = ROW * COL;
        
        for(int i = 0; i < size/2 ; i++){
            Box a = board[i];
            Box b = board[size - 1 - i];
            Box temp = new Box(a.getPiece());
            
            if (a.hasPiece()){
                if (b.hasPiece()){ // a and b has Piece
                    a.setPiece(b.getPiece());
                    b.setPiece(temp.getPiece());
                } else { // only a has Piece
                    b.setPiece(a.getPiece());
                    a.deset();
                }
            } else if (b.hasPiece()){ // only b has Piece
                a.setPiece(b.getPiece());
                b.deset();
            } else { 
                // neither has Piece, so no swap take place
            }
        }
    }    
    
    private void updateLabel(){
        label.setText("Turn count: " + turn + ", Current player: " + currentPlayer.getName());
    }
    
    /**
     * Swap the Star with the Cross, and the Cross with the Star based on the passed Player
     * 
     * @param player who owns the piece.
     */
    private void swapStarCross(Player player){
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
     * Set a new name for a Player based on the Player's isWhite (side)
     * @param name the new name to set to a Player
     * @param isWhite the side of the Player to identify which Player to update
     */
    public void setPlayerName(String name, boolean isWhite){
        if (isWhite) {
            if (name != null && !name.isEmpty()){
                white.setName(name);
            } else {
                white.setName("White");
            }
        } else {
            if (name != null && !name.isEmpty()){
                black.setName(name);
            } else {
                black.setName("Black");
            }
        }
        
        updateLabel();
    }
    
    /**
     * Get the index integer based on given row and column.
     * @param drow desired row used for index calculation
     * @param dcol desired column used for index calculation
     * @return the calculated index.
     */
    private int getIndex(int drow, int dcol){
        return drow * COL + dcol;
    }
    
    /**
     * Get the row based on given index number.
     * @param index the index of the object.
     * @return the row of the object at the index inside the grid.
     */
    private int getRow(int index){
        return index / COL;
    }
    
    /**
     * Get the column based on given index number.
     * @param index the index of the object
     * @return the column of the object at the index inside the grid.
     */
    private int getCol(int index){
        return index % COL;
    }
}