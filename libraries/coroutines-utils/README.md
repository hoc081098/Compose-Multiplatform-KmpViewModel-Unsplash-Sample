# Coroutines Utils Module

## Dependencies

- Pure Kotlin.
- No Compose Multiplatform plugin and dependencies.
- Depends on:
  - `kotlinx-coroutines-core`.
  - `FlowExt`.

## Content

- `Flow<T>.publish(selector: suspend SelectorScope<T>.() -> Flow<R>)`.
