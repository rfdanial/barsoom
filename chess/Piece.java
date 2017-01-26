
import java.util.ArrayList;

/**
 * This is Piece, superclass of Arrow, Heart, Star, and Cross
 * Each type of pieces has its own rules and movement.
 * @author rfd lab
 */
public class Piece {
    private Player owner;
    
    /**
     * Super Constructor of each Piece
     * @param owner the Player who owns this piece.
     */
    public Piece(Player owner){
        this.owner = owner;
    }
    
    /**
     * This will return a number of RowCol objects to determine
     * other legal position(s) of the Piece based on the current position inside the grid.
     * 
     * @param nowX the current column inside the grid
     * @param nowY the current row inside the grid
     * @return an ArrayList of rows and columns of legal positions based on current row and column
     */
    public ArrayList<RowCol> legals(int nowX, int nowY){
        return null;
    }
    
    /**
     * Get the filename of the image based on the owner's isWhite
     * @return a string that represents the file name of the image
     */
    public String getFilename(){
        return null;
    }
    
    /**
     * This will get the Player that owns this piece.
     * @return a Player object that owns this piece.
     */
    public Player getOwner(){
        return this.owner;
    }
    
    /**
     * This will get the owner's colour/side
     * @return the colour/side (true if White, false if Black)
     */
    public boolean isWhite(){
        return this.owner.isWhite();
    }
}
