import java.util.ArrayList;

/**
 * This is Arrow, one of the Piece inside the Barsoom Chess
 * RULE: 
 * The Arrow Box can only move forward, 1 or 2 steps, 
 * If it reaches the end of the board, it turns around and 
 * starts heading back the other way.
 * 
 * @author rfd lab
 */
public class Arrow extends Piece {
    
    private boolean down;
    
    /**
     * Constructor of Arrow
     * @param owner Player that owns this piece
     * @param down the direction of this piece is heading
     */
    public Arrow(Player owner, boolean down){
        super(owner);
        this.down = down;
    }
    
    /**
     * This will return a number of RowCol objects to determine
     * other legal position(s) based on the current position of the piece inside the grid.
     * 
     * @param nowX the current column inside the grid
     * @param nowY the current row inside the grid
     * @return an ArrayList of rows and columns of legal positions based on current row and column
     */
    @Override
    public ArrayList<RowCol> legals(int nowX, int nowY){
        // list of legal rowcols
        ArrayList<RowCol> legals = new ArrayList();
        
        // this will determine which direction the arrow is going
        if (nowY == 7){
            down = false;
        } else if (nowY == 0){
            down = true;
        }
        
        // this will determine the legal rowcols
        if (down){ // going down            
            for(int i = nowY + 1; i <= nowY + 2; i++){
                if (0 <= i && i < 8)
                    legals.add(new RowCol(i, nowX));
            }
        } else { // going up
            for(int i = nowY - 1; nowY - 2 <= i; i--){
                if (0 <= i && i < 8)
                    legals.add(new RowCol(i, nowX));
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
            return "whiteArrow.png";
        } else {
            return "blackArrow.png";
        }
    }
    
    /**
     * Get the type of piece
     * @return a string that represents the type of this piece
     */
    @Override
    public String toString() {
        return "Arrow";
    }
    
}
