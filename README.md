# Catálogo de Videojuegos (RecyclerView)

Proyecto Android que usa `RecyclerView` para mostrar y gestionar un catálogo de videojuegos. Permite crear, editar, eliminar y ver el detalle de cada videojuego, cargando los datos iniciales desde un JSON local.

## Características principales

- **Catálogo de videojuegos** con `RecyclerView` y tarjetas (`CardView`).
- **CRUD completo** mediante un `DialogFragment` para crear/editar.
- **Detalle** en un fragmento dedicado.
- **Consumo de API** con Retrofit (JSON Server para pruebas).
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

## API remota

La app consume una API remota. La base actual es:

```
https://untrigonometric-postmaximal-candice.ngrok-free.dev/
```

El recurso de la API es `videogame`, con los endpoints:

```
GET    /videogame
POST   /videogame
PUT    /videogame/{id}
DELETE /videogame/{id}
```


Ejemplo de registro en el `db.json` del JSON Server:

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

- **`VideoGameAdapter` / `VideoGameViewHolder`**: Adaptador y ViewHolder del catálogo.
- **`VideoGameDialogFragment`**: Formulario para crear/editar videojuegos.
- **`VideoGamesViewModel`**: Lógica de presentación y estado.
- **`ConcesionarioApp.kt`**: Carga el JSON inicial al iniciar la app.

## Tecnologías

- Kotlin
- MVVM + Clean Architecture
- Hilt
- RecyclerView + Material Components
- Gson (parseo JSON)

## Cómo ejecutar

1. Ejecuta JSON Server en `http://localhost:3000`.
2. Abre el proyecto en Android Studio.
3. Sincroniza Gradle.
4. Ejecuta en emulador o dispositivo.

> Nota: por defecto el proyecto apunta al host ngrok indicado arriba.
