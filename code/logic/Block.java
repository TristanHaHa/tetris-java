import java.awt.*;
/**
 * class defines the identity, funcion, and behavior of a Tetronimo block
 *
 */
public class Block
{
    private Point[] loc;
    private Point topLeft;
    private boolean[][] shape;
    private Color color, shadowColor;
    private int length;
    private Tetronimo id;
    private final int initialY1 = -1;
    private final int initialY2 = -2;
    
    // constructor initializes instance variables of a random type of block
    public Block() {
        id = Tetronimo.getRandomTetronimo();
        newBlock();
    }
    
    // alternate constructor initializes instance variables of a specified type of block
    public Block(Tetronimo id) {
        this.id = id;
        newBlock();
    }
    
    // enum to store the different types of blocks
    private enum Tetronimo {
        I,J,L,O,S,T,Z;
        
        public static Tetronimo getRandomTetronimo() {
            int randomIndex = (int) (Math.random() * values().length);
            return values()[randomIndex];
        }
    }
    
    // getter methods
    
    public Point[] getLoc() {
        return loc;
    }
    public Point getTopLeft() {
        return topLeft;
    }
    public boolean[][] getshape() {
        return shape;
    }
    public Color getColor() {
        return color;
    }
    public Color getShadowColor() {
        return shadowColor;
    }
    public int getLength() {
        return length;
    }
    public Tetronimo getId() {
        return id;
    }
    public int getInitialY1() {
        return initialY1;
    }
    public int getInitialY2() {
        return initialY2;
    }
    
    // setter methods (the only instance variable that needs to be changed)
    
    public void setLoc(Point[] loc) {
        for (int i = 0; i < 4; i ++) {
            this.loc[i] = new Point(loc[i].x,loc[i].y);;
        }
    }
    
    // movement methods
    
    public void left() {
        for (int i = 0; i < loc.length; i++) {
            int x = loc[i].x;
            int y = loc[i].y;
            loc[i] = new Point(x-1,y);
        }
        topLeft = new Point(topLeft.x-1,topLeft.y);
    }
    public void right() {
        for (int i = 0; i < loc.length; i++) {
            int x = loc[i].x;
            int y = loc[i].y;
            loc[i] = new Point(x+1,y);
        }
        topLeft = new Point(topLeft.x+1,topLeft.y);
    }
    public void down() {
        for (int i = 0; i < loc.length; i++) {
            int x = loc[i].x;
            int y = loc[i].y;
            loc[i] = new Point(x,y+1);
        }
        topLeft = new Point(topLeft.x,topLeft.y+1);
    }
    public void up() {
        for (int i = 0; i < loc.length; i++) {
            int x = loc[i].x;
            int y = loc[i].y;
            loc[i] = new Point(x,y-1);
        }
        topLeft = new Point(topLeft.x,topLeft.y-1);
    }
    public boolean[][] rotate() {
        for (int r = 0; r < shape.length/2; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                swap(r,c,shape.length-1-r,c);
            }
        }
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < r; c++) {
                swap(r,c,c,r);
            }
        }
        return shape;
    }
    private void swap(int r1, int c1, int r2, int c2) {
        boolean temp = shape[r1][c1];
        shape[r1][c1] = shape[r2][c2];
        shape[r2][c2] = temp;
    }
    public boolean[][] unRotate() {
        rotate();
        rotate();
        return rotate();
    }
    
    // block creation methods
    
    public void newBlock() {
        loc = new Point[4];
        length = 3;
        shape = new boolean[length][length];
        if (id == Tetronimo.I) i();
        else if (id == Tetronimo.J) j();
        else if (id == Tetronimo.L) l();
        else if (id == Tetronimo.O) o();
        else if (id == Tetronimo.S) s();
        else if (id == Tetronimo.T) t();
        else if (id == Tetronimo.Z) z();
        makeShadowColor();
    }
    public void i(){
        length = 4;
        loc[0] = new Point(3,initialY1);
        loc[1] = new Point(4,initialY1);
        loc[2] = new Point(5,initialY1);
        loc[3] = new Point(6,initialY1);
        topLeft = new Point(3,initialY2);
        shape = new boolean[length][length];
        color = Color.CYAN;
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                if (r == 1) shape[r][c] = true;
                else shape[r][c] = false;
            }
        }
    }
    public void j(){
        loc[0] = new Point(3,initialY2);
        loc[1] = new Point(3,initialY1);
        loc[2] = new Point(4,initialY1);
        loc[3] = new Point(5,initialY1);
        topLeft = new Point(3,initialY2);
        color = Color.BLUE;
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                if (r == 1 || (r==0 && c==0)) shape[r][c] = true;
                else shape[r][c] = false;
            }
        }
    }
    public void l(){
        loc[0] = new Point(3,initialY1);
        loc[1] = new Point(4,initialY1);
        loc[2] = new Point(5,initialY1);
        loc[3] = new Point(5,initialY2);
        topLeft = new Point(3,initialY2);
        color = Color.ORANGE;
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                if (r == 1 || (r==length-1 && c==0)) shape[r][c] = true;
                else shape[r][c] = false;
            }
        }
    }
    public void o(){
        length = 2;
        loc[0] = new Point(4,initialY1);
        loc[1] = new Point(4,initialY2);
        loc[2] = new Point(5,initialY1);
        loc[3] = new Point(5,initialY2);
        topLeft = new Point(4,initialY2);
        shape = new boolean[length][length];
        color = Color.YELLOW;
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                shape[r][c] = true;
            }
        }
    }
    public void s(){
        loc[0] = new Point(3,initialY1);
        loc[1] = new Point(4,initialY1);
        loc[2] = new Point(4,initialY2);
        loc[3] = new Point(5,initialY2);
        topLeft = new Point(3,initialY2);
        color = Color.GREEN;
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                if (r == 0) {
                    if (c == 0) shape[r][c] = false;
                    else shape[r][c] = true;
                } else if (r == 1) {
                    if (c == length-1) shape[r][c] = false;
                    else shape[r][c] = true;
                } else shape[r][c] = false;
            }
        }
    }
    public void t(){
        loc[0] = new Point(3,initialY1);
        loc[1] = new Point(4,initialY1);
        loc[2] = new Point(4,initialY2);
        loc[3] = new Point(5,initialY1);
        topLeft = new Point(3,initialY2);
        color = Color.MAGENTA;
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                if (r == 1 || (r==0 && c==length/2)) shape[r][c] = true;
                else shape[r][c] = false;
            }
        }
    }
    public void z(){
        loc[0] = new Point(3,initialY2);
        loc[1] = new Point(4,initialY2);
        loc[2] = new Point(4,initialY1);
        loc[3] = new Point(5,initialY1);
        topLeft = new Point(3,initialY2);
        color = Color.RED;
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                if (r == 0) {
                    if (c == length-1) shape[r][c] = false;
                    else shape[r][c] = true;
                } else if (r == 1) {
                    if (c == 0) shape[r][c] = false;
                    else shape[r][c] = true;
                } else shape[r][c] = false;
            }
        }
    }
    public void makeShadowColor() {
        shadowColor = color.darker();
        for (int i = 0; i < 3; i++) shadowColor = shadowColor.darker();
    }
}


