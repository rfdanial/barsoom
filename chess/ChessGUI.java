/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;


/**
 *
 * @author rfd lab
 */
public class ChessGUI extends JFrame{
    
    private static final int row = 8;
    private static final int col = 5;   
    
    private int turn;
    
    private Player p1;
    private Player p2; 
    private Player currentPlayer;
    
    private Box now;
    private Box desired;
    private Box[] buttons;
    
    private JLabel topLbl = new JLabel("Turn: ");
    
    public ChessGUI(){
        super("Barsoom Chess by Team Chill");
        buttons = new Box[row * col];
        
        p1 = new Player("White", true);
        p2 = new Player("Black", false);
        
        JPanel content = new JPanel(new BorderLayout());
        
        content.add(topLbl, BorderLayout.NORTH);
        content.add(createBoxes(), BorderLayout.CENTER);
        
        newGame();
        
        this.setJMenuBar(createTopMenu());
        this.setContentPane(content);        
        this.pack();
        this.setVisible(true);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public JMenuBar createTopMenu(){
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        
        JMenuItem newGameButton = new JMenuItem("New Game", KeyEvent.VK_N);
        newGameButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newGameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                newGame();
            }
        });
        
        menu.add(newGameButton);
        
        JMenuItem howButton = new JMenuItem("How to Play", KeyEvent.VK_H);
        howButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JPanel panel = new JPanel(new GridLayout(10, 1, 2, 2));
                panel.add(new JLabel(buttons[0].loadImage("blackHeart.png")));
                panel.add(new JLabel("This is the Heart. The Heart can move only 1 step in any direction. The game ends when the heart is captured by the other side.", JLabel.CENTER));
                
                panel.add(new JLabel(buttons[0].loadImage("blackArrow.png")));
                panel.add(new JLabel("This is the Arrow. The Arrow can only move forward, 1 or 2 steps. If it reaches the end of the board, it turns around and starts heading back the other way.", JLabel.CENTER));

                panel.add(new JLabel(buttons[0].loadImage("blackCross.png")));
                panel.add(new JLabel("This is the Cross. The Cross can only move diagonally, but can go any distance.", JLabel.CENTER));

                panel.add(new JLabel(buttons[0].loadImage("blackStar.png")));
                panel.add(new JLabel("This is the Star. The Star can move 1 or 2 steps in any direction.", JLabel.CENTER));
                
                panel.add(new JLabel("The Cross will change to Star and vice versa for each 4 moves.", JLabel.CENTER));
                
                showSimpleDialog(panel, "HOW TO PLAY THIS SHIT GAME");
            }
        });
        
        menu.add(howButton);
        
        JMenuItem aboutButton = new JMenuItem("About this software", KeyEvent.VK_A);
        aboutButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String msg = 
                        "Barsoom Chess version 1.0\n"+
                        "developed by Team Chill\n\n"+
                        "Team Member:\n"+
                        "- 1142702426 Mohd Arif Danial\n"+
                        "- 1142702491 Shukri Armizi\n"+
                        "- 1142700818 Syawaludin Anas Yusof\n"+
                        "- 1142702495 Hamizan Adli\n"+
                        "- 1141126629 Ahmad Haziq Khan\n\n"+
                        "© 2017\n";
                
                showSimpleDialog(msg, "About this software");
            }
        });
        
        menu.add(aboutButton);
        
        menuBar.add(menu);
        return menuBar;
    }
    
    public JPanel createBoxes(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(row, col, 2, 2));
        
        for(int i = 0; i < row * col; i++){
            int r = getRow(i);
            int c = getCol(i);

            buttons[i] = new Box(null);
            
            buttons[i].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    int nowIndex = getIndex(r, c);
                    
                    if (now == null){
                        if (buttons[nowIndex].hasPiece() && buttons[nowIndex].isThisPlayer(currentPlayer)){
                            now = buttons[nowIndex];
                        
                            ArrayList<RowCol> legals = now.getPiece().legals(c, r);

                            for(int k = 0; k < legals.size(); k++){
                                int legalRow = legals.get(k).r();
                                int legalCol = legals.get(k).c();
                                int legalId = getIndex(legalRow, legalCol);                            

                                if (0 <= legalId && legalId < row * col){
                                    if (buttons[legalId].hasPiece()){
                                        if (buttons[legalId].isThisPlayer(currentPlayer) == false){
                                            buttons[legalId].setColor(Color.BLUE); // edible
                                        }
                                    } else {
                                        buttons[legalId].setColor(Color.GREEN); // moveable
                                    }
                                }
                            }

                            desired = null;
                        }                        
                    } else if (desired == null){
                        desired = buttons[nowIndex];                        
                        
                        if (desired.getColor() != null){
                            // edible / moveable, count as turn
                            if (desired.hasPiece()){
                                Piece piece = desired.getPiece();
                                if (piece.toString().equals("Heart")){
                                    showSimpleDialog("Winner: " + currentPlayer.getName() + "!\nPress 'OK' to restart a new Game.", "GAME OVER");
                                    newGame();
                                    return;
                                }
                            }
                            
                            desired.setPiece(now.getPiece());
                            now.deset();
                            
                            turn++;
                            
                            // swap star with cross and vice versa, for each 4 turns
                            if (turn % 4 == 0){
                                swapStarCross(p1);
                                swapStarCross(p2);
                            }
                            
                            // swap turns between players
                            if (currentPlayer.equals(p1)){
                                currentPlayer = p2;
                            } else {
                                currentPlayer = p1;
                            }
                            
                            topLbl.setText("Turn: " + currentPlayer.getName() + ", turn count: " + turn);
                        }
                        
                        now = desired = null;
                        
                        for(int k = 0; k < row * col; k++){
                            buttons[k].setColor(null);
                        }
                    }
                }
            });

            buttonPanel.add(buttons[i]);
        }
        
        return buttonPanel;
    }    
    
    public void swapStarCross(Player player){
        for(int i = 0; i < row * col; i++){
            if (buttons[i].hasPiece()){
                Piece piece = buttons[i].getPiece();
                Player owner = piece.getOwner();
                String type = piece.toString();

                if (owner.equals(player)){
                    if (type.equals("Star")){
                        buttons[i].setPiece(new Cross(player));
                    } else if (type.equals("Cross")){
                        buttons[i].setPiece(new Star(player));
                    }
                }
            }
        }
    }
    
    private void newGame(){
        
        // this will randomly pick who starts the move first
        Random rnd = new Random(System.currentTimeMillis());
        int val = rnd.nextInt(10);
        
        if (val > 4){
            currentPlayer = p2;
        } else {
            currentPlayer = p1;
        }
        
        // reset
        turn = 0;
        now = desired = null;
        
        topLbl.setText("Turn: " + currentPlayer.getName() + ", turn count: " + turn);
        
        // inform who starts first
        showSimpleDialog(currentPlayer.getName() + " starts first!", "NEW GAME");
        
        //clear all the pieces from the board
        for(int i = 0; i < row * col; i++){
            buttons[i].deset();
            buttons[i].setColor(null);
        }
        
        // initialize the position of the pieces
        buttons[getIndex(0, 0)].setPiece(new Star(p1));
        buttons[getIndex(0, 1)].setPiece(new Cross(p1));
        buttons[getIndex(0, 2)].setPiece(new Heart(p1));
        buttons[getIndex(0, 3)].setPiece(new Cross(p1));
        buttons[getIndex(0, 4)].setPiece(new Star(p1));
        
        buttons[getIndex(1, 1)].setPiece(new Arrow(p1, true));
        buttons[getIndex(1, 2)].setPiece(new Arrow(p1, true));
        buttons[getIndex(1, 3)].setPiece(new Arrow(p1, true));
        
        buttons[getIndex(6, 1)].setPiece(new Arrow(p2, false));
        buttons[getIndex(6, 2)].setPiece(new Arrow(p2, false));
        buttons[getIndex(6, 3)].setPiece(new Arrow(p2, false));
        
        buttons[getIndex(7, 0)].setPiece(new Star(p2));
        buttons[getIndex(7, 1)].setPiece(new Cross(p2));
        buttons[getIndex(7, 2)].setPiece(new Heart(p2));
        buttons[getIndex(7, 3)].setPiece(new Cross(p2));
        buttons[getIndex(7, 4)].setPiece(new Star(p2));
    }
    
    public void showSimpleDialog(Object message, String title){
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void playAudio(String audioPath){
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(audioPath).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public int getIndex(int drow, int dcol){
        return drow * this.col + dcol;
    }
    
    public int getRow(int index){
        return index / this.col;
    }
    
    public int getCol(int index){
        return index % this.col;
    }
}
