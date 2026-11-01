# Graphics Editor — JavaFX shapes and undo/redo

[Русская версия](README_RUS.md) · [All coursework projects](../../)

A small JavaFX drawing editor created as the semester project. Users create geometric shapes on a canvas, select them, drag or resize them, delete them and change their stacking order. The central design exercise is making those operations reversible.

## Features

- Create a rectangle, circle or isosceles triangle; choose the type in the UI.
- Double-click to create a shape at the pointer; the Create button places a shape at a random location.
- Select and move shapes; use resize handles with shape-specific behavior (including a center-preserving circle resize).
- Delete, bring to front, send to back, and use **Undo/Redo**.
- Keyboard shortcuts: **Ctrl+Z**, **Ctrl+Y**, **Delete**; a 20-pixel background grid is drawn on the Canvas.
- History stores at most **50 executed commands** by default.

## Architecture and patterns

- **Command:** `Command` defines `execute()`, `undo()`, and `getDescription()`. Concrete classes encapsulate shape creation, movement, resizing, deletion and z-order changes.
- **Undo/Redo with two stacks:** `CommandManager` maintains `ArrayDeque` stacks. Executing a new action clears the redo stack; undo transfers a command in the opposite direction; redo executes it again.
- **Polymorphism:** the abstract `Shape` class defines drawing, hit-testing, resize handles and copying. `Rectangle`, `Circle` and `Triangle` implement their own geometry.
- **Geometry:** the triangle uses **barycentric coordinates** to check whether a point lies inside; the canvas searches shapes back-to-front so the visually topmost shape is selected.
- **Model/view separation:** `ShapeManager` owns the list and selection state. `MainController` interprets mouse/keyboard events and issues commands; the view uses JavaFX `Canvas`/FXML.

**Implementation detail:** drag events update the shape live, and a single movement or resize command is recorded at mouse release. Undo can therefore restore the starting geometry instead of recording every mouse pixel.

## Known limitations and code-level issues

- `MoveShapeCommand.getDescription()` and `DeleteShapeCommand.getDescription()` always say **"Rectangle"**, including for circles and triangles. The command behavior is generic; the human-readable description is inaccurate.
- No project save/load, export to image, or automated GUI test is implemented. New-document action clears the in-memory canvas and history.
- Shape creation uses random dimensions/colors for the demonstration. Some resize-boundary behaviors are complicated and have not been manually regression-tested across all three shape types.
- A successful Maven package is **not** proof of correct visual interaction.

## Run

**JDK 21**, Maven and a graphical desktop:

```bash
mvn javafx:run
```

Java compilation is checked by the [repository workflow](../../.github/workflows/maven-labs.yml). The original semester-project ZIP and report are retained at the [repository root](../../).
