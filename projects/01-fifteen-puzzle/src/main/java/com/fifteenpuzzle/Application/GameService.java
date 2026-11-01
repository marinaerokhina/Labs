package com.fifteenpuzzle.Application;

import java.util.*;

import com.fifteenpuzzle.Domain.Board;
import com.fifteenpuzzle.Domain.ValueObjects.Position;

public class GameService implements IGameService {
    private Board board;
    private final Random rnd = new Random();

    public void newGame(int n) {
        this.board = Board.initial(n);
    }

    public Board getBoard() { return board; }

    public boolean tryMove(Position pos) {
        if (board.slideIfAdjacentToZero(pos)) {
            return true;
        }
        return false;
    }

    public void shuffleUntilSolvable() {
        do 
        { 
            randomShuffle();
        } while (!board.isSolvable());
    }

    public boolean isSolved() { return board.isSolved(); }

    private void randomShuffle() {
        int n = board.getSize();

        List<Integer> vals = new ArrayList<>(n*n);

        for (int r=0;r<n;r++) {
            for (int c=0;c<n;c++) {
                vals.add(board.getValue(r,c));
            }
        }

        Collections.shuffle(vals, rnd);

        int i=0;

        for (int r=0;r<n;r++) {
            for (int c=0;c<n;c++) {
                board.setValue(r,c, vals.get(i++));
            }
        }
    }
}
