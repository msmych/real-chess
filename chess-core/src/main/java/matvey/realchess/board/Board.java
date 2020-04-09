package matvey.realchess.board;

import matvey.realchess.board.piece.King;
import matvey.realchess.board.piece.Piece;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;
import static matvey.realchess.board.Square.square;

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
                """);
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
                """);
    }

    static Board fromString(String s) {
        return new Board(squares(s), 0, empty());
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

    public Board apply(Move move) {
        return switch (move.type()) {
            case BASIC -> set(square(move.start().position()))
                    .set(move.end().endMove(move.start().piece().orElseThrow()));
            case EN_PASSANT -> set(square(move.start().position()))
                    .set(move.end().endMove(move.start().piece().orElseThrow()))
                    .set(square(move.end().file() + "" + move.eaten().map(Piece::passantRank).orElseThrow()));
            case CASTLING -> set(square(move.start().position()))
                    .set(move.end().endMove(move.start().piece().orElseThrow()))
                    .set(square(move.end().rookPositionForCastling()))
                    .set(square(move.end().rookTargetForCastling())
                            .endMove(squareAt(move.end().rookPositionForCastling()).piece().orElseThrow()));

        };
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
