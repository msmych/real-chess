package matvey.realchess.piece;

import matvey.realchess.Board;
import matvey.realchess.Move;
import matvey.realchess.Square;

import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.Move.basicMove;
import static matvey.realchess.Move.eat;
import static matvey.realchess.piece.Piece.Color.BLACK;
import static matvey.realchess.piece.Piece.Color.WHITE;

public final class Rook extends Piece {

    private Rook(Color color) {
        super(color);
    }

    public static Rook rb() {
        return new Rook(BLACK);
    }

    public static Rook rw() {
        return new Rook(WHITE);
    }

    @Override
    public Optional<Move> pieceMove(Board board, Square start, Square end) {
        if (canMoveAlongLine(board, start, end)) {
            return end.piece()
                    .map(piece -> eat(start, end, piece))
                    .or(() -> Optional.of(basicMove(start, end)));
        }
        return empty();
    }

    @Override
    public String toString() {
        return "R" + color;
    }
}
