package matvey.realchess.piece;

import matvey.realchess.Square;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static matvey.realchess.Board.emptyBoard;
import static matvey.realchess.Move.eat;
import static matvey.realchess.Move.basicMove;
import static matvey.realchess.Square.square;
import static matvey.realchess.piece.Knight.nb;
import static matvey.realchess.piece.Knight.nw;
import static org.assertj.core.api.Assertions.assertThat;

class KnightTest {

    @Test
    void d4_white_knight_should_move_to_any_of_e6_f5_f3_e2_c2_b3_b5_c6() {
        var start = square("d4", "Nw");

        Stream.of("e6", "f5", "f3", "e2", "c2", "b3", "b5", "c6")
                .map(Square::square)
                .forEach(end -> assertThat(nw().move(emptyBoard().set(start), start, end))
                        .hasValue(basicMove(start, end)));
    }

    @Test
    void a5c4_black_knight_should_eat_white_bishop() {
        var start = square("a5", "Nb");
        var end = square("c4", "Bw");

        var move = nb().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).hasValue(eat(start, end, end.piece()));
    }

    @Test
    void g1f3_white_knight_should_not_move_if_white_pawn_on_f3() {
        var start = square("g1", "Nw");
        var end = square("f3", "Pw");

        var move = nw().move(emptyBoard().set(start).set(end), start, end);

        assertThat(move).isEmpty();
    }

}