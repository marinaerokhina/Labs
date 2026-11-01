# Labs — лабораторные по архитектуре приложений (Java / JavaFX)

[English version](README.md)

Пять проектов пятого семестра: **четыре JavaFX-приложения и одна консольная библиотека** для клонирования объектов. Исходники распакованы по отдельным папкам, исходные ZIP-архивы и отчёты Word сохранены в корне репозитория.

| Работа | Основная идея | Подробное описание |
| --- | --- | --- |
| **1. Пятнашки** | Поле N×N, проверка разрешимости, слои, Value Object | [Русский](projects/01-fifteen-puzzle/README_RUS.md) · [English](projects/01-fifteen-puzzle/README.md) |
| **2. DeepCloner** | Reflection API, Strategy, Builder, Factory, аннотации | [Русский](projects/02-deep-cloner/README_RUS.md) · [English](projects/02-deep-cloner/README.md) |
| **3. Peak Indicator** | Observer, события, планировщик, JavaFX Canvas | [Русский](projects/03-peak-indicator/README_RUS.md) · [English](projects/03-peak-indicator/README.md) |
| **4. Pot on Stove** | Многопоточность, модель нагрева, события, график | [Русский](projects/04-pot-on-stove/README_RUS.md) · [English](projects/04-pot-on-stove/README.md) |
| **5. Graphics Editor (РГР)** | Command, Undo/Redo, геометрия фигур | [Русский](projects/05-graphics-editor/README_RUS.md) · [English](projects/05-graphics-editor/README.md) |

**Внутри каждого проекта** — подробности о возможностях, архитектуре, паттернах, результате, ограничениях и потенциальных ошибках, найденных при анализе исходников.

## Запуск

Нужны **JDK 21 и Maven**. Проекты в `projects/` независимы. Например, для «Пятнашек»:

```bash
cd projects/01-fifteen-puzzle
mvn javafx:run
```

DeepCloner запускается в консоли; команда указана в его README. Все пять проектов компилируются в [GitHub Actions](.github/workflows/maven-labs.yml), но интерфейсы JavaFX отдельно автоматически не проверяются.

**Дисциплина:** «Инжиниринг ПО», 5 семестр. **Автор:** Марина Ерохина.
