import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
/**
 * class handles the logic of the game
 *
 */
public class Board
{
    private javax.swing.Timer timer;
    private boolean[][] landedGrid;
    private Color[][] landedColorGrid;
    private ArrayList<Block> blocks, futureBlocks;
    private int score, level, linesCleared, timeLanded, lineThreshold;
    private boolean holding, landing;
    private Block holdBlock, shadowBlock;
    private MyFrame mf;

    // constructor initializes instance variables
    public Board(MyFrame mf) {
        this.mf = mf;
        timer = new javax.swing.Timer(10, new TimerListener());
        init();
    }

    // helper method to the constructor that initializes instance variables
    // is a separate method so it can be called again when the player restarts the game
    private void init() {
        linesCleared = 0;
        score = 0;
        level = 0;
        timeLanded = 0;
        lineThreshold = 5;
        blocks = new ArrayList<Block>();
        futureBlocks = new ArrayList<Block>();
        holdBlock = null;
        landedGrid = new boolean[20][10];
        landedColorGrid = new Color[20][10];
        holding = false;
        landing = false;
        for (int i = 0; i < 4; i++) {
            futureBlocks.add(new Block());
        }
        for (int r = 0; r < landedGrid.length; r++) {
            for (int c = 0; c < landedGrid[0].length; c++) {
                landedGrid[r][c] = false;
                landedColorGrid[r][c] = Color.BLACK;
            }
        }
        newBlock();
    }

    // getter methods

    public boolean[][] getLandedGrid() {
        return landedGrid;
    }
    public Color[][] getLandedColorGrid() {
        return landedColorGrid;
    }
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
    public ArrayList<Block> getFutureBlocks() {
      return futureBlocks;
    }
    public int getScore() {
        return score;
    }
    public int getLevel() {
        return level;
    }
    public int getLinesCleared() {
        return linesCleared;
    }
    public boolean isHolding() {
        return holding;
    }
    public boolean isLanding() {
        return landing;
    }
    public Block getHoldBlock() {
        return holdBlock;
    }
    public Block getShadowBlock() {
        return shadowBlock;
    }

    // setter methods

