package matvey.realchess.board.piece;

import matvey.realchess.board.Board;
import matvey.realchess.board.Move;
import matvey.realchess.board.Square;

import java.util.Optional;

import static java.lang.Math.abs;
import static java.util.Optional.empty;
import static matvey.realchess.board.Move.basicMove;
import static matvey.realchess.board.Move.eat;
import static matvey.realchess.board.piece.Piece.Color.BLACK;
import static matvey.realchess.board.piece.Piece.Color.WHITE;

public final class Knight extends Piece {

    private Knight(Color color) {
        super(color);
    }

    public static Knight nb() {
        return new Knight(BLACK);
    }

    public static Knight nw() {
        return new Knight(WHITE);
    }

    @Override
    public Optional<Move> pieceMove(Board board, Square start, Square end) {
        if (isKnightMove(start, end)) {
            return end.piece()
                    .map(piece -> eat(start, end, piece))
                    .or(() -> Optional.of(basicMove(start, end)));
        }
        return empty();
    }

    private boolean isKnightMove(Square start, Square end) {
        return abs(end.file() - start.file()) == 2 && abs(end.rank() - start.rank()) == 1 ||
            abs(end.file() - start.file()) == 1 && abs(end.rank() - start.rank()) == 2;
    }

    @Override
    public String toString() {
        return "N" + color;
    }
}
