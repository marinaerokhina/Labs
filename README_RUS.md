# Labs — Лабораторные работы по инжинирингу ПО

Репозиторий содержит серию проектов, выполненных в рамках курса "Инжиниринг ПО" (НГТУ, 5 семестр). Все проекты построены на принципах **слоистой архитектуры** (Domain → Application → Presentation → Infrastructure) с применением **паттернов проектирования** и акцентом на разделение ответственности.
Проекты оставлены в zip-архивах вследствие того, что запуск происходит через исполняемый файл. Информацию по конкретным проектам и листинг кода можно увидеть в отчетах Word.

## 📋 Список работ

| № | Проект | Тема | Ключевые паттерны и концепции |
|---|--------|------|-------------------------------|
| 1 | [Пятнашки](#1-пятнашки-fifteen-puzzle) | Игра с произвольной размерностью | Слоистая архитектура, Value Objects, алгоритм решаемости |
| 2 | [DeepCloner](#2-deepcloner) | Универсальный фреймворк глубокого клонирования | Strategy, Builder, Factory, Reflection API, аннотации |
| 3 | [Peak Indicator](#3-peak-indicator) | Пиковый индикатор уровня | Observer, Value Objects, ScheduledExecutorService |
| 4 | [Pot on Stove](#4-pot-on-stove) | Симулятор кастрюли на плите | Observer, многопоточность, физическая модель |
| 5 | [Graphics Editor](#5-graphics-editor) | Графический редактор (РГЗ) | Command, Undo/Redo, полиморфизм фигур |

---

## 1. Пятнашки (Fifteen Puzzle)

Игра "Пятнашки" с произвольной размерностью N×N и гарантированно решаемыми конфигурациями.

### Архитектура
```
Presentation (GameController + FXML)
      ↓
Application (GameService)
      ↓
Domain (Board, Position)
```

### Ключевые особенности
- **Value Object `Position`** с методом `isAdjacent()` (Манхэттен-соседство)
- **Алгоритм проверки решаемости** через подсчёт инверсий с учётом чётности N и позиции пустой ячейки
- **Динамическая разметка** GridPane с N×N ячейками
- Чистое разделение: UI не знает о правилах игры, домен не знает о JavaFX

### Демонстрируемые навыки
✅ Слоистая архитектура  
✅ Value Objects  
✅ Алгоритмы (подсчёт инверсий)  
✅ Динамический GUI  

---

## 2. DeepCloner

Универсальный фреймворк для глубокого клонирования произвольных Java-объектов через рефлексию.

### Архитектура
```
API (Cloner, ClonerBuilder)
      ↓
Application (ClonerService, ReflectionModel, StrategyRegistry)
      ↓
Domain (CloneStrategy, InstanceFactory, CloneContext, аннотации)
      ↓
Infrastructure (ArrayStrategy, CollectionStrategy, MapStrategy, DateTimeStrategy, IdentityStrategy)
```

### Ключевые особенности
- **Паттерн Strategy** — для каждого типа данных своя стратегия клонирования
- **Паттерн Builder** — гибкая настройка фреймворка через `ClonerBuilder`
- **Паттерн Factory** — абстракция создания экземпляров (`InstanceFactory`)
- **Защита от циклических ссылок** через `CloneContext` с кэшем уже клонированных объектов
- **Аннотации для управления поведением:**
  - `@CloneIgnore` — исключить поле из клонирования
  - `@ShallowClone` — поверхностное копирование
  - `@CloneWith` — принудительно задать стратегию
- Поддержка коллекций, массивов, Map, Date/Calendar, records

### Демонстрируемые навыки
✅ Reflection API  
✅ 3 паттерна проектирования  
✅ Работа с аннотациями  
✅ Обработка циклических графов  
✅ Расширяемая архитектура  

---

## 3. Peak Indicator

Вертикальный пиковый индикатор уровня с инертным индикатором максимума (красная линия плавно спадает).

### Архитектура
```
Presentation (PeakIndicatorPane, MainController)
      ↓
Application (IndicatorService, DataSource, LevelListener, ThresholdListener)
      ↓
Domain (LevelValue, Thresholds, IndicatorSnapshot, LevelUpdated, MaxThresholdExceeded)
      ↓
Infrastructure (PeriodicTimer, SineNoiseSource)
```

### Ключевые особенности
- **Value Objects** `LevelValue` (с само-валидацией 0..1) и `Thresholds`
- **Событийная модель** — доменные события `LevelUpdated` и `MaxThresholdExceeded`
- **Инерция пика** — красная линия линейно спадает со скоростью `peakDecayPerSecond`
- **ScheduledExecutorService** для периодических тиков без привязки к UI
- Отрисовка через `Canvas` с автоматическим ресайзом

### Демонстрируемые навыки
✅ Domain Events  
✅ Value Objects с валидацией  
✅ ScheduledExecutorService  
✅ JavaFX Canvas  
✅ Слабая связность слоёв  

---

## 4. Pot on Stove

Симулятор кастрюли на плите с физической моделью термодинамики, работающий в отдельном потоке.

### Архитектура
```
Presentation (PotControlPane, MainController, App)
      ↓
Application (PotOnStoveController — Runnable)
      ↓
Domain (PotState, PotCommands, PotStateListener, BoilingStarted/StoppedEvent)
```

### Ключевые особенности
- **Физическая модель:** нагрев, теплопотери (зависят от крышки), кипение при 100°C, испарение
- **Многопоточность:** симуляция в отдельном потоке, GUI в JavaFX Application Thread
- **Паттерн Observer** — `PotStateListener` с методами `onStateUpdate()` и `onEvent()`
- **Потокобезопасность:** `CopyOnWriteArrayList` для подписчиков, `synchronized` для методов изменения состояния
- **Platform.runLater()** для безопасного обновления GUI из фонового потока
- **Корректный жизненный цикл** — остановка потока при закрытии приложения

### Демонстрируемые навыки
✅ Многопоточность  
✅ Потокобезопасность  
✅ Observer pattern  
✅ Физическое моделирование  
✅ Корректная работа с JavaFX в многопоточной среде  

---

## 5. Graphics Editor (РГЗ)

Полноценный графический редактор с поддержкой трёх типов фигур и механизмом Undo/Redo.

### Архитектура
```
Presentation (MainController, DrawingCanvas)
      ↓
Command (CommandManager + 6 конкретных команд)
      ↓
Domain (Shape, Rectangle, Circle, Triangle, ShapeManager)
```

### Поддерживаемые фигуры
- **Прямоугольник** — изменение размера через 4 угловых маркера
- **Круг** — сохраняет пропорции, изменение размера от центра
- **Треугольник** — равнобедренный, изменение размера через 3 вершины (барицентрические координаты для hit-testing)

### Команды
- `CreateShapeCommand` — создание фигуры выбранного типа
- `MoveShapeCommand` — перемещение
- `ResizeShapeCommand` — изменение размера
- `DeleteShapeCommand` — удаление с возможностью восстановления
- `BringToFrontCommand` / `SendToBackCommand` — изменение z-order

### Ключевые особенности
- **Паттерн Command** — каждая операция инкапсулирована в отдельном классе
- **Undo/Redo** через два стека (`ArrayDeque`) с ограничением истории (50 команд)
- **Полиморфизм фигур** — абстрактный `Shape` с методами `draw()`, `contains()`, `isInResizeArea()`, `copy()`
- **Глубокое копирование** для корректной работы Undo
- **Горячие клавиши:** Ctrl+Z, Ctrl+Y, Delete
- **Специальная логика** для каждой фигуры при resize

### Демонстрируемые навыки
✅ Command pattern  
✅ Undo/Redo механизм  
✅ Полиморфизм и наследование  
✅ Геометрические алгоритмы (hit-testing, барицентрические координаты)  
✅ JavaFX Canvas  
✅ Обработка событий мыши и клавиатуры  

---

## 🛠 Общие технологии

- **Java 17+** (включая records, pattern matching)
- **JavaFX** — GUI
- **FXML** — разметка интерфейса
- **Maven** — сборка
- **Reflection API** — для DeepCloner
- **java.util.concurrent** — для многопоточности

## 🎯 Сквозные архитектурные принципы

Во всех проектах прослеживаются:

1. **Слоистая архитектура** — чёткое разделение Domain / Application / Presentation / Infrastructure
2. **Зависимости сверху вниз** — Domain не зависит ни от чего, Presentation зависит от Application
3. **Value Objects** — неизменяемые объекты с само-валидацией
4. **Слабая связность** — слои общаются через интерфейсы
5. **Паттерны проектирования** — каждый проект демонстрирует 1-3 паттерна
6. **Чистый домен** — бизнес-логика не зависит от UI и фреймворков

## 📚 Чему научилась

✅ Слоистая и чистая архитектура  
✅ 6+ паттернов проектирования (Command, Observer, Strategy, Builder, Factory, Value Object)  
✅ Многопоточное программирование  
✅ JavaFX (GUI, Canvas, FXML, Platform.runLater)  
✅ Reflection API и аннотации  
✅ Алгоритмы (инверсии, барицентрические координаты)  
✅ Физическое моделирование  
✅ Доменно-ориентированное проектирование (DDD-подход)  

---

**Автор:** Марина Ерохина  
**Курс:** 5 семестр, дисциплина "Инжиниринг ПО"  
