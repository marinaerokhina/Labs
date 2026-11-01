# Pot on Stove — multithreaded temperature simulation

[Русская версия](README_RUS.md) · [All coursework projects](../../)

An interactive JavaFX model of water heating in a pot. The exercise combines a simplified physical process, a background simulation thread, event notifications and a live temperature chart.

## Features and result

- Choose one of five burner levels (**0–4**, mapped to 0…1), add water with a specified temperature, and put the lid on or remove it.
- Shows water temperature, remaining volume, lid state and boiling status.
- Estimates heating from burner power, heat loss to the environment, and heat capacity of the water.
- Caps modeled boiling temperature at **100 °C**; above the threshold, water can evaporate, with slower evaporation when the lid is on.
- Adds water using a mass-weighted temperature calculation and enforces a **10 L** maximum volume.
- Displays an optional live temperature chart in a separate window.

## Architecture and patterns

- **Observer:** `PotStateListener` receives both `onStateUpdate(PotState)` and `onEvent(PotEvent)`. The main UI and chart window subscribe independently; closing the chart removes its subscription.
- **Value Object / snapshot:** immutable `PotState` is a Java record containing temperature, volume, lid, power and boiling flag.
- **Domain events:** `BoilingStartedEvent` and `BoilingStoppedEvent` describe state transitions without tightly coupling the model to JavaFX.
- **Command interface:** `PotCommands` defines user operations `setBurnerLevel`, `toggleLid`, and `addWater`. This is an interface for actions, **not** a complete GoF Command pattern with undo/redo.
- **Concurrency:** `PotOnStoveController` implements `Runnable`, updates the model on a background thread approximately every 100 ms, synchronizes state changes and uses `CopyOnWriteArrayList` for listeners. `Platform.runLater` dispatches UI updates on the JavaFX thread.

## Known limitations and code-level issues

- This is a **simplified educational model**, not a validated thermodynamics simulator. Heat-loss and evaporation constants are illustrative.
- `TemperatureChart` appends every point indefinitely. Long-running sessions may use increasingly large amounts of memory and slow the chart.
- The simulation uses wall-clock `System.currentTimeMillis()` for the timestep; large scheduling delays can make updates coarse. UI controls are not supported by automated end-to-end tests.
- Some unused/parallel UI code exists (`PotControlPane` alongside the FXML controller); it could be consolidated.
- The existing lifecycle requests shutdown and joins the simulation thread; threading stress tests have not been performed.

## Run

**JDK 21**, Maven and a graphical desktop:

```bash
mvn javafx:run
```

The [GitHub workflow](../../.github/workflows/maven-labs.yml) checks compilation of the coursework project. Original ZIP and Word reports remain at the [repository root](../../).
