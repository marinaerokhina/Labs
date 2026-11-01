# Graphics Editor

A JavaFX drawing application supporting geometric shapes and reversible editing commands. The design uses a command history for undo/redo and separate domain/presentation components.

## Design highlights

Command pattern; undo/redo; shape polymorphism; mouse interactions and geometric hit-testing.

## Build / run

Use **JDK 21** and **Maven 3.9+**. From this project directory:

```bash
mvn clean package
mvn javafx:run
```

JavaFX requires a desktop environment. The main application class declared in the POM is `com.rgz_e.presentation.App`, and the Maven artifact is `RGRSEM5MARINA`.

## Source layout

- `src/main/java/` — implementation and separated logical layers
- `src/main/resources/` — FXML and other resources, where present
- `pom.xml` — independent Maven build

## Original coursework

The original ZIP and the Word report are kept at the [repository root](../../). This source directory is an extracted, browsable copy. No claim of production readiness or completed GUI end-to-end tests is made.