    public void setlandedGrid(boolean[][] landedGrid) {
        this.landedGrid = landedGrid;
    }
    public void setLandedColorGrid(Color[][] color) {
        this.landedColorGrid = landedColorGrid;
    }
    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = blocks;
    }
    public void setFutureBlocks(ArrayList<Block> futureBlocks) {
        this.futureBlocks = futureBlocks;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setLinesCleared(int linesCleared) {
        this.linesCleared = linesCleared;
    }
    public void setHolding(boolean holding) {
        this.holding = holding;
    }
    public void setLanding(boolean landing) {
        this.landing = landing;
    }
    public void setHoldBlock(Block holdBlock) {
        this.holdBlock = holdBlock;
    }
    public void setShadowBlock(Block shadowBlock) {
        this.shadowBlock = shadowBlock;
    }

    // other getter methods

    public Block getCurrentBlock() {
        return blocks.get(blocks.size()-1);
    }
    public Point[] getCurrentLoc() {
        return getCurrentBlock().getLoc();
    }
    public Point getTopLeft() {
        return getCurrentBlock().getTopLeft();
    }
    // grid getter methods that return 2d color arrays in order to be displayed in MyFrame

    public Color[][] getSideGrid() {
        Color[][] grid = new Color[12][4];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                grid[r][c] = Color.BLACK;
            }
        }
        for (int i = 0; i < futureBlocks.size(); i++) {
            for (int j = 0; j < 4; j++) {
                int r = futureBlocks.get(i).getLoc()[j].y+(3*(i+1));
                int c = futureBlocks.get(i).getLoc()[j].x-3;
                grid[r][c] = futureBlocks.get(i).getColor();
            }
        }
        return grid;
    }
    public Color[][] getHoldGrid() {
        Color[][] grid = new Color[3][4];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                grid[r][c] = Color.BLACK;
            }
        }
        if (holdBlock != null) {
            for (int i = 0; i < 4; i++) {
                int r = holdBlock.getLoc()[i].y + 3;
                int c = holdBlock.getLoc()[i].x - 3;
                grid[r][c] = holdBlock.getColor();
            }
        }
        return grid;
    }
    public Color[][] getMainGrid() {
        Color[][] grid = new Color[20][10];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                grid[r][c] = Color.BLACK;
                if (!landedColorGrid[r][c].equals(Color.BLACK)) grid[r][c] = landedColorGrid[r][c];
            }
        }
        for (int i = 0; i < shadowBlock.getLoc().length; i++) {
            int r = shadowBlock.getLoc()[i].y;
            int c = shadowBlock.getLoc()[i].x;
            if (r >= 0 && r <= 19 && c >= 0 && c <= 9) grid[r][c] = getCurrentBlock().getShadowColor();
        }
        for (int i = 0; i < getCurrentLoc().length; i++) {
            int r = getCurrentLoc()[i].y;
            int c = getCurrentLoc()[i].x;
            if (r >= 0 && r <= 19 && c >= 0 && c <= 9) grid[r][c] = getCurrentBlock().getColor();
        }
        return grid;
    }

    // simple movement methods

    public void up() {
        this.getCurrentBlock().up();
    }
    public boolean down() {
        if (canMoveDown()) {
            getCurrentBlock().down();
            return true;
        } else {
            landing = true;
            timer.start();
            return false;
        }
    }
    public boolean right() {
        if (canMoveRight()) {
            if (landing) timeLanded -= 20;
            getCurrentBlock().right();
            updateShadow();
            return true;
        } else return false;
    }
    public boolean left() {
        if (canMoveLeft()) {
            if (landing) timeLanded -= 20;
            getCurrentBlock().left();
            updateShadow();
            return true;
        } else return false;
    }
    public void slam() {
        if (canMoveDown()) {
            score+=1;
            this.down();
            slam();
        } else land();
    }
    public void updateShadow() {
        shadowBlock.setLoc(getCurrentLoc());
        shadowSlam();
    }
    public void shadowSlam() {
        while (shadowCanMoveDown()) shadowBlock.down();
    }

    // complex movement methods

    public void hold() {
        holding = true;
        Block temp;
        if (holdBlock == null) {
            holdBlock = new Block(getCurrentBlock().getId());
            blocks.remove(blocks.size()-1);
            newBlock();
        } else {
            temp = holdBlock;
            holdBlock = new Block(getCurrentBlock().getId());
            blocks.remove(blocks.size()-1);
            blocks.add(temp);
        }
        shadowBlock = new Block(getCurrentBlock().getId());
        updateShadow();
    }
    public void rotate() {
        Point[] temp = new Point[4];
        boolean[][] rotated = getCurrentBlock().rotate();
        int index = 0;
        for (int r = 0; r < rotated.length; r++) {
            for (int c = 0; c < rotated[0].length; c++) {
                if (rotated[r][c] == true) {
                    int x = c+getTopLeft().x;
                    int y = r+getTopLeft().y;
                    temp[index++] = new Point(x,y);
                }
            }
        }
        boolean shouldRotate = true;
        // checks if wall or floor kick is needed
        if (!pointIsValid(temp)) {
            for(int i = 0; i < temp.length; i++) {
                int x = temp[i].x;
                int y = temp[i].y;
                temp[i] = new Point(x, y+1);
            }
            if (!pointIsValid(temp)) {
                for(int i = 0; i < temp.length; i++) {
                    int x = temp[i].x;
                    int y = temp[i].y;
                    temp[i] = new Point(x+1, y-2);
                }
                if (!pointIsValid(temp)) {
                    for(int i = 0; i < temp.length; i++) {
                        int x = temp[i].x;
                        int y = temp[i].y;
                        temp[i] = new Point(x-2, y);
                    }
                    if (!pointIsValid(temp)) {
                        for(int i = 0; i < temp.length; i++) {
                            int x = temp[i].x;
                            int y = temp[i].y;
                            temp[i] = new Point(x+3, y);
                        }
                        if (!pointIsValid(temp)) {
                            for(int i = 0; i < temp.length; i++) {
                                int x = temp[i].x;
                                int y = temp[i].y;
                                temp[i] = new Point(x-4, y);
                            }
                            if (!pointIsValid(temp)) {
                                shouldRotate = false;
                                getCurrentBlock().unRotate();
                            }
                        }
                    }
                }
            }
        }
        if (shouldRotate) {
            getCurrentBlock().setLoc(temp);
            updateShadow();
            if (landing) timeLanded -= 20;
        }
    }
    public boolean pointIsValid(Point[] p) {
        boolean valid = true;
        for (int i = 0; i < p.length; i++) {
            int x = p[i].x;
            int y = p[i].y;
            if (y < 0) y = 0;
            if (x < 0 || x > 9) valid = false;
            if (y > 19) valid = false;
            if (valid)
                if (landedGrid[y][x] == true) valid = false;
        }
        return valid;
    }

    // update methods

    public void update() {
        this.down();
    }
    public void newBlock() {
        blocks.add(futureBlocks.remove(0));
        shadowBlock = new Block(getCurrentBlock().getId());
        shadowSlam();
        futureBlocks.add(new Block());
        this.down();
    }
    public void levelUp() {
        level++;
        mf.levelUp();
    }
    public void land() {
        if (checkToEnd() == true) {
            timeLanded = 0;
            mf.endGame();
            endGame();
        }
        else {
            score+=10;
            landing = false;
            holding = false;
            updateColors();
            checkToClear();
            newBlock();
        }
    }
    public void checkToClear() {
        HashSet<Integer> heightSet = new HashSet<Integer>();
        Iterator<Integer> itr;
        for (int i = 0; i < getCurrentLoc().length; i++) {
            int x = getCurrentLoc()[i].x;
            int y = getCurrentLoc()[i].y;
            if (x <= 9 && x >= 0) {
                if (y < 0) y = 0;
                landedGrid[y][x] = true;
            }
            heightSet.add(new Integer(y));
        }
        itr = heightSet.iterator();
        while (itr.hasNext()) {
            boolean continuous = true;
            int r = itr.next();
            for (int c = 0; c < 10; c++) {
                if (landedGrid[r][c] == false) continuous = false;
            }
            if (!continuous) {
                itr.remove();
            }
        }
        if (heightSet.size() > 0) clearLines(hashSetToArr(heightSet), heightSet.size());
    }
    private int[] hashSetToArr(HashSet<Integer> set) {
        int[] arr = new int[set.size()];
        int index = 0;
        for (Integer i : set) {
            arr[index++] = i;
        }
        return arr;
    }
    public void clearLines(int[] fullRows, int size) {
        for (int r = fullRows[size-1]; r >= 0; r--) {
                System.out.println(r);
            for (int c = 0; c < 10; c++) {
                int aboveRow = r-size;
                if (aboveRow >= 0) {
                    landedGrid[r][c] = landedGrid[aboveRow][c];
                    landedColorGrid[r][c] = landedColorGrid[aboveRow][c];
                } else {
                    landedGrid[r][c] = false;
                    landedColorGrid[r][c] = Color.BLACK;
                }
            }
        }

        if (size == 1) score += 100*(level+1);
        if (size == 2) score += 300*(level+1);
        if (size == 3) score += 500*(level+1);
        if (size == 4) score += 800*(level+1);

        linesCleared += size;
        if (linesCleared >= lineThreshold) {
            lineThreshold += 5;
            levelUp();
        }
        mf.displayText(score, linesCleared);
    }
    public void updateColors() {
        for (int i = 0; i < getCurrentLoc().length; i++) {
            int x = getCurrentLoc()[i].x;
            int y = getCurrentLoc()[i].y;
            if (y < 0) y = 0;
            landedColorGrid[y][x] = getCurrentBlock().getColor();
        }
    }
    public void endGame() {
        timer.stop();
        blocks.clear();
        futureBlocks.clear();
        holdBlock = null;
        init();
    }

    // checker methods

    public boolean canMoveDown() {
        for (int i = 0; i < getCurrentLoc().length; i++) {
            int x = getCurrentLoc()[i].x;
            int y = getCurrentLoc()[i].y;
            if (y + 1 > 19) return false;
            if (y + 1 >= 0) {
                if (landedGrid[y+1][x] == true) return false;
            } else return true;
        }
        return true;
    }
    public boolean shadowCanMoveDown() {
        for (int i = 0; i < shadowBlock.getLoc().length; i++) {
            int x = shadowBlock.getLoc()[i].x;
            int y = shadowBlock.getLoc()[i].y;
            if (y + 1 > 19) return false;
            if (y + 1 >= 0) {
                if (landedGrid[y+1][x] == true) return false;
            } else return true;
        }
        return true;
    }
    public boolean canMoveRight() {
        for (int i = 0; i < getCurrentLoc().length; i++) {
            int x = getCurrentLoc()[i].x;
            int y = getCurrentLoc()[i].y;
            if (y < 0) y = 0;
            if (x + 1 > 9) return false;
            else if (landedGrid[y][x+1] == true) return false;
        }
        return true;
    }
    public boolean canMoveLeft() {
        for (int i = 0; i < getCurrentLoc().length; i++) {
            int x = getCurrentLoc()[i].x;
            int y = getCurrentLoc()[i].y;
            if (y < 0) y = 0;
            if (x - 1 < 0) return false;
            else if (landedGrid[y][x-1] == true) return false;
        }
        return true;
    }
    public boolean canRotate() {
        return true;
    }
   public boolean checkToEnd() {
        Point[] p = getCurrentBlock().getLoc();
        int yMax = p[0].y;
        for (int i = 1; i < p.length; i++) {
            yMax = Math.max(yMax,p[i].y);
        }
        return (yMax < 0);
    }

    // timer action listener to delay the landing of a block once it hits the ground
    private class TimerListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if (canMoveDown()) {
                timeLanded = 0;
                timer.stop();
            } else timeLanded++;
            if (timeLanded >= 20) {
                timeLanded = 0;
                land();
                timer.stop();
            }

        }
    }
}
