package chess;

import java.util.ArrayList;

/**
 * This is Star, one of the Piece inside the Barsoom Chess
 * RULE: 
 * The Star can move ONE or TWO steps towards any direction.
 * 
 * @author rfd lab
 */
public class Star extends Piece {
    
    /**
     * Constructor of Star
     * @param owner Player that owns this piece
     */
    public Star(Player owner){
        super(owner);
    }
    
     /**
     * This will return a number of RowCol objects to determine
     * other legal position(s) based on the current position inside the grid.
     * 
     * @param nowX the current column inside the grid
     * @param nowY the current row inside the grid
     * @return an ArrayList of rows and columns of legal positions based on current row and column
     */
    @Override
    public ArrayList<RowCol> legals(int nowX, int nowY){
        ArrayList<RowCol> legals = new ArrayList<RowCol>();
        
        for(int y = nowY - 2; y <= nowY + 2; y++){
            for (int x = nowX - 2; x <= nowX + 2; x++){
                if ((0 <= y) && (y < 8) && (0 <= x) && (x < 5)){
                    if (y == nowY && x == nowX){
                        // because we dont want to register "moving to original position" as a move
                        continue;
                    }
                    
                    legals.add(new RowCol(y, x));
                }
            }
        }
        
        return legals;
    }
    
    /**
     * Get the filename of the image based on the owner's isWhite
     * @return a string that represents the file name of the image
     */
    @Override
    public String getFilename(){
        if (isWhite()){
            return "whiteStar.png";
        } else {
            return "blackStar.png";
        }
    }
    
    /**
     * Get the type of piece
     * @return a string that represents the type of this piece
     */
    @Override
    public String toString() {
        return "Star";
    }
}
