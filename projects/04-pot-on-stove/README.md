# Pot on Stove

A multi-threaded JavaFX simulation of a pot heating on a stove. The temperature model includes heat exchange, boiling and related domain events.

## Design highlights

Threading and UI synchronization; Observer; simulation state and command interfaces; charts.

## Build / run

Use **JDK 21** and **Maven 3.9+**. From this project directory:

```bash
mvn clean package
mvn javafx:run
```

JavaFX requires a desktop environment. The main application class declared in the POM is `com.lab4.presentation.App`, and the Maven artifact is `LAB4SEM5`.

## Source layout

- `src/main/java/` — implementation and separated logical layers
- `src/main/resources/` — FXML and other resources, where present
- `pom.xml` — independent Maven build

## Original coursework

The original ZIP and the Word report are kept at the [repository root](../../). This source directory is an extracted, browsable copy. No claim of production readiness or completed GUI end-to-end tests is made.
