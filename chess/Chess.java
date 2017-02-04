
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Manages the GUI of the BarsoomChess game.
 * 
 * @author Shukri Armizi, Hamizan Adli
 */
public class Chess extends JFrame{
    
    //this will holds the information about the game
    private Game game;
    
    //this is to contains the button
    private JPanel gridPanel = new JPanel(new GridLayout(8, 5, 2, 2));
    
    // the main function
    public static void main(String[] args) {
        Chess chess = new Chess();
    }
    
    /**
     * Create a new Frame for the game.
     * 
     * @author Shukri Armizi
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
     * Creates a menu at the top of the frame.
     * 
     * @return a JMenuBar to be attached at the top of the frame.
     * 
     * @author Hamizan Adli, Shukri Armizi
     */
    public JMenuBar createTopMenu(){
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menu = new JMenu("Menu");
        
        JMenuItem newGameButton = new JMenuItem("New Game");
        newGameButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newGameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                game.reset();
            }
        });
        
        menu.add(newGameButton);
        
        JMenuItem setPlayerName = new JMenuItem("Edit players' name");
        setPlayerName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        setPlayerName.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JPanel pane = new JPanel(new GridLayout(2, 2, 1, 1));
                
                pane.add(new JLabel("White's name: "));
                JTextField whiteBox = new JTextField();
                pane.add(whiteBox);
                
                pane.add(new JLabel("Black's name: "));
                JTextField blackBox = new JTextField();
                pane.add(blackBox);
                
                int result = JOptionPane.showOptionDialog(null, pane, "Set Players' name", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {"Done", "Cancel"}, JOptionPane.NO_OPTION);
                if (result == JOptionPane.YES_OPTION){
                    game.setPlayerName(whiteBox.getText(), true);
                    game.setPlayerName(blackBox.getText(), false);
                }
            }
        });
        
        menu.add(setPlayerName);
        
        JMenuItem saveGameButton = new JMenuItem("Save game");
        saveGameButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveGameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                showSimpleDialog(game.saveGame(), "Attention");
            }
        });
        
        menu.add(saveGameButton);
        
        JMenuItem loadGameButton = new JMenuItem("Load last game");
        loadGameButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        loadGameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                showSimpleDialog(game.loadGame(), "Attention");
            }
        });
        
        menu.add(loadGameButton);
        
        JMenu help = new JMenu("Help");
        
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
        
        help.add(howButton);
        
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
        
        help.add(aboutButton);
        
        menuBar.add(menu);
        menuBar.add(help);
        return menuBar;
    }
    
    /**
     * Show a DialogBox
     * 
     * @param message The message to be shown
     * @param title The title of the dialogBox
     * 
     * @author Shukri Armizi
     */
    public static void showSimpleDialog(Object message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
