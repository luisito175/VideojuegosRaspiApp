# Catálogo de Videojuegos (RecyclerView)

Proyecto Android que usa `RecyclerView` para mostrar y gestionar un catálogo de videojuegos. Permite crear, editar, eliminar y ver el detalle de cada videojuego, cargando los datos iniciales desde un JSON local.

## Características principales

- **Catálogo de videojuegos** con `RecyclerView` y tarjetas (`CardView`).
- **CRUD completo** mediante un `DialogFragment` para crear/editar.
- **Detalle** en un fragmento dedicado.
- **Datos iniciales desde JSON** ubicado en `app/src/main/assets/videojuegos.json`.
- **Arquitectura MVVM + Clean Architecture** con Hilt para inyección de dependencias.

## Estructura de datos

La app trabaja con esta estructura:

```kotlin
data class VideoGame(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val plataforma: String,
    val caracteristicas: String
)
```

## Archivo JSON

Ejemplo de entrada (ver `app/src/main/assets/videojuegos.json`):

```json
{
  "id": 1,
  "nombre": "The Legend of Zelda: Breath of the Wild",
  "precio": 59.99,
  "plataforma": "Nintendo Switch",
  "caracteristicas": "Mundo abierto, exploración libre y puzles basados en física."
}
```

## Componentes clave

- **`AdapterMoto.kt` / `ViewHMoto.kt`**: Adaptador y ViewHolder del catálogo.
- **`MotoDialogFragment.kt`**: Formulario para crear/editar videojuegos.
- **`MotosViewModel.kt`**: Lógica de presentación y estado.
- **`ConcesionarioApp.kt`**: Carga el JSON inicial al iniciar la app.

## Tecnologías

- Kotlin
- MVVM + Clean Architecture
- Hilt
- RecyclerView + Material Components
- Gson (parseo JSON)

## Cómo ejecutar

1. Abrir el proyecto en Android Studio.
2. Sincronizar Gradle.
3. Ejecutar en emulador o dispositivo físico.
