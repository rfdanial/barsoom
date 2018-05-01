
import java.util.ArrayList;

/**
 * This is Cross, one of the Piece inside the Barsoom Chess
 * RULE: 
 * The Cross can only move diagonally, but can go any distance.
 * 
 * @author Arif Danial
 */
public class Cross extends Piece {
    
    /**
     * Constructor of Cross
     * @param owner Player that owns this piece
     */
    public Cross(Player owner){
        super(owner);
    }
    
    /**
     * This will return a number of RowCol objects to determine
     * other legal position(s) based on the current position inside the grid.
     * 
     * @param nowX the current column inside the grid
     * @param nowY the current row inside the grid
     * @return an ArrayList of rows and columns of legal positions based on current row and column
     * 
     * @author Arif Danial
     */
    @Override
    public ArrayList<RowCol> getLegals(int nowY, int nowX){
        ArrayList<RowCol> legals = new ArrayList<RowCol>();        
                
        for(int i = 1; i < 5; i++){            
            if (0 <= nowY - i){ // top
                if (0 <= nowX - i){
                    legals.add(new RowCol(nowY - i, nowX - i));
                }
                
                if (nowX + i < 5){
                    legals.add(new RowCol(nowY - i, nowX + i));
                }
            }
            
            if (nowY + i < 8){ // bottom
                if (0 <= nowX - i){
                    legals.add(new RowCol(nowY + i, nowX - i));
                }
                
                if (nowX + i < 5){
                    legals.add(new RowCol(nowY + i, nowX + i));
                }
            }
        }
        
        return legals;
    }
    
    /**
     * Get the filename of the image based on the owner's isWhite
     * @return a string that represents the file name of the image
     * 
     * @author Arif Danial
     */
    @Override
    public String getFilename(){
        if (isWhite()){
            return "whiteCross.png";
        } else {
            return "blackCross.png";
        }
    }
    
    /**
     * Get the type of piece
     * @return a string that represents the type of this piece
     * 
     * @author Arif Danial
     */
    @Override
    public String toString() {
        return "Cross";
    }
}
