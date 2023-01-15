public class Pawn extends Piece{
        public Pawn(int x, int y, Side side, Board board) {
            // TODO: Call super constructor
            super (x, y, side, board);
        }

        @Override
        public boolean canMove(int destX, int destY) {
            // For the pawn, you are responsible for implementing the rule that they can move
            // 2 blocks forward when it is their first move. To check if it's a first move,
            // you can simply check if the pawn is still on its starting square.
            // Please see the PDF for a complete description of the pawn's movement rules.
            //TODO: Check piecerules.md for the movement rule for this piece :)
            if ((this.y == 1 || this.y == 6) && this.x == destX && (Math.abs(this.x - destX) == 0) && Math.abs(this.y - destY) <= 2) {
                return true;
            } else if (this.x == destX && (Math.abs(this.y - destY) <= 1)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getSymbol() {
            return this.getSide() == Side.BLACK ? "♟" : "♙";
        }
}



