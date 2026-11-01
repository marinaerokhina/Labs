package com.fifteenpuzzle.Application;

import com.fifteenpuzzle.Domain.Board;
import com.fifteenpuzzle.Domain.ValueObjects.Position;

public interface IGameService {
    void newGame(int n);
    Board getBoard();
    boolean tryMove(Position pos);
    void shuffleUntilSolvable();
    boolean isSolved();
}
