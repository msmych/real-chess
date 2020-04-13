package matvey.realchess;

import matvey.realchess.piece.Piece;

import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.Move.Type.*;

public record Move(Square start,
                   Square end,
                   Type type,
                   Optional<Piece>eaten) {

    public record Result(Board board, Optional<Square>pawnToPromote, Optional<Piece.Color>winner) {}

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

    public static Move enPassant(Square start, Square end, Piece passant) {
        return new Move(start, end, EN_PASSANT, Optional.of(passant));
    }

    enum Type {
        BASIC, CASTLING, EN_PASSANT
    }
}
