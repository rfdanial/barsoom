/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author rfd lab
 */
public class Arrow extends Piece {
    
    //The Arrow Box can only move forward, 1 or 2 steps. 
    //If it reaches the end of the board, it turns around
    //and starts heading back the other way.
    
    private boolean down;
    
    public Arrow(Player owner, boolean down){
        super(owner);
        this.down = down;
    }
    
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
    
    @Override
    public String getFilename(){
        if (isWhite()){
            return "whiteArrow.png";
        } else {
            return "blackArrow.png";
        }
    }
    
    @Override
    public String toString() {
        return "Arrow";
    }
    
}
