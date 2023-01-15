//@author Sofia Velasquez Sierra

public class Knight extends Piece{
    public Knight(int x, int y, Side side, Board board) {
        // TODO: Call super constructor
        super (x, y, side, board);
    }

    @Override
    public boolean canMove(int destX, int destY) {

        //implement rule to hop over pieces
        //TODO: Check piecerules.md for the movement rule for this piece :)
        if ((Math.abs(this.x - destX) == 2 && Math.abs(this.y  - destY) == 1) || (Math.abs(this.x - destX) == 1 && Math.abs(this.y  - destY) == 2))
        return false;
         else
            return true;
        }

        @Override
        public String getSymbol() {
            return this.getSide() == Side.BLACK ? "♞" : "♘";
        }
}



