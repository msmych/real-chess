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

public final class Bishop extends Piece {

    private Bishop(Color color) {
        super(color);
    }

    public static Bishop bb() {
        return new Bishop(BLACK);
    }

    public static Bishop bw() {
        return new Bishop(WHITE);
    }

    @Override
    public Optional<Move> pieceMove(Board board, Square start, Square end) {
        if (canMoveDiagonally(board, start, end)) {
            return end.piece()
                    .map(piece -> eat(start, end, piece))
                    .or(() -> Optional.of(basicMove(start, end)));
        }
        return empty();
    }

    @Override
    public String toString() {
        return "B" + color;
    }
}
