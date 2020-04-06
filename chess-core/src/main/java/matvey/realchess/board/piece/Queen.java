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

public final class Queen extends Piece {

    private Queen(Color color) {
        super(color);
    }

    public static Queen qb() {
        return new Queen(BLACK);
    }

    public static Queen qw() {
        return new Queen(WHITE);
    }

    @Override
    Optional<Move> doMove(Board board, Square start, Square end) {
        if (canMoveAlongLine(board, start, end) || canMoveDiagonally(board, start, end)) {
            return end.piece()
                    .map(piece -> eat(start, end, piece))
                    .or(() -> Optional.of(basicMove(start, end)));
        }
        return empty();
    }

}
