
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Provides data for each Box inside the grid layout
 * 
 * @author Hamizan Adli
 */
public class Box extends JButton{
    
    // this is to contains any Piece
    private Piece piece;
    
    // this is to provide the color of the current Box
    private Color color;
    
    /**
     * Creates a new Box
     * 
     * @param piece type of piece that this Box is holding, null if empty
     * 
     * @author Hamizan Adli
     */
    public Box(Piece piece){
        if (piece != null){
            setPiece(piece);
        }
    }
    
    /**
     * Assign this Box to hold a piece
     * 
     * @param piece the piece that is supposed to be inside this Box
     * 
     * @author Hamizan Adli
     */
    public void setPiece(Piece piece){
        this.piece = piece;
        this.setIcon(loadImage(piece.getFilename()));
    }
    
    /**
     * Remove any piece from this Box.
     * 
     * @author Hamizan Adli
     */
    public void deset(){
        this.piece = null;
        this.setIcon(null);
    }
    
    /**
     * Check if there's a piece inside this Box
     * 
     * @return true if this Box has a Piece inside it
     * 
     * @author Hamizan Adli
     */
    public boolean hasPiece(){
        return this.piece != null;
    }
    
    /**
     * Check if the Piece inside this Box is owned by the passed Player.
     * 
     * @param player Player to be compared.
     * @return true if the Piece inside this Box is owned by the passed Player, false if enemy of the passed Player
     * 
     * @author Hamizan Adli
     */
    public boolean isThisPlayer(Player player){
        return this.piece.getOwner().getName().equals(player.getName());
    }
    
    /**
     * Get the Piece contained by this Box
     * 
     * @return the Piece contained by this Box
     * 
     * @author Hamizan Adli
     */
    public Piece getPiece(){
        return this.piece;
    }
    
    /**
     * Get the Color of this Box's background
     * 
     * @return null if not containing any Piece, GREEN if movable, BLUE if edible
     * 
     * @author Hamizan Adli
     */
    public Color getColor(){
        return this.color;
    }
    
    /**
     * Set the Box's background color.
     * 
     * @param color the desired color
     * 
     * @author Hamizan Adli
     */
    public void setColor(Color color){
        this.setBackground(color);
        this.color = color;
    }
    
    /**
     * Get the ImageIcon based on the filePath inside the folder containing the source files.
     * 
     * @param path the absolute file path
     * @return ImageIcon to be set into a Box
     * 
     * @author Hamizan Adli
     */
    public ImageIcon loadImage(String path){
        Image image = new ImageIcon(this.getClass().getResource(path)).getImage();
        Image scaledImage = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
