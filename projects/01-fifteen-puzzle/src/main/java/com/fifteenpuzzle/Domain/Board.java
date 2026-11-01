package com.fifteenpuzzle.Domain;

import com.fifteenpuzzle.Domain.ValueObjects.Position;

public class Board {
    private final int size;
    private final int[][] tiles;

    private Board(int size) {
        this.size = size;
        this.tiles = new int[size][size];
    }

    public static Board initial(int n) {
        Board b = new Board(n);

        int v = 1;

        for (int r=0; r<n; r++) {
            for (int c=0; c<n; c++) {
                int value = (r == n-1 && c == n-1) ? 0 : v++;

                b.tiles[r][c] = value;
            }
        }

        return b;
    }

    public int getSize() { return size; }
    public int getValue(int r, int c) { return tiles[r][c]; }
    public void setValue(int r, int c, int v) { tiles[r][c] = v; }

    public Position findZero() {
        for (int r=0; r<size; r++) {
            for (int c=0; c<size; c++) {
                if (getValue(r,c) == 0) { 
                    return new Position(r,c);
                }
            }
        }

        throw new IllegalStateException("No zero");
    }

    public boolean slideIfAdjacentToZero(Position from) {
        Position zero = findZero();

        if (!from.isAdjacent(zero)) {
            return false;
        }

        int val = getValue(from.row(), from.col());
        setValue(zero.row(), zero.col(), val);
        setValue(from.row(), from.col(), 0);

        return true;
    }

    public boolean isSolved() {
        int expected = 1;

        for (int r=0; r<size; r++) {
            for (int c=0; c<size; c++) {
                if (r == size-1 && c == size-1) {
                    return getValue(r,c) == 0;
                }

                if (getValue(r,c) != expected++) {
                    return false;
                }
            }
        }

        return true; // на всякий случай, но логика выше уже вернула бы раньше
    }

    // Проверка разрешимости конфигурации
    public boolean isSolvable() {
        int[] arr = new int[size*size];
        int k=0;

        for (int r=0;r<size;r++) {
            for (int c=0;c<size;c++) {
                arr[k++] = getValue(r,c);
            }
        }

        int inversions = 0;

        for (int i=0;i<arr.length;i++) {
            for (int j=i+1;j<arr.length;j++) {  
                if (arr[i] != 0 && arr[j] != 0 && arr[i] > arr[j]) {
                    inversions++;
                }
            }
        }

        if (size % 2 == 1) {
            return inversions % 2 == 0; // нечётная ширина
        } else {
            int zeroRowFromBottom = size - findZero().row();
            // для чётной ширины (классическое правило 15-пазла)
            if (zeroRowFromBottom % 2 == 0) {
                return inversions % 2 == 1;
            } else {    
                return inversions % 2 == 0;
            }
        }
    }
}
