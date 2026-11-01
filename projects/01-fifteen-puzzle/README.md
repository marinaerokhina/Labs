# Fifteen Puzzle — JavaFX sliding puzzle

[Русская версия](README_RUS.md) · [All coursework projects](../../)

A configurable N × N sliding-tile puzzle. The goal is to arrange numbered tiles in ascending order, leaving the bottom-right cell empty. The main point of the assignment was to keep game rules separate from the JavaFX interface.

## Features and results

- Create a board of size **3 × 3 or larger** (4 × 4 by default).
- Shuffle tiles into a configuration that passes the solvability check.
- Click a tile next to the empty cell to move it; invalid moves have no effect.
- Display the current board and a completion message when it is solved.
- Generate the grid dynamically with JavaFX `GridPane` rather than hardcoding 15 tiles.

## Architecture and techniques

- **Domain — `Board`:** stores the `int[][]` tiles; implements legal moves, solved-state detection and inversion-based solvability. For odd widths the inversion count must be even. For even widths the parity also depends on the blank's row counted from the bottom.
- **Value Object — `Position`:** an immutable Java `record` representing a row and column. `isAdjacent()` checks Manhattan distance = 1. This keeps the adjacency rule out of mouse-event handlers.
- **Application — `IGameService` / `GameService`:** the service creates games, attempts moves and repeats random shuffling until the board is solvable. The interface separates the UI from the implementation.
- **Presentation — `GameController` + FXML:** generates non-editable cells, handles clicks and redraws the board. The controller delegates game logic to the service.

**Design approach:** layered architecture, interface-based dependency, and an immutable value object. This is not an example of every GoF pattern; the emphasis is separation of concerns.

## Known limitations and things to improve

- `parseSize()` enforces a minimum of 3 but **no upper bound**. A very large value can create an impractically large JavaFX grid.
- Shuffling is random and repeated until solvable; it may occasionally produce an already solved board. It does not guarantee a target difficulty.
- Board state is mutable, and there are no automated tests for parity rules or move boundaries. The JavaFX screens were not manually validated as part of the portfolio refresh.
- No move counter, timer, persistence or keyboard control is implemented.

## Run

**JDK 21** and Maven, from this folder:

```bash
mvn javafx:run
```

A graphical desktop is required. The project successfully compiles in the repository's [Java build workflow](../../.github/workflows/maven-labs.yml). Original submission archive and report are at the [repository root](../../).
