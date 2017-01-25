package chess;

/**
 * Provides data to determine the position of an object inside the GridLayout
 * @author Mohd Arif Danial
 */
public class RowCol {
    private int r;
    private int c;
    
    /**
     * Constructor of RowCol class.
     * @param row an integer that represents the row inside the grid
     * @param col an integer that represents the column inside the grid
     */
    public RowCol(int row, int col){
        this.r = row;
        this.c = col;
    }
    
    /**
     * This will return the the row of this position inside the grid.
     * @return the row of this position 
     */
    public int getRow(){
        return this.r;
    }
    
    /**
     * This will return the column of this position inside the grid.
     * @return the column of this position
     */
    public int getCol(){
        return this.c;
    }
}
