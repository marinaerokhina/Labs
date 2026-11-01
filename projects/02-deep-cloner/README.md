# DeepCloner — reflection-based object copying

[Русская версия](README_RUS.md) · [All coursework projects](../../)

A small Java deep-copy library built to explore reflection, annotations and extensible object creation. Its console demo clones a pair of `Person` objects with nested addresses and a circular friend relationship.

## Features and observed design

- Generic API: `Cloner.deepClone(obj)` or a configured instance from `ClonerBuilder`.
- Reflection discovers fields, including fields with custom annotations.
- Strategies handle arrays, collections, maps, dates and known immutable types.
- Object-to-object cycles are tracked with `IdentityHashMap` for objects processed by the reflection fallback.
- A console demo displays original and cloned objects and checks selected reference relationships.

## Patterns: where and why

- **Strategy:** `CloneStrategy` defines `supports(type)` and `cloneValue(source, context)`; `StrategyRegistry` picks the first matching handler. `ArrayStrategy`, `MapStrategy`, `CollectionStrategy`, `DateTimeStrategy` and `IdentityStrategy` encapsulate type-specific logic.
- **Builder:** `ClonerBuilder.addStrategy(...)` and `instanceFactory(...)` configure the cloning service without changing its public API.
- **Factory abstraction:** `InstanceFactory` / `DefaultInstanceFactory` separate object construction from traversal. Standard classes generally need an accessible no-argument constructor.
- **Context / identity map:** `ClonerService` caches source-to-clone pairs *before* traversing ordinary object fields, supporting references back to previously visited objects.
- **Annotation-driven customization:** `@CloneIgnore` skips a field, `@ShallowClone` reuses a reference, and `@CloneWith` chooses a field-specific strategy.

## Known limitations and code-level issues

- **Cyclic arrays or collections are not generally protected.** Strategy dispatch happens before the ordinary-object cache insertion, and collection strategies recursively clone elements. Self-referential collections can recurse indefinitely. The circular `Person.friend` relationship works by a different path.
- **`record` instantiation is incomplete.** `DefaultInstanceFactory` explicitly throws for records with components unless a custom factory is supplied.
- **`@CloneIgnore` does not necessarily make a field `null`.** The destination is created using its constructor, then ignored fields are left at their constructor/default initialization values; for example the demo `Person.cached` field has an initializer.
- A strategy may fall back to a different collection implementation, so exact concrete collection types are not guaranteed.
- There is no automated unit-test suite. These are observations from the source, not a claim that every edge case has been reproduced.

## Run

**JDK 21**, Maven, from this folder:

```bash
mvn package
java -cp target/classes com.deepcloner.App
```

This example runs in the **terminal**, not as a JavaFX screen. Maven compilation is checked by the repository's [build workflow](../../.github/workflows/maven-labs.yml). Original ZIP and report are at the [repository root](../../).
