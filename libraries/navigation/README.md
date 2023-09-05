# Koin Compose Utils Module

## Dependencies

- Compose Multiplatform plugin.
- Android library plugin.
- Kotlin Parcelize plugin.
- Common main depends on
  - `compose-runtime`.
  - `compose-runtime-saveable`.
  - `kmp-viewmodel`.
  - `kmp-viewmodel-savedstate`.
  - `kmp-viewmodel-compose`.
- Android main depends on
  - `khonshu-navigation-compose`.
- Non android main depends on
  - `uuid`.
  - `kotlinx-collections-immutable`.

## Content

### Common main

- `EXTRA_ROUTE`.
- `BaseRoute`, `NavRoute`, `NavRoot`.
- `rememberCloseableForRoute`.
- `SavedStateHandle.requireRoute()`.
- `SavedStateHandle.putArguments(BaseRoute)`.

### NonAndroid main

- `NavHost` and `NavigationSetup`.
