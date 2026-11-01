# Software Engineering Labs — Java & JavaFX

**Five independent Maven/JavaFX coursework projects** from a software engineering course (semester 5). They explore layered architecture, design patterns, Java concurrency, graphics and event-driven application design.

Each project has been **unpacked from the original ZIP** so its source code can be browsed directly. The original archives and Word reports are retained at the repository root for reference.

> Educational portfolio, not production software. GUI execution has not been manually verified on every operating system.

## Projects

| Project | Focus | Source |
| --- | --- | --- |
| [01 — Fifteen Puzzle](projects/01-fifteen-puzzle/) | N×N puzzle, value objects, layered UI/domain logic | [Java sources](projects/01-fifteen-puzzle/src/main/java/) |
| [02 — DeepCloner](projects/02-deep-cloner/) | Reflection, cloning, Strategy/Builder/Factory | [Java sources](projects/02-deep-cloner/src/main/java/) |
| [03 — Peak Indicator](projects/03-peak-indicator/) | Observer, domain events, timed signal updates | [Java sources](projects/03-peak-indicator/src/main/java/) |
| [04 — Pot on Stove](projects/04-pot-on-stove/) | Multithreading, heating model, JavaFX chart | [Java sources](projects/04-pot-on-stove/src/main/java/) |
| [05 — Graphics Editor](projects/05-graphics-editor/) | Command pattern, undo/redo and geometric tools | [Java sources](projects/05-graphics-editor/src/main/java/) |

## Run a project

Requires **JDK 21** and **Maven**. Each lab is an independent project. For example, from the repository root:

```bash
cd projects/01-fifteen-puzzle
mvn javafx:run
```

For another lab, use its corresponding folder under `projects/`. JavaFX requires a graphical desktop. All five projects are compiled in GitHub Actions.

## Original submissions and documentation

The original `LAB1SEM5.zip`–`LAB4SEM5.zip` and `RGRSEM5_Erokhina.zip` are kept unchanged. The associated `.docx` reports are also retained. The original in-depth Russian project descriptions are available in [README_RUS.md](README_RUS.md).

## What this portfolio demonstrates

- Separating domain/application logic from GUI code
- Object-oriented modeling and design patterns (Observer, Strategy, Command, Builder and Factory)
- Java reflection and annotation processing
- Concurrency and UI-thread coordination
- JavaFX controls, FXML and drawing with Canvas

**Author:** Marina Erokhina · University coursework, semester 5.
