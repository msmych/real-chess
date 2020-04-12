package matvey.realchess;

import matvey.realchess.piece.King;
import matvey.realchess.piece.Pawn;
import matvey.realchess.piece.Piece;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.Math.abs;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;
import static matvey.realchess.Square.square;
import static matvey.realchess.piece.Piece.Color.BLACK;
import static matvey.realchess.piece.Piece.Color.WHITE;

/**
 * <code>passant</code> — square with pawn
 * that advanced two squares
 */
public record Board(Map<Character, Map<Character, Square>>squares,
                    int movesCount,
                    Optional<Square>passant) {

    public static Board emptyBoard() {
        return fromString("""
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                """, 0);
    }

    public static Board initialBoard() {
        return fromString("""
                Rb Nb Bb Qb Kb Bb Nb Rb
                Pb Pb Pb Pb Pb Pb Pb Pb
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                Pw Pw Pw Pw Pw Pw Pw Pw
                Rw Nw Bw Qw Kw Bw Nw Rw
                """, 0);
    }

    static Board fromString(String s, int movesCount) {
        return new Board(squares(s), movesCount, empty());
    }

    private static Map<Character, Map<Character, Square>> squares(String s) {
        var board = new HashMap<Character, Map<Character, Square>>(8);
        for (var f = 'a'; f <= 'h'; f++) {
            board.put(f, new HashMap<>());
        }
        var ranks = s.split("\n");
        for (var r = '1'; r <= '8'; r++) {
            var rankSquares = ranks[7 - (r - '1')].split(" ");
            for (var f = 'a'; f <= 'h'; f++) {
                board.get(f).put(r, square(f + "" + r, rankSquares[f - 'a']));
            }
        }
        return Map.copyOf(board);
    }

    public Square squareAt(String position) {
        return this.squares.get(position.charAt(0)).get(position.charAt(1));
    }

    public Piece.Color currentMove() {
        return movesCount % 2 == 0 ? WHITE : BLACK;
    }

    public Optional<Move.Result> move(String move) {
        return squareAt(move.substring(0, 2))
                .move(this, squareAt(move.substring(2, 4)))
                .map(this::apply)
                .map(board -> new Move.Result(board, board.winner()));
    }

    public Board apply(Move move) {
        var board = next().set(square(move.start().position()));
        return switch (move.type()) {
            case BASIC -> move.start().piece().filter(piece -> piece instanceof Pawn).isPresent() &&
                    abs(move.end().rank() - move.start().rank()) == 2
                    ? board.passant(move.end().endMove(move.start().piece().orElseThrow()))
                    : board.set(move.end().endMove(move.start().piece().orElseThrow()));
            case EN_PASSANT -> board
                    .set(move.end().endMove(move.start().piece().orElseThrow()))
                    .set(square(move.end().file() + "" + move.eaten().map(Piece::passantRank).orElseThrow()));
            case CASTLING -> board
                    .set(move.end().endMove(move.start().piece().orElseThrow()))
                    .set(square(move.end().rookPositionForCastling()))
                    .set(square(move.end().rookTargetForCastling())
                            .endMove(squareAt(move.end().rookPositionForCastling()).piece().orElseThrow()));

        };
    }

    private Optional<Piece.Color> winner() {
        return squares.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(square -> square.piece().map(piece -> piece.color() == currentMove()).orElse(false))
                .anyMatch(this::canMoveAnywhereFrom)
                ? empty()
                : Optional.of(currentMove() == WHITE ? BLACK : WHITE);
    }

    private boolean canMoveAnywhereFrom(Square start) {
        return squares.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .map(end -> start.piece().map(piece -> piece.move(this, start, end)))
                .anyMatch(Optional::isPresent);
    }

    public Board set(Square square) {
        var squares = new HashMap<>(this.squares);
        var f = square.file();
        var fileSquares = new HashMap<>(squares.get(f));
        var r = square.rank();
        fileSquares.put(r, square);
        squares.put(f, Map.copyOf(fileSquares));
        return new Board(Map.copyOf(squares), movesCount, passant);
    }

    public Board passant(Square passant) {
        return new Board(set(passant).squares(), movesCount, Optional.of(passant));
    }

    public Board next() {
        return new Board(squares, movesCount + 1, passant);
    }

    public boolean kingInCheck(Piece.Color color) {
        return squares.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(square -> square.piece()
                        .filter(piece -> piece instanceof King)
                        .filter(king -> king.color() == color)
                        .isPresent())
                .findAny()
                .map(square -> square.inCheck(this))
                .orElse(false);
    }

    @Override
    public String toString() {
        return range(0, 8)
                .mapToObj(r -> (char) ((8 - r) + '0'))
                .map(r -> range(0, 8)
                        .mapToObj(f -> (char) (f + 'a'))
                        .map(f -> squares.get(f).get(r))
                        .map(Square::toString)
                        .collect(joining(" ")))
                .collect(joining("\n"));

    }
}
