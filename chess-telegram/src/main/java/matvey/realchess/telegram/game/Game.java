package matvey.realchess.telegram.game;

import matvey.realchess.Board;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Optional.empty;
import static matvey.realchess.Board.initialBoard;
import static matvey.realchess.piece.Piece.Color.WHITE;

public final record Game(Optional<Player>whitePlayer,
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

    public Game withPlayer2(int userId, int messageId) {
        if (whitePlayer.isEmpty()) {
            return new Game(Optional.of(new Player(userId, messageId)), blackPlayer, board);
        } else {
            return new Game(whitePlayer, Optional.of(new Player(userId, messageId)), board);
        }
    }

    public Optional<Player> currentPlayer() {
        if (whitePlayer.isEmpty() || blackPlayer.isEmpty()) {
            return empty();
        }
        return board.currentMove() == WHITE ? whitePlayer : blackPlayer;
    }

    public Optional<Player> waitingPlayer() {
        if (whitePlayer.isEmpty() || blackPlayer.isEmpty()) {
            return empty();
        }
        return board.currentMove() == WHITE ? blackPlayer : whitePlayer;
    }
}
