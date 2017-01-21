/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author rfd lab
 */
public class Box extends JButton{
    private Piece piece;
    private Color color;
    
    public Box(Piece piece){
        if (piece != null){
            setPiece(piece);
        }
    }
    
    public void setPiece(Piece piece){
        this.piece = piece;
        this.setIcon(loadImage(piece.getFilename()));
    }
    
    public void deset(){
        this.piece = null;
        this.setIcon(null);
    }
    
    public boolean hasPiece(){
        return this.piece != null;
    }
    
    public boolean isThisPlayer(Player player){
        return this.piece.getOwner().getName().equals(player.getName());
    }
    
    public Piece getPiece(){
        return this.piece;
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public void setColor(Color color){
        this.setBackground(color);
        this.color = color;
    }
    
    public ImageIcon loadImage(String path){
        Image image = new ImageIcon(this.getClass().getResource(path)).getImage();
        Image scaledImage = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
