import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JLabel;

/**
 * Provides information on current game, and logic/rule for the game.
 * 
 * @author Arif Danial, Shukri Armizi, Syawaludin Anas Yusof, Hamizan Adli, Haziq Khan
 */
public class Game{
    
    // the number of rows in the board
    private final static int ROW = 8;
    
    // the number of columns in the board
    private final static int COL = 5;
    
    // the filename for the file to save the progress of the game
    private final static String SAVEFILE = "save.txt";
    
    // the turn count
    private int turn;
    
    // this will holds information about the White player
    private Player white;
    
    // this will holds information about the Black player
    private Player black;
    
    // this will refer to who is the current player that can make a move
    private Player currentPlayer;
    
    // this will refer to the current Box that the user picks
    private Box now;
    
    // this will refer to the desired destination after user select a Box (that has a piece)
    private Box destination;
    
    // the board of the game (8 by 5)
    private Box[] board = new Box[ROW * COL];
    
    // a JLabel to update the turn count and the current player
    private JLabel label = new JLabel();
    
    /**
     * Creates a new Game.
     * 
     * @author Arif Danial
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
     * 
     * @author Hamizan Adli
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
            return ex.getMessage();
        }
    }
    
    /**
     * Load the previous progress of the game.
     * @return true if there's a previous progress, false if there's none
     * 
     * @author Syawaludin Anas Yusof
     */
    public String loadGame(){
        now = destination = null;
        
        try{
            File file = new File(SAVEFILE);
            Scanner scanner = new Scanner(file);
            
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
            return ex.getMessage();
        }
    }
    
    /**
     * Prepares the board for the game.
     * 
     * @author Shukri Armizi
     */
    private void initBoard(){
        for(int i = 0; i < ROW * COL; i++){
            board[i] = new Box(null);
            
            int nowIndex = i;
            
            board[i].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    if (now == null){
                        selectPiece(nowIndex);
                    } else if (destination == null){
                        movePiece(nowIndex);
                    }
                }
            });
        }
    }
    
    /**
     * Will select the Piece to move at given index and assign it as Now box
     * This can only be called if the Now box is null
     * @param nowIndex the given index to move from
     * 
     * @author Arif Danial
     */
    private void selectPiece(int nowIndex){
        if (board[nowIndex].hasPiece() && board[nowIndex].isThisPlayer(currentPlayer)){
            now = board[nowIndex];
            
            int row = getRow(nowIndex);
            int col = getCol(nowIndex);
            
            // get the legal positions that can be a destination of the selected Piece
            ArrayList<RowCol> legals = now.getPiece().getLegals(row, col);

            for(int k = 0; k < legals.size(); k++){
                int legalRow = legals.get(k).getRow();
                int legalCol = legals.get(k).getCol();
                int legalId = getIndex(legalRow, legalCol);                            
                
                // fill the legal destinations with colour
                if (0 <= legalId && legalId < ROW * COL){
                    if (board[legalId].hasPiece()){
                        if (board[legalId].isThisPlayer(currentPlayer) == false){
                            // if the legal destination at index (legalId) contains enemy's Piece
                            // set it to edible destination
                            board[legalId].setColor(Color.BLUE); 
                        }
                    } else {
                        //if the legal destination at index (legalId) contains no Piece
                        // set it to moveable destination
                        board[legalId].setColor(Color.GREEN); 
                    }
                }
            }

            destination = null;
        }                        
    }
    
    /**
     * This will move the Piece from the Now box to the Destination Box
     * This can only be called when the Now box is not null, and the Destination Box is null
     * @param nowIndex the desired destination index
     * 
     * @author Arif Danial
     */
    private void movePiece(int nowIndex){
        destination = board[nowIndex];                        
                        
        if (destination.getColor() != null){
            // edible / moveable, count as turn
            if (destination.hasPiece()){
                Piece piece = destination.getPiece();

                // if the Destination is a Heart, currentPlayer will win the game
                if (piece.toString().equals("Heart")){
                    Chess.showSimpleDialog("Winner: " + currentPlayer.getName() + "!\nPress 'OK' to restart a new Game.", "GAME OVER");

                    //Prepare a new game
                    reset();
                    return;
                }
            }

            // move the piece at Now to Destination
            destination.setPiece(now.getPiece());
            now.deset();

            // turn is successful
            turnComplete();
        }
        
        //reset all the color of the board
        for(int k = 0; k < ROW * COL; k++){
            board[k].setColor(null);
        }

        //nullify the Now and Destination box for the next movement
        now = destination = null;
    }
    
    /**
     * Resets the turn to 0, and randomly pick who will start the game, and arrange the pieces for the game.
     * 
     * @author Syawaludin Anas Yusof
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
     * 
     * @author Shukri Armizi
     */
    public JLabel getLabel(){
        return label;
    }
    
    /**
     * This will get the Box at the specified index to be bind to a JPanel
     * @param index the index of the desired Box
     * @return a Box object to be added to a JPanel
     * 
     * @author Shukri Armizi
     */
    public Box getBox(int index){
        return board[index];
    }
    
    /**
     * Called when completing a move.
     * 
     * @author Syawaludin Anas Yusof
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
     * 
     * @author Arif Danial
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
    
    /**
     * This is to update which player is making a move currently and the turn count.
     * 
     * @author Shukri Armizi
     */
    private void updateLabel(){
        label.setText("Turn count: " + turn + ", Current player: " + currentPlayer.getName());
    }
    
    /**
     * Swap the Star with the Cross, and the Cross with the Star based on the passed Player
     * 
     * @param player who owns the piece.
     * 
     * @author Hamizan Adli
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
     * 
     * @author Haziq Khan
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
     * 
     * @author Arif Danial
     */
    private int getIndex(int drow, int dcol){
        return drow * COL + dcol;
    }
    
    /**
     * Get the row based on given index number.
     * @param index the index of the object.
     * @return the row of the object at the index inside the grid.
     * 
     * @author Arif Danial
     */
    private int getRow(int index){
        return index / COL;
    }
    
    /**
     * Get the column based on given index number.
     * @param index the index of the object
     * @return the column of the object at the index inside the grid.
     * 
     * @Author Arif Danial
     */
    private int getCol(int index){
        return index % COL;
    }
}