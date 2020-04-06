package matvey.realchess.board.piece;

import matvey.realchess.board.Square;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static matvey.realchess.board.Board.emptyBoard;
import static matvey.realchess.board.Board.initialBoard;
import static matvey.realchess.board.Move.eat;
import static matvey.realchess.board.Move.basicMove;
import static matvey.realchess.board.Square.square;
import static matvey.realchess.board.piece.Rook.rb;
import static matvey.realchess.board.piece.Rook.rw;
import static org.assertj.core.api.Assertions.assertThat;

class RookTest {

    @Test
    void c4_white_rook_should_move_to_any_of_c8_g4_c3_a4() {
        var start = square("c4", "Rw");

        Stream.of("c8", "g4", "c3", "a4")
                .map(Square::square)
                .forEach(end -> assertThat(rw().move(emptyBoard().set(start), start, end))
                        .hasValue(basicMove(start, end)));
    }

    @Test
    void f5_white_rook_should_not_move_to_any_of_c8_g6_e3() {
        var start = square("f5", "Rw");

        Stream.of("c8", "g6", "e3")
                .map(Square::square)
                .forEach(end -> assertThat(rw().move(emptyBoard().set(start), start, end))
                        .isEmpty());
    }

    @Test
    void c5h5_black_rook_should_eat_white_knight() {
        var start = square("c5", "Rb");
        var end = square("h5", "Nw");

        var move = rb().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).hasValue(eat(start, end, end.piece()));
    }

    @Test
    void a1a5_white_rook_should_not_move_if_white_pawn_on_a2() {
        var start = square("a1", "Rw");
        var end = square("a5");

        var move = rw().move(initialBoard(), start, end);

        assertThat(move).isEmpty();
    }
}