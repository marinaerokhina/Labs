# Fifteen Puzzle

An N×N sliding-tile puzzle developed in JavaFX. The domain model handles tile positions, valid moves, and solvable board generation. Presentation and application logic are kept separate.

## Design highlights

Layered architecture; immutable Position value object; board state; FXML/JavaFX user interface.

## Build / run

Use **JDK 21** and **Maven 3.9+**. From this project directory:

```bash
mvn clean package
mvn javafx:run
```

JavaFX requires a desktop environment. The main application class declared in the POM is `com.fifteenpuzzle.App`, and the Maven artifact is `LAB1SEM5`.

## Source layout

- `src/main/java/` — implementation and separated logical layers
- `src/main/resources/` — FXML and other resources, where present
- `pom.xml` — independent Maven build

## Original coursework

The original ZIP and the Word report are kept at the [repository root](../../). This source directory is an extracted, browsable copy. No claim of production readiness or completed GUI end-to-end tests is made.
