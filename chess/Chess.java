package chess;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * Manages the GUI of the BarsoomChess game.
 * 
 * @author rfd lab
 */
public class Chess extends JFrame{
    private Game game;
    
    private JPanel gridPanel = new JPanel(new GridLayout(8, 5, 2, 2));
    
    public static void main(String[] args) {
        Chess chess = new Chess();
    }
    
    /**
     * Create a new Frame for the game.
     */
    public Chess(){
        super("Barsoom Chess by Team Chill");
        game = new Game();
        
        JPanel content = new JPanel(new BorderLayout());
        content.add(game.getLabel(), BorderLayout.NORTH);
        
        for(int i = 0; i < 8 * 5; i++){
            gridPanel.add(game.getBox(i));
        }
        
        content.add(gridPanel, BorderLayout.CENTER);
        
        this.setJMenuBar(createTopMenu());
        this.setContentPane(content);        
        this.pack();
        this.setVisible(true);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    /**
     * Creates a menu that contains "New Game", "How to Play" and "About" at the top of the frame.
     * 
     * @return a JMenuBar to be attached at the top of the frame.
     */
    public JMenuBar createTopMenu(){
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        
        JMenuItem newGameButton = new JMenuItem("New Game");
        newGameButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newGameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                game.reset();
            }
        });
        
        menu.add(newGameButton);
        
        JMenuItem howButton = new JMenuItem("How to Play");
        howButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Box box = new Box(null);
                
                JPanel panel = new JPanel(new GridLayout(10, 1, 2, 2));
                panel.add(new JLabel(box.loadImage("blackHeart.png")));
                panel.add(new JLabel("This is the Heart. The Heart can move only 1 step in any direction. The game ends when the heart is captured by the other side.", JLabel.CENTER));
                
                panel.add(new JLabel(box.loadImage("blackArrow.png")));
                panel.add(new JLabel("This is the Arrow. The Arrow can only move forward, 1 or 2 steps. If it reaches the end of the board, it turns around and starts heading back the other way.", JLabel.CENTER));

                panel.add(new JLabel(box.loadImage("blackCross.png")));
                panel.add(new JLabel("This is the Cross. The Cross can only move diagonally, but can go any distance.", JLabel.CENTER));

                panel.add(new JLabel(box.loadImage("blackStar.png")));
                panel.add(new JLabel("This is the Star. The Star can move 1 or 2 steps in any direction.", JLabel.CENTER));
                
                panel.add(new JLabel("The Cross will change to Star and vice versa for each 4 moves.", JLabel.CENTER));
                
                showSimpleDialog(panel, "HOW TO PLAY?");
                box = null;
            }
        });
        
        menu.add(howButton);
        
        JMenuItem aboutButton = new JMenuItem("About");
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
    
    /**
     * Show a DialogBox
     * @param message The message to be shown
     * @param title The title of the dialogBox
     */
    public static void showSimpleDialog(Object message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Plays the audio based on the file path of the audio given
     * @param audioPath the file path inside the folder containing the source files/.
     */
    public static void playAudio(String audioPath){
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
}
