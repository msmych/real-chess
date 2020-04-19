package matvey.realchess.telegram.game;

import matvey.realchess.Board;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Optional.empty;
import static matvey.realchess.Board.initialBoard;

public record Game(Optional<Player>whitePlayer,
                   Optional<Player>blackPlayer,
                   Board board) {

    public record Player(int userId, int messageId) { }

    public static Game startGame() {
        return new Game(empty(), empty(), initialBoard());
    }

    public Game withPlayer1(int userId, int messageId) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            return new Game(Optional.of(new Player(userId, messageId)), empty(), board);
        } else {
            return new Game(empty(), Optional.of(new Player(userId, messageId)), board);
        }
    }
}
