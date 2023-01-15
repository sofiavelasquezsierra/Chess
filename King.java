//@author Sofia Velasquez Sierra

public class King extends Piece {
    public King(int x, int y, Side side, Board board) {
        // TODO: Call super constructor
        super (x, y, side, board);
    }

    @Override
    public boolean canMove(int destX, int destY) {

        //TODO: Check piecerules.md for the movement rule for this piece :)
       if (Math.abs(this.x - destX) <= 1 && Math.abs(this.y  - destY) <=1)
           return true;
       else
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "♚" : "♔";
    }
}
