package matvey.realchess.piece;

import matvey.realchess.Square;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static matvey.realchess.Board.emptyBoard;
import static matvey.realchess.Board.initialBoard;
import static matvey.realchess.Move.basicMove;
import static matvey.realchess.Move.castling;
import static matvey.realchess.Move.eat;
import static matvey.realchess.Square.square;
import static matvey.realchess.piece.King.kb;
import static matvey.realchess.piece.King.kw;
import static matvey.realchess.piece.Knight.nw;
import static matvey.realchess.piece.Rook.rb;
import static org.assertj.core.api.Assertions.assertThat;

class KingTest {

    @Test
    void e4_white_king_should_move_to_any_of_e5_f5_f4_f3_e3_d3_d4_d5() {
        var start = square("e4", "Kw");

        Stream.of("e5", "f5", "f4", "f3", "e3", "d3", "d4", "d5")
                .map(Square::square)
                .forEach(end -> assertThat(kw().move(initialBoard().set(start), start, end))
                        .hasValue(basicMove(start, end)));
    }

    @Test
    void e5_black_king_should_not_move_to_any_of_e8_f7_g7_g6() {
        var start = square("e5").endMove(kb());

        Stream.of("e8", "f7", "g7", "g6")
                .map(Square::square)
                .forEach(end -> assertThat(kb().move(emptyBoard().set(start), start, end))
                        .isEmpty());
    }

    @Test
    void g4h5_white_king_should_eat_black_rook() {
        var start = square("g4", "Kw");
        var end = square("h5", "Rb");

        var move = kw().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).hasValue(eat(start, end, end.piece()));
    }

    @Test
    void e1_white_king_should_castle_g1_and_c1() {
        var start = square("e1", "Kw");
        var rookRight = square("h1", "Rw");
        var rookLeft = square("a1", "Rw");
        var endRight = square("g1");
        var endLeft = square("c1");

        var moveRight = kw().move(emptyBoard().set(start).set(rookRight), start, endRight);
        var moveLeft = kw().move(emptyBoard().set(start).set(rookLeft), start, endLeft);

        assertThat(moveRight).hasValue(castling(start, endRight));
        assertThat(moveLeft).hasValue(castling(start, endLeft));
    }

    @Test
    void e8_black_king_should_castle_g8_and_c8() {
        var start = square("e8", "Kb");
        var rookRight = square("h8", "Rb");
        var rookLeft = square("a8", "Rb");
        var endRight = square("g8");
        var endLeft = square("c8");

        var moveRight = kb().move(emptyBoard().set(start).set(rookRight), start, endRight);
        var moveLeft = kb().move(emptyBoard().set(start).set(rookLeft), start, endLeft);

        assertThat(moveRight).hasValue(castling(start, endRight));
        assertThat(moveLeft).hasValue(castling(start, endLeft));
    }

    @Test
    void e1b1_white_king_should_not_castle() {
        var start = square("e1", "Kw");
        var rook = square("a1", "Rw");
        var end = square("b1");

        var move = kw().move(emptyBoard().set(start).set(rook), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void e1g1_white_king_should_not_castle_if_no_rook_on_h1() {
        var start = square("e1", "Kw");
        var end = square("g1");

        var move = kw().move(emptyBoard().set(start), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void e8g8_black_king_should_not_castle_if_not_initial() {
        var start = square("e8").endMove(kb());
        var rook = square("h8", "Rb");
        var end = square("g1");

        var move = kb().move(emptyBoard().set(start).set(rook), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void e8c8_black_king_should_not_castle_if_rook_is_not_initial() {
        var start = square("e8", "Kb");
        var rook = square("a8").endMove(rb());
        var end = square("c8");

        var move = kb().move(emptyBoard().set(start).set(rook), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void e8_black_king_should_not_castle_if_white_knight_on_b8_and_black_bishop_on_f8() {
        var start = square("e8", "Kb");
        var leftRook = square("a8", "Rb");
        var rightRook = square("h8", "Rb");
        var knight = square("b8").endMove(nw());
        var bishop = square("f8", "Bb");
        var leftEnd = square("c8");
        var rightEnd = square("g8");

        var leftMove = kb().move(emptyBoard().set(start).set(leftRook).set(rightRook).set(knight).set(bishop), start, leftEnd);
        var rightMove = kb().move(emptyBoard().set(start).set(leftRook).set(rightRook).set(knight).set(bishop), start, rightEnd);

        assertThat(leftMove).isEmpty();
        assertThat(rightMove).isEmpty();
    }

    @Test
    void e1g1_white_king_should_not_castle_if_is_in_check() {
        var start = square("e1", "Kw");
        var whiteRook = square("h1", "Rw");
        var blackRook = square("e5", "Rb");
        var end = square("g1");

        var move = kw().move(emptyBoard().set(start).set(whiteRook).set(blackRook), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void e8c8_black_king_should_not_castle_if_white_bishop_on_g5() {
        var start = square("e8", "Kb");
        var rook = square("a8", "Rb");
        var bishop = square("g5", "Bw");
        var end = square("c8");

        var move = kb().move(emptyBoard().set(start).set(rook).set(bishop), start, end);

        assertThat(move).isEmpty();
    }

    @Test
    void e1g1_white_king_should_not_castle_if_black_knight_on_g3() {
        var start = square("e1", "Kw");
        var rook = square("h1", "Rw");
        var knight = square("g3", "Nb");
        var end = square("g1");

        var move = kw().move(emptyBoard().set(start).set(rook).set(knight), start, end);

        assertThat(move).isEmpty();
    }
}