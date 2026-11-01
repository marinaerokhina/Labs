# Peak Indicator

A JavaFX peak level indicator receiving a generated signal and updating a visual level display. The implementation separates value objects, domain events, listeners and timed data sources.

## Design highlights

Observer; domain events; Value Objects; scheduled updates; JavaFX Canvas.

## Build / run

Use **JDK 21** and **Maven 3.9+**. From this project directory:

```bash
mvn clean package
mvn javafx:run
```

JavaFX requires a desktop environment. The main application class declared in the POM is `com.indicator.App`, and the Maven artifact is `LAB3SEM5`.

## Source layout

- `src/main/java/` — implementation and separated logical layers
- `src/main/resources/` — FXML and other resources, where present
- `pom.xml` — independent Maven build

## Original coursework

The original ZIP and the Word report are kept at the [repository root](../../). This source directory is an extracted, browsable copy. No claim of production readiness or completed GUI end-to-end tests is made.
