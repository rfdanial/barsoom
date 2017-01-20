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
public class Cross extends Piece {
    
    //The Cross can only move diagonally, but can go any distance.
    public Cross(Player owner){
        super(owner);
    }
    
    @Override
    public ArrayList<RowCol> legals(int nowX, int nowY){
        ArrayList<RowCol> legals = new ArrayList();        
                
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
    
    @Override
    public String getFilename(){
        if (isWhite()){
            return "whiteCross.png";
        } else {
            return "blackCross.png";
        }
    }
    
    @Override
    public String toString() {
        return "Cross";
    }
}
