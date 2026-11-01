# Software Engineering Labs — Java, JavaFX and Design Patterns

[Русская версия](README_RUS.md)

Five coursework projects from semester 5: **four JavaFX applications and a console-based cloning library**. The source code is unpacked and browsable; the original ZIP archives and Word reports are preserved at the repository root.

| Project | What it demonstrates | Detailed documentation |
| --- | --- | --- |
| **01. Fifteen Puzzle** | N×N board, solvability checks, layered architecture and Value Object | [English](projects/01-fifteen-puzzle/README.md) · [Русский](projects/01-fifteen-puzzle/README_RUS.md) |
| **02. DeepCloner** | Reflection, Strategy, Builder, Factory, custom annotations and object graphs | [English](projects/02-deep-cloner/README.md) · [Русский](projects/02-deep-cloner/README_RUS.md) |
| **03. Peak Indicator** | Observer, events, scheduled signal processing and JavaFX Canvas | [English](projects/03-peak-indicator/README.md) · [Русский](projects/03-peak-indicator/README_RUS.md) |
| **04. Pot on Stove** | Threaded simulation, state snapshots, Observer and temperature chart | [English](projects/04-pot-on-stove/README.md) · [Русский](projects/04-pot-on-stove/README_RUS.md) |
| **05. Graphics Editor** | Command, Undo/Redo, shape polymorphism and geometry | [English](projects/05-graphics-editor/README.md) · [Русский](projects/05-graphics-editor/README_RUS.md) |

Each project's README describes **features, architecture, where patterns are used, the outcome, known code-level limitations and how to run it**.

## Build and run

**JDK 21 + Maven.** The five folders under `projects/` are independent Maven projects. For a JavaFX example:

```bash
cd projects/01-fifteen-puzzle
mvn javafx:run
```

The DeepCloner example runs in the console; see its README. The [GitHub Actions workflow](.github/workflows/maven-labs.yml) compiles/packages all five projects. This does not replace manual GUI testing.

**Coursework:** Software Engineering, semester 5. **Author:** Marina Erokhina.
