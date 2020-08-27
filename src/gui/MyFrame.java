import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/**
 * class handles the display of the game on a JPanel in a JFrame
 * utilizes JComponents, timers, key listeners, and button listeners
 *
 */
public class MyFrame extends JFrame {
    private javax.swing.Timer timer;
    private JPanel jp, board, side, hold;
    private ButtonListener butt;
    private JLabel[][] grid, sideGrid, holdGrid; // the blocks will be displayed on the screen using a 2d grid of JLabels with colored backgrounds
    private JLabel scoreLabel, score, levelLabel, level, lineLabel1, lineLabel2, line, gameOver, holdLabel, futureLabel;
    private JButton play, restart;
    private Board b;
    private int gameTick;
    private boolean[] keys;
    private boolean timerRunning;
    
    // constructor initializes all instance variables (mostly jcomponents)
    public MyFrame() {
        setTitle("Tetris");
        jp = new JPanel();
        jp.setBackground(Color.LIGHT_GRAY);
        jp.setPreferredSize(new Dimension(840,700));
        jp.setLayout(null);
       
        jp.addKeyListener( new Listen() );
        jp.setFocusable(true);
        jp.requestFocus();
        
        gameTick = 800;
        timer = new javax.swing.Timer(gameTick, new TimerListener());
        timerRunning = false;
        
        b = new Board(this);
        keys = new boolean[7];
        
        board = new JPanel();
        board.setBackground(Color.BLACK);
        board.setLayout(null);
        board.setBounds(250,0,(int)(350),(int)(700));
       
        butt = new ButtonListener();
        play = new JButton("Play");
        play.setBounds(105, 250, 140, 50);
        play.setFont(new Font("Serif", Font.BOLD, 30));
        board.add(play);
        play.addActionListener(butt);
       
        restart = new JButton("Restart");
        restart.setBounds(105, 350, 140, 50);
        restart.setFont(new Font("Serif", Font.BOLD, 30));
        board.add(restart);
        restart.addActionListener(butt);
        restart.hide();
       
        scoreLabel = new JLabel("Score:",SwingConstants.CENTER);
        scoreLabel.setBounds(50, 400, 140, 50);
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 30));
        jp.add(scoreLabel);
       
        score = new JLabel("0",SwingConstants.CENTER);
        score.setBounds(50, 425, 140, 50);
        score.setFont(new Font("Serif", Font.BOLD, 30));
        jp.add(score);
       
        levelLabel = new JLabel("Level:",SwingConstants.CENTER);
        levelLabel.setBounds(50, 475, 140, 50);
        levelLabel.setFont(new Font("Serif", Font.BOLD, 30));
        jp.add(levelLabel);
       
        level = new JLabel("0",SwingConstants.CENTER);
        level.setBounds(50, 500, 140, 50);
        level.setFont(new Font("Serif", Font.BOLD, 30));
        jp.add(level);
       
        lineLabel1 = new JLabel("Lines",SwingConstants.CENTER);
        lineLabel1.setBounds(50, 550, 140, 50);
        lineLabel1.setFont(new Font("Serif", Font.BOLD, 30));
        jp.add(lineLabel1);
       
        lineLabel2 = new JLabel("Cleared:",SwingConstants.CENTER);
        lineLabel2.setBounds(50, 575, 140, 50);
        lineLabel2.setFont(new Font("Serif", Font.BOLD, 30));
        jp.add(lineLabel2);
        
        line = new JLabel("0",SwingConstants.CENTER);
        line.setBounds(50, 600, 140, 50);
        line.setFont(new Font("Serif", Font.BOLD, 30));
        jp.add(line);
        
        holdLabel = new JLabel("Hold",SwingConstants.CENTER);
        holdLabel.setBounds(50, 0, 140, 50);
        holdLabel.setFont(new Font("Serif", Font.BOLD, 30));
        jp.add(holdLabel);
       
        futureLabel = new JLabel("Next",SwingConstants.CENTER);
        futureLabel.setBounds(650, 0, 140, 50);
        futureLabel.setFont(new Font("Serif", Font.BOLD, 30));
        jp.add(futureLabel);
        
        gameOver = new JLabel("Game Over",SwingConstants.CENTER);
        gameOver.setBounds(0, 100, 350, 50);
        gameOver.setFont(new Font("Serif", Font.BOLD, 50));
        gameOver.setForeground(Color.RED);
        board.add(gameOver);
        gameOver.hide();
        
        side = new JPanel();
        side.setBackground(Color.BLACK);
        side.setLayout(null);
        side.setBounds(650,50,140,420);
       
        hold = new JPanel();
        hold.setBackground(Color.BLACK);
        hold.setLayout(null);
        hold.setBounds(50,50,140,100);
       
        init();
        getContentPane().add(board);
        getContentPane().add(side);
        getContentPane().add(hold);
        getContentPane().add(jp);
        pack();
    }

    // helper method to the constructor that initializes the JLabel grids
    private void init() {
        grid = new JLabel[20][10];
        int s = board.getWidth()/10;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                grid[r][c] = new JLabel();
                grid[r][c].setBounds(s*c,s*r,s,s);
                grid[r][c].setOpaque(true);
                grid[r][c].setBackground(Color.BLACK);
                board.add(grid[r][c]);
            }
        }
       
        sideGrid = new JLabel[12][4];
        s = side.getWidth()/4;
        for (int r = 0; r < sideGrid.length; r++) {
            for (int c = 0; c < sideGrid[0].length; c++) {
                sideGrid[r][c] = new JLabel();
                sideGrid[r][c].setBounds(s*c,s*r,s,s);
                sideGrid[r][c].setOpaque(true);
                sideGrid[r][c].setBackground(Color.BLACK);
                side.add(sideGrid[r][c]);
            }
        }
       
        holdGrid = new JLabel[3][4];        
        s = hold.getWidth()/4;
        for (int r = 0; r < holdGrid.length; r++) {
            for (int c = 0; c < holdGrid[0].length; c++) {
                holdGrid[r][c] = new JLabel();
                holdGrid[r][c].setBounds(s*c,s*r,s,s);
                holdGrid[r][c].setOpaque(true);
                holdGrid[r][c].setBackground(Color.BLACK);
                hold.add(holdGrid[r][c]);
            }
        }
       
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }
    
    // updaters that the Board class utilizes
    
    public void levelUp() {
        timer.setDelay((int)(timer.getDelay()*0.9));
    }
    public void endGame() {
        timer.stop();
        gameOver.show();
        timerRunning = false;
        play.setText("Restart");
        play.show();
        play.repaint();
        gameTick = 800;
        timer.setDelay(gameTick);
    }
    
    // display methods
    
    public void displayText(int newScore, int linesCleared) {
        level.setText("" + b.getLevel());
        line.setText("" + linesCleared);
        score.setText("" + newScore);
    }
    public void displayMainGrid(Color[][] newGrid) {
        for (int r = 0; r < newGrid.length; r++) {
            for (int c = 0; c < newGrid[0].length; c++) {
                grid[r][c].setBackground(newGrid[r][c]);
            }
        }
    }
    public void displayHoldGrid(Color[][] newGrid) {
        for (int r = 0; r < newGrid.length; r++) {
            for (int c = 0; c < newGrid[0].length; c++) {
                holdGrid[r][c].setBackground(newGrid[r][c]);
            }
        }
    }
    public void displaySideGrid(Color[][] newGrid) {
        for (int r = 0; r < newGrid.length; r++) {
            for (int c = 0; c < newGrid[0].length; c++) {
                sideGrid[r][c].setBackground(newGrid[r][c]);
            }
        }
    }
    public void displayGame() {
        displaySideGrid(b.getSideGrid());
        displayMainGrid(b.getMainGrid());
        displayHoldGrid(b.getHoldGrid());
        
        score.setText("" + b.getScore());
        line.setText("" + b.getLinesCleared());
    }
    public void display() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
                jp.requestFocus();
            }
        });
    }
    
    // action listeners
    
    private class TimerListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            displayGame();
            b.update();
        }
    }
    private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == play) {
                timerRunning = true;
                timer.start();
                play.hide();
                restart.hide();
                gameOver.setForeground(Color.RED);
                gameOver.setText("Game Over");
                gameOver.hide();
            }
            if (e.getSource() == restart) {
                b.endGame();
                play.hide();
                restart.hide();
                timer.start();
                timerRunning = true;
                level.setText("" + b.getLevel());
                line.setText("" + b.getLinesCleared());
                score.setText("" + b.getScore());
                
                gameOver.setForeground(Color.RED);
                gameOver.setText("Game Over");
                gameOver.hide();
            }
        }
    }
    /* game controls:
     * left/right/down arrow keys for block movement
     * up arrow for clockwise rotation
     * spacebar to drop block instantly
     * c to hold block
     * esc to pause game
     */
    private class Listen extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){ //0up,1down.2left,3right,4space,5hold, 6pause
            if (e.getKeyCode() == KeyEvent.VK_LEFT) keys[2] = true;
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) keys[3] = true;
            else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (timerRunning) b.rotate();
                keys[0] = true;
            }  
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) keys[1] = true; 
            else if (e.getKeyCode() == KeyEvent.VK_SPACE){
                if (keys[4] == false) {
                    if (timerRunning) b.slam();
                    keys[4] = true;
                }
            } else if (e.getKeyCode() == 67) { 
                if (keys[5] == false) {
                    if (b.isHolding() == false && timerRunning) b.hold();
                    keys[5] = true;
                }
            } else if (e.getKeyCode() == 27) {
                if (keys[6] == false) {
                    if (timerRunning) {
                        timer.stop();
                        timerRunning = false;
                        gameOver.setForeground(Color.WHITE);
                        gameOver.setText("Paused");
                        gameOver.show();
                        play.setText("Resume");
                        play.show();
                        play.repaint();
                        restart.show();
                        restart.repaint();
                    }
                    keys[6] = true;
                }
            }
            if (keys[1] && timerRunning) b.down();
            if (keys[2] && timerRunning) b.left();
            if (keys[3] && timerRunning) b.right();
            displayMainGrid(b.getMainGrid());
        }
        public void keyReleased(KeyEvent e){
            if (e.getKeyCode() == KeyEvent.VK_LEFT) keys[2] = false;
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) keys[3] = false;
            else if (e.getKeyCode() == KeyEvent.VK_UP) keys[0] = false;
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) keys[1] = false;
            else if (e.getKeyCode() == KeyEvent.VK_SPACE)keys[4] = false;
            else if (e.getKeyCode() == 67) keys[5] = false;
            else if (e.getKeyCode() == 27) keys[6] = false;
            displayMainGrid(b.getMainGrid());
        }

    }
}
