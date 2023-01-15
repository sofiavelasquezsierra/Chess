import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Game {
    Board b;
    Stack<String> moveHistory = new Stack<String>();
    Side currentTurn;

    public Game(){
        // todo: write a constructor that initializes the game with a new board
        // hint: you are also responsible for tracking whose turn it is
        b = new Board();
        currentTurn = Side.WHITE;
    }

    public static String getName() {
        return "Queen's Game-bit";
    }
    public boolean canMove(int x, int y, int destX, int destY, Side s) {

        Piece p = this.b.get(x, y);
        Piece dest_p = this.b.get(destX, destY);
        //Conditions for false:
        // - Origin or destination coordinates are outside the board
        if (x > 7 || x < 0 || y > 7 || y < 0) {
           return false;
        }
        else if (destX > 7 || destX < 0 || destY > 7 || destY < 0){
            return false;
        }
        //- Piece at origin is null
        else if (p == null) {
            return false;
            }
        //- If source and destination coordinates are the same
        else if (x == destX && y == destY) {
            return false;
            }
        //- Piece at origin is not of the same side as s
        //    - You can check this using piece.getSide()
        else if (p.getSide() != s) {
            return false;
        }
        //- Piece cannot move to the destination by piece movement rules
        //   - You should check this using Piece.canMove(destX, destY)
        else if (p.canMove(destX, destY) == false){
            return false;
        }
        //- Destination has a piece of the same Side as the player
        else if (dest_p.getSide() == s){
            return false;
        }
        //- piece must move "through" a piece to get from (x,y) to (destX,destY) (use isVisible())
        //   - The knight is the exception to this rule. The knight can hop "over" pieces, so be sure to check for this.
       else if (p.getClass() != Knight.class) {
            if (isVisible(x, y, destX, destY) == true) {
                return false;
            }
        }

        // TODO write a method that checks if a piece at coordinates x,y can move to coordinates destX,destY
        else if (p.canMove(destX, destY)){
            return true;
        }
        return true;
    }

    /**
     * This method is provided to you in order to help with canMove().
     *
     * In chess, no piece except the knight can "move through" or "hop over" a piece that's in its way
     *
     * This method checks that there are no pieces along the path from (x,y) to (destX,destY).
     * Note that a "path" is only defined if (x,y) and (destX,destY) are on the same row, column, or diagonal.
     * If the requested path is undefined, the method throws an exception.
     *
     * If the path is defined and no piece is found along the path, the method returns true.
     *
     * Don't worry about how this method works or tests and edge cases for it, we will
     * grade you assuming you keep it exactly as provided and use it as a part of your
     * canMove() method.
     */
    private boolean isVisible(int x, int y, int destX, int destY) {
        int diffX = destX - x;
        int diffY = destY - y;

        boolean validCheck = (diffX == 0 || diffY == 0) || (Math.abs(diffX) == Math.abs(diffY));
        if (!validCheck) {
            try {
                throw new Exception("The 'path' between the squares (" + x + ", " + y + ") and (" + destX + ", " + destY +") is undefined");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Diagonal
        if (Math.abs(diffX) == Math.abs(diffY) && Math.abs(diffX) > 1) {
            //Determine direction of move
            int dirX = diffX > 0 ? 1 : -1;
            int dirY = diffY > 0 ? 1 : -1;
            for (int i = x + dirX, j = y + dirY; i != destX && j != destY; i += dirX, j += dirY) {
                if (b.get(i, j) != null) {
                    return false;
                }
            }
        }

        //Linear
        if ((diffX == 0 && Math.abs(diffY) > 1) || (diffY == 0 && Math.abs(diffX) > 1)) {
            if (diffX == 0) {
                int dirY = diffY > 0 ? 1 : -1;
                for (int j = y + dirY; j != destY; j += dirY) {
                    if (b.get(x, j) != null) {
                        return false;
                    }
                }
            } else {
                int dirX = diffX > 0 ? 1 : -1;
                for (int i = x + dirX; i != destX; i += dirX) {
                    if (b.get(i, y) != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void appendCheckToHistory(Side side){
        moveHistory.push(side.toString()+ " is in check");
    }

    private void appendWinToHistory(Side side){
        moveHistory.push(side.toString()+ " has won");
    }

    private void appendMoveToHistory(int x, int y, int destX, int destY, Side side){
        Piece toMove = b.get(x,y);
        Piece toCapture = b.get(destX, destY);
        if(toCapture == null){
            moveHistory.push(side.toString() + toMove.getSymbol() + " at "+ x + ", " + y + " to " + destX + ", " + destY);
        }else{
            moveHistory.push(side.toString() + toMove.getSymbol() + " at " + x + ", " + y + " captures " + toCapture.getSymbol() + " at " + destX + ", " + destY);
        }
    }


    /**
     * This method takes as input the coordinates of the piece to move and of the destination to move to.
     *
     * It returns true if the move is valid and false otherwise.
     *
     * Conditions for a valid move are determined in canMove()
     *
     * Upon a successful move, this method also:
     * - updates the board to reflect the move
     * - tracks the move in game history
     *   - also tracks in history if the move was:
     *     - a capture
     *     - result in check EDIT: for either side
     *     - EDIT: also if it results in a win / king capture
     * - updates the current player's turn
     *
     *  x The x coordinate of the piece to move
     *  y The y coordinate of the piece to move
     *  destX The x coordinate of the destination to move to
     *  destY The y coordinate of the destination to move to
     */
    public boolean move(int x, int y, int destX, int destY){
        // TODO write this method

        if (canMove(x, y, destX, destY, this.currentTurn)){
            appendMoveToHistory(x, y , destX, destY, this.currentTurn);
            Piece p = this.b.get(x, y);
            Piece dest_p = this.b.get(destX, destY);
            p.move(destX, destY);

            if(this.isInCheck(this.currentTurn)){
                this.appendCheckToHistory(this.currentTurn);
            }
            if (this.isInCheck(Side.negate(this.currentTurn))){
                this.appendCheckToHistory((Side.negate(this.currentTurn)));
            }

            Piece k = this.b.getKing(Side.negate(this.currentTurn));
            if (k != null){
                this.appendWinToHistory(this.currentTurn);
            }
        }
        return false;
    }

    /**
     * Return true if the King of Side side can be captured by any of
     * the opponent's pieces.
     *
     */
    public boolean isInCheck(Side side) {
        // TODO write this method
        King k = this.b.getKing(Side.negate(side));
        List Pieces = b.getPieces(side);

        if (k != null) {
            for (int i = 0; i < Pieces.size(); i++) {
                Piece tempPiece = (Piece) Pieces.get(i);
                if (canMove(tempPiece.x, tempPiece.y, k.x, k.y, side) == true) {
                    return true;
                } else {
                    return false;
                }
            }
            //none of the pieces can move to the king
        }
        else{
            return false;
        }
        return false;
    }

    /**
     * Ensures that the game is in the exact same state as a new game
     */
    public void reset(){
        while(!moveHistory.empty()){
            System.out.println(moveHistory.pop());
        }
        b.fillBoard();
        currentTurn = Side.WHITE;
    }


    public static void main(String[] args){
        ArrayList<String> arr = new ArrayList<String>();

        arr.add("hello");
        arr.add("hi");
        arr.add("hi");

        Iterator<String> iter = arr.iterator();

        while (((Iterator<?>) iter).hasNext()) {
            iter.next();

            System.out.println(iter.next().equals(iter.next()));
        }

        iter = arr.iterator();

        System.out.println(iter.next());
    }

    /**
     * Return an array of Strings containing every successful move made during the game,
     * and every time a move resulted in check.
     */
    public String[] moveHistory(){
        String[] out = new String[moveHistory.size()];
        for(int i = 0; i < moveHistory.size(); i++){
            out[i] = moveHistory.get(i);
        }
        return out;
    }

    public Side getCurrentTurn(){
        return currentTurn;
    }
}
