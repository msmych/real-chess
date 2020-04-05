package matvey.realchess.board.piece;

import matvey.realchess.board.Square;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static matvey.realchess.board.Board.emptyBoard;
import static matvey.realchess.board.Move.eat;
import static matvey.realchess.board.Move.basicMove;
import static matvey.realchess.board.Square.square;
import static matvey.realchess.board.piece.Queen.Qb;
import static matvey.realchess.board.piece.Queen.Qw;
import static org.assertj.core.api.Assertions.assertThat;

class QueenTest {

    @Test
    void d5_white_queen_should_move_to_any_of_g8_e4_a2_b7_d6_h5_d3_a5() {
        var start = square("d5", "Qw");

        Stream.of("g8", "e4", "a2", "b7", "d6", "h5", "d3", "a5")
                .map(Square::square)
                .forEach(end -> assertThat(Qw.move(emptyBoard().set(start), start, end))
                        .hasValue(basicMove(start, end)));
    }

    @Test
    void d5_black_queen_should_move_to_any_of_f8_g4_a3() {
        var start = square("e5", "Qb");

        Stream.of("f8", "g4", "a3")
                .map(Square::square)
                .forEach(end -> assertThat(Qb.move(emptyBoard().set(start), start, end))
                        .isEmpty());
    }

    @Test
    void b5e2_black_queen_should_eat_white_pawn() {
        var start = square("b5", "Qb");
        var end = square("e2", "Pw");

        var move = Qb.move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).hasValue(eat(start, end, end.piece()));
    }

    @Test
    void e4c2_black_queen_should_not_move_if_black_knight_on_d3() {
        var start = square("e4", "Qb");
        var knight = square("d3", "Nb");
        var end = square("c2");

        var move = Qb.move(emptyBoard().set(start).set(knight), start, end);

        assertThat(move).isEmpty();
    }

}