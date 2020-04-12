package matvey.realchess;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Test
    void c5c4_black_pawn_should_not_eat_white_pawn_ahead() {
        var board = Board.fromString("""
                Rb Nb Bb Qb Kb Bb Nb Rb
                Pb Pb .. Pb Pb Pb Pb Pb
                .. .. .. .. .. .. .. ..
                .. .. Pb .. .. .. .. ..
                .. .. Pw .. .. .. .. ..
                .. .. .. .. .. .. .. ..
                Pw Pw .. Pw Pw Pw Pw Pw
                Rw Nw Bw Qw Kw Bw Nw Rw
                """, 3);

        var result = board.move("c5c4");

        assertThat(result).isEmpty();
    }
}