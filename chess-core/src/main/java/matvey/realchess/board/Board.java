package matvey.realchess.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static matvey.realchess.board.Square.square;

/**
 * <code>passant</code> — square with pawn
 * that advanced two squares
 */
public record Board(List<List<Square>>squares,
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

    private static List<List<Square>> squares(String s) {
        var board = new ArrayList<List<Square>>(8);
        var ranks = s.split("\n");
        for (var r = '1'; r <= '8'; r++) {
            var rank = new ArrayList<Square>(8);
            var rankSquares = ranks[r - '1'].split(" ");
            for (var f = 'a'; f <= 'h'; f++) {
                rank.add(square(f + "" + r, rankSquares[f - 'a']));
            }
            board.add(List.copyOf(rank));
        }
        return List.copyOf(board);
    }

    public Square squareAt(String position) {
        return squareAt((char) (position.charAt(0) - 'a'), (char) (position.charAt(1) - '1'));
    }

    public Square squareAt(char file, char rank) {
        return squares.get(rank).get(file);
    }

    public Board set(Square square) {
        var squares = new ArrayList<>(this.squares);
        var i = square.rank() - '1';
        var rankSquares = new ArrayList<>(squares.get(i));
        var j = square.file() - 'a';
        rankSquares.set(j, square);
        squares.set(i, List.copyOf(rankSquares));
        return new Board(List.copyOf(squares), movesCount, passant);
    }

    public Board passant(Square passant) {
        return new Board(set(passant).squares(), movesCount, Optional.of(passant));
    }
}
