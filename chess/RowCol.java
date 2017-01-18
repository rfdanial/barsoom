/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

/**
 *
 * @author rfd lab
 */
public class RowCol {
    private int r;
    private int c;
    
    public RowCol(int row, int col){
        this.r = row;
        this.c = col;
    }
    
    public int r(){
        return this.r;
    }
    
    public int c(){
        return this.c;
    }
    
    public boolean check(int row, int col){
        return this.r == row && this.c == col;
    }
    
    public boolean check(RowCol rc){
        return this.r == rc.r() && this.c == rc.c();
    }
}
