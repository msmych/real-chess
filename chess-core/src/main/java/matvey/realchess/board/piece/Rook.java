package matvey.realchess.board.piece;

import matvey.realchess.board.Board;
import matvey.realchess.board.Move;
import matvey.realchess.board.Square;

import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.board.Move.basicMove;
import static matvey.realchess.board.Move.eat;
import static matvey.realchess.board.piece.Piece.Color.BLACK;
import static matvey.realchess.board.piece.Piece.Color.WHITE;

public final class Rook extends Piece {

    public static Rook Rw = new Rook(WHITE);
    public static Rook Rb = new Rook(BLACK);

    private Rook(Color color) {
        super(color);
    }

    @Override
    Optional<Move> doMove(Board board, Square start, Square end) {
        if (canMoveAlongLine(board, start, end)) {
            return end.piece()
                    .map(piece -> eat(start, end, piece))
                    .or(() -> Optional.of(basicMove(start, end)));
        }
        return empty();
    }
}
