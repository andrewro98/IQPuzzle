package iqpuzzle;

/**
 *
 * @author andrewrosen
 */
public class IQPuzzle {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KeyboardReader r = new KeyboardReader();
        int x = r.readInt("X: ");
        int y = r.readInt("Y: ");
        int n = r.readInt("N: ");
        game puzz = new game(x,y,n);
    }
    
}
