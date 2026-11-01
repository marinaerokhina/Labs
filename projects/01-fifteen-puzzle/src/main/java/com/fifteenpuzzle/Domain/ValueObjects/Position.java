package com.fifteenpuzzle.Domain.ValueObjects;

public record Position(int row, int col) {
    public boolean isAdjacent(Position other) {
        int dr = Math.abs(row - other.row);
        int dc = Math.abs(col - other.col);
        return (dr + dc) == 1; // Манхэттен соседство
    }
}
