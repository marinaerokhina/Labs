# DeepCloner

An educational deep-copy framework for object graphs written in Java. Reflection, cloning strategies, custom annotations, and a builder-style API demonstrate extensible framework design.

## Design highlights

Strategy, Builder, Factory; reflection and annotations; array/collection/map handling; cycle-awareness.

## Build / run

Use **JDK 21** and **Maven 3.9+**. From this project directory:

```bash
mvn clean package
mvn javafx:run
```

JavaFX requires a desktop environment. The main application class declared in the POM is `com.deepcloner.App`, and the Maven artifact is `LAB2SEM5`.

## Source layout

- `src/main/java/` — implementation and separated logical layers
- `src/main/resources/` — FXML and other resources, where present
- `pom.xml` — independent Maven build

## Original coursework

The original ZIP and the Word report are kept at the [repository root](../../). This source directory is an extracted, browsable copy. No claim of production readiness or completed GUI end-to-end tests is made.
