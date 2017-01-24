import java.util.ArrayList;

/**
 * This is Heart, one of the Piece inside the Barsoom Chess
 * RULE: 
 * The Heart can move only ONE step towards any direction, 
 * the game ends when this piece was killed by the enemy.
 * 
 * @author rfd lab
 */
public class Heart extends Piece {
    
    /**
     * Constructor of Star
     * @param owner Player that owns this piece
     */
    public Heart(Player owner){
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
        ArrayList<RowCol> legals = new ArrayList();
        
        for(int y = nowY - 1; y <= nowY + 1; y++){
            for (int x = nowX - 1; x <= nowX + 1; x++){
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
            return "whiteHeart.png";
        } else {
            return "blackHeart.png";
        }
    }
    
    /**
     * Get the type of piece
     * @return a string that represents the type of this piece
     */
    @Override
    public String toString() {
        return "Heart";
    }
}
