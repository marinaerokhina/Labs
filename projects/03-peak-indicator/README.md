# Peak Indicator — event-driven JavaFX gauge

[Русская версия](README_RUS.md) · [All coursework projects](../../)

A live level gauge with a moving blue level bar, an independent red peak marker and configurable thresholds. Values come from a generated sine-wave signal with noise. The exercise focuses on event delivery and keeping signal processing separate from presentation.

## Features

- Adjustable input range, minimum/maximum thresholds, peak decay rate, history capacity and update period.
- A `RealSineSource` generates sample values, which are normalized to **0…1** for internal calculations.
- The current level is sampled periodically; the peak holds its recent maximum and then decays over elapsed time.
- Exceeding the upper threshold raises an event; separate events indicate when the **peak** crosses above or below the threshold.
- JavaFX `Canvas` draws the moving bar, horizontal threshold lines and peak marker.

## Architecture and patterns

- **Observer / Listener:** `IndicatorService` publishes `LevelUpdated`, `MaxThresholdExceeded` and peak-crossing events through `LevelListener` and `ThresholdListener`. The UI registers callbacks instead of embedding drawing logic in the service.
- **Value Objects:** `LevelValue` validates numeric input and clamps it to 0…1. `Thresholds` rejects an inverted min/max pair; `IndicatorSnapshot` packages a point-in-time reading.
- **Dependency inversion:** `DataSource.next()` lets `IndicatorService` consume a signal without caring how it is generated. `RealSineSource` and `SineNoiseSource` are interchangeable implementations.
- **Scheduling:** `PeriodicTimer` uses a single-thread `ScheduledExecutorService`. UI updates are moved to the JavaFX thread using `Platform.runLater`.
- **Layering:** the service performs peak/threshold calculations; the presentation draws the result.

## Known limitations and possible issues

- `setLevelListener` and `setThresholdListener` store one listener each, **not a subscriber list**. Registering another replaces the previous callback.
- While the **current** level is above the maximum, `onExceeded` fires on *every tick* rather than only on the first crossing. It may repeatedly update the warning label.
- The sine source uses an unseeded `Random`; visual runs are not deterministic. The history list has bounded capacity but is not displayed as a separate graph.
- Update period validation is limited. A non-positive period is rejected by the scheduler at runtime rather than by a dedicated form validation message.
- Maven compilation passed, but GUI interactions and threshold edge cases have no automated tests.

## Run

**JDK 21**, Maven and a graphical desktop:

```bash
mvn javafx:run
```

Build status is verified in the [repository workflow](../../.github/workflows/maven-labs.yml). Original ZIP/report are in the [repository root](../../).
