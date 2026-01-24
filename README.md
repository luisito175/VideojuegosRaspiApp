# Proyecto Concesionario de Motos (RecyclerViewMotos)

Este es un proyecto de Android que demuestra el uso de `RecyclerView` para mostrar y gestionar un listado de motocicletas. La aplicación permite a los usuarios ver, añadir, editar y eliminar elementos de la lista de forma interactiva.

## Características Principales

- **Visualización de Datos**: Muestra una lista de motocicletas utilizando `RecyclerView`, con cada elemento presentado en un `CardView` para un diseño limpio y moderno.
- **Gestión de Datos (CRUD)**:
  - **Crear**: Añadir nuevas motos a la lista a través de un formulario en un diálogo.
  - **Leer**: Ver los detalles de cada moto en la lista principal.
  - **Actualizar**: Modificar la información de una moto existente.
  - **Eliminar**: Borrar una moto de la lista con un solo clic.
- **Selección de Imágenes**: Permite al usuario seleccionar una imagen desde la galería del dispositivo para asociarla a una moto. La imagen se previsualiza en el formulario y se muestra en la lista principal.
- **Interfaz de Usuario Moderna**: Utiliza `DialogFragment` para una experiencia de edición y adición fluida sin cambiar de pantalla.
- **Arquitectura Simple**: Emplea un patrón `Controller` para separar la lógica de negocio de la interfaz de usuario, facilitando el mantenimiento del código.

## Demostraciones en Vídeo
- **Versión 1.3**: [Ver demostración en YouTube](https://youtu.be/ug9qEMpEm7k)
- **Versión 1.2**: [Ver demostración en YouTube](https://youtu.be/ULpFjaF5WlY)
- **Versión 1.1**: [Ver demostración en YouTube](https://youtu.be/6ClddBkvGy8)

## Componentes Clave

- **`MainActivity.kt`**: Actividad principal que contiene el `RecyclerView` y coordina las interacciones principales.
- **`AdapterMoto.kt`**: Adaptador del `RecyclerView`. Se encarga de vincular la lista de motos con las vistas (`CardView`) y de gestionar los eventos de clic (eliminar, editar).
- **`ViewHMoto.kt`**: `ViewHolder` para el adaptador, que mantiene las referencias a las vistas de cada elemento de la lista.
- **`MotoDialogFragment.kt`**: `DialogFragment` que muestra un formulario para añadir o editar los detalles de una moto, incluyendo la selección de imágenes.
- **`Controller.kt`**: Clase que actúa como intermediario entre la `MainActivity` y la capa de datos, manejando la lógica para añadir, borrar y actualizar motos.
- **`Dao/DaoMoto.kt`**: Objeto singleton que simula una fuente de datos (repositorio), proporcionando la lista inicial de motos.
- **`models/Moto.kt`**: `data class` de Kotlin que define la estructura del objeto `Moto`.

## Tecnologías y Librerías

- **Lenguaje**: Kotlin
- **UI**: 
  - `ViewBinding`: Para acceder a las vistas de forma segura y concisa.
  - `RecyclerView`: Para mostrar listas de datos de manera eficiente.
  - `CardView`: Para el diseño de cada ítem de la lista.
  - `Material Components`: Para los widgets de la interfaz de usuario como botones y campos de texto.
- **Gestión de Imágenes**:
  - `ActivityResultContracts`: API moderna para obtener resultados de actividades, como la selección de imágenes.
  - `Glide`: Librería para cargar y mostrar imágenes de forma eficiente y fluida.

## Cómo Ejecutar el Proyecto

1.  **Clonar el repositorio**: `git clone <URL_DEL_REPOSITORIO>`
2.  **Abrir en Android Studio**: Importa el proyecto en Android Studio.
3.  **Sincronizar Gradle**: Espera a que Android Studio descargue todas las dependencias necesarias.
4.  **Ejecutar**: Inicia la aplicación en un emulador o en un dispositivo físico con Android.
