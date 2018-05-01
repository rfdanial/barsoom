
import java.util.ArrayList;

/**
 * This is Piece, an abstract class for Arrow, Heart, Star, and Cross to inherit
 * from. Each type of pieces has its own rules and movement.
 *
 * @author Haziq Khan
 */
public abstract class Piece {

    // the Player who owns this Piece
    private Player owner;

    /**
     * Constructor for each Piece(s)
     *
     * @param owner the Player who owns this piece.
     *
     * @author Haziq Khan
     */
    public Piece(Player owner) {
        this.owner = owner;
    }

    /**
     * This will return a number of RowCol objects to determine other legal
     * position(s) of the Piece based on the current position inside the grid.
     *
     * @param nowX the current column inside the grid
     * @param nowY the current row inside the grid
     * @return an ArrayList of rows and columns of legal positions based on
     * current row and column
     *
     * @author Haziq Khan
     */
    public ArrayList<RowCol> getLegals(int nowY, int nowX) {
        return null;
    }

    /**
     * Get the filename of the image based on the owner's isWhite
     *
     * @return a string that represents the file name of the image
     *
     * @author Haziq Khan
     */
    public String getFilename() {
        return null;
    }

    /**
     * This will get the Player that owns this piece.
     *
     * @return a Player object that owns this piece.
     *
     * @author Haziq Khan
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * This will get the owner's colour/side
     *
     * @return the colour/side (true if White, false if Black)
     *
     * @author Haziq Khan
     */
    public boolean isWhite() {
        return this.owner.isWhite();
    }
}
