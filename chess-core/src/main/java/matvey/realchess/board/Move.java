package matvey.realchess.board;

import matvey.realchess.board.piece.Piece;

import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.board.Move.Type.BASIC;
import static matvey.realchess.board.Move.Type.CASTLING;

public record Move(Square start,
                   Square end,
                   Type type,
                   Optional<Piece>eaten) {

    public static Move basicMove(Square start, Square end) {
        return new Move(start, end, BASIC, empty());
    }

    public static Move eat(Square start, Square end, Optional<Piece> eaten) {
        return new Move(start, end, BASIC, eaten);
    }

    public static Move eat(Square start, Square end, Piece eaten) {
        return eat(start, end, Optional.of(eaten));
    }

    public static Move castling(Square start, Square end) {
        return new Move(start, end, CASTLING, empty());
    }

    enum Type {
        BASIC, CASTLING
    }
}
