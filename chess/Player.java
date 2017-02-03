
/**
 * Provides data for each player that is participating in a game.
 * 
 * @author Syawaludin Anas Yusof
 */
public class Player {
    private String name;
    private boolean isWhite;
    
    /**
     * Creates a new Player
     * @param name desired name to represent this Player object
     * @param isWhite this will represent the side of the Player; true if White, false if Black
     * 
     * @author Syawaludin Anas Yusof
     */
    public Player(String name, boolean isWhite){
        this.name = name;
        this.isWhite = isWhite;
    }
    
    /**
     * Get the name of the Player
     * @return a string that represents the name of the Player
     * 
     * @author Syawaludin Anas Yusof
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Set a new name for this Player
     * @param name the desired name
     * 
     * @author Syawaludin Anas Yusof
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Get the side of the Player
     * @return a boolean that represents the side of the Player; true if White, false if Black
     * 
     * @author Syawaludin Anas Yusof
     */
    public boolean isWhite(){
        return this.isWhite;
    }
}
