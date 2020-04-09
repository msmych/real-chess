package matvey.realchess.board.piece;

import matvey.realchess.board.Board;
import matvey.realchess.board.Move;
import matvey.realchess.board.Square;

import java.util.Optional;

import static java.lang.Math.abs;
import static java.util.Optional.empty;

public abstract class Piece {

    protected final Color color;

    protected Piece(Color color) {
        this.color = color;
    }

    public Optional<Move> move(Board board, Square start, Square end) {
        if (end.file() == start.file() && end.rank() == start.rank()) {
            return empty();
        }
        if (end.piece().map(piece -> piece.color == color).orElse(false)) {
            return empty();
        }
        return pieceMove(board, start, end)
                .filter(move -> !board.apply(move).kingInCheck(color));
    }

    public abstract Optional<Move> pieceMove(Board board, Square start, Square end);

    public final Color color() {
        return color;
    }

    protected final int rankDistance(Square start, Square end) {
        return switch (color) {
            case WHITE -> end.rank() - start.rank();
            case BLACK -> start.rank() - end.rank();
        };
    }

    public final char passantRank() {
        return switch (color) {
            case WHITE -> '4';
            case BLACK -> '5';
        };
    }

    protected final boolean canMoveAlongLine(Board board, Square start, Square end) {
        return canMoveHorizontally(board, start, end) || canMoveVertically(board, start, end);
    }

    private boolean canMoveHorizontally(Board board, Square start, Square end) {
        if (start.rank() != end.rank()) {
            return false;
        }
        var one = Integer.compare(end.file(), start.file());
        for (char f = (char) (start.file() + one); abs(end.file() - f) > 0; f += one) {
            if (board.squareAt(f + "" + start.rank()).piece().isPresent()) {
                return false;
            }
        }
        return true;
    }

    private boolean canMoveVertically(Board board, Square start, Square end) {
        if (start.file() != end.file()) {
            return false;
        }
        var one = Integer.compare(end.rank(), start.rank());
        for (char r = (char) (start.rank() + one); abs(end.rank() - r) > 0; r += one) {
            if (board.squareAt(start.file() + "" + r).piece().isPresent()) {
                return false;
            }
        }
        return true;
    }

    protected final boolean canMoveDiagonally(Board board, Square start, Square end) {
        var sf = start.file();
        var sr = start.rank();
        var ef = end.file();
        var er = end.rank();
        if (ef > sf && er > sr) {
            if (ef - sf != er - sr) {
                return false;
            }
            for (sf++, sr++; sf < ef && sr < er; sf++, sr++) {
                if (board.squareAt(sf + "" + sr).piece().isPresent()) {
                    return false;
                }
            }
        } else if (ef > sf && er < sr) {
            if (ef - sf != sr - er) {
                return false;
            }
            for (sf++, sr--; sf < ef && sr > er; sf++, sr--) {
                if (board.squareAt(sf + "" + sr).piece().isPresent()) {
                    return false;
                }
            }
        } else if (ef < sf && er > sr) {
            if (sf - ef != er - sr) {
                return false;
            }
            for (sf--, sr++; sf > ef && sr < er; sf--, sr++) {
                if (board.squareAt(sf + "" + sr).piece().isPresent()) {
                    return false;
                }
            }
        } else {
            if (sf - ef != sr - er) {
                return false;
            }
            for (sf--, sr--; sf > ef && sr > er; sf--, sr--) {
                if (board.squareAt(sf + "" + sr).piece().isPresent()) {
                    return false;
                }
            }
        }
        return true;
    }

    public enum Color {
        WHITE, BLACK
    }
}
