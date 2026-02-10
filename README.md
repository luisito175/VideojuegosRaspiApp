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

## Actualización v1.4: Arquitectura MVVM + Firebase

La versión 1.4 introduce una arquitectura moderna basada en **MVVM (Model-View-ViewModel)** con **Clean Architecture** y la integración de **Firebase** para autenticación y persistencia de datos en la nube.

### Arquitectura MVVM

El patrón **MVVM** separa la interfaz de usuario de la lógica de negocio mediante:

- **Model**: Representa los datos de la aplicación (entidades del dominio y datos del repositorio).
- **View**: Actividades y Fragmentos que muestran la interfaz de usuario.
- **ViewModel**: Clase que contiene la lógica de presentación y expone datos mediante `LiveData` para que la View se suscriba a cambios.

#### Ventajas de MVVM:
- Separación clara de responsabilidades
- Código más testeable y mantenible
- Mejor reactividad con `LiveData`
- Estado de UI persistente durante cambios de configuración

### Firebase Integration

#### Autenticación con Firebase

La aplicación utiliza **Firebase Authentication** para gestionar el registro e inicio de sesión de usuarios:

- **Email/Password Authentication**: Registro de nuevos usuarios y autenticación con credenciales.
- **LoginActivity**: Pantalla de inicio de sesión con validación en Firebase.
- **RegisterActivity**: Formulario completo para crear nuevas cuentas con confirmación de contraseña.

**Validaciones implementadas:**
- ✓ Formato de email válido (regex)
- ✓ Contraseña mínimo 6 caracteres
- ✓ Confirmación de contraseña
- ✓ Mensajes de error descriptivos

#### Base de Datos: Firestore

**Cloud Firestore** almacena los datos de usuarios en la nube:

**Colección: `users`**
```
users/
  {userId}/
    - userId: String
    - email: String
    - fullName: String
    - createdAt: Timestamp
```

**Ventajas:**
- Sincronización en tiempo real
- Respaldo automático en la nube
- Seguridad con Rules de Firestore
- Escalabilidad automática

### Componentes Nuevos en v1.4

#### AuthViewModel
ViewModel centralizado que gestiona toda la lógica de autenticación:

```kotlin
// Métodos principales
fun login(email: String, password: String)
fun register(email: String, password: String, fullName: String)
fun logout()
fun isUserLoggedIn(): Boolean
fun getCurrentUserEmail(): String?
```

**LiveData expuesto:**
- `authState`: Estado de autenticación (Success/LoggedOut/Loading)
- `loading`: Indica si hay operación en progreso
- `errorMessage`: Mensajes de error

#### FirebaseModule
Módulo Hilt que proporciona instancias singleton de:
- `FirebaseAuth`: Para operaciones de autenticación
- `FirebaseFirestore`: Para operaciones de base de datos

### Dependencia Inyección con Hilt

La aplicación usa **Hilt** para inyección automática de dependencias:

```kotlin
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    // Hilt proporciona automáticamente el ViewModel
}
```

**Beneficios:**
- Código más limpio sin ServiceLocator manual
- Mejor testabilidad
- Gestión automática del ciclo de vida

### Flujo de Autenticación

1. **Registro**: 
   - Usuario completa formulario con nombre, email, contraseña
   - `AuthViewModel.register()` crea cuenta en Firebase
   - Datos de usuario se guardan en Firestore
   - Logout automático y redirección a LoginActivity

2. **Login**:
   - Usuario ingresa email y contraseña
   - `AuthViewModel.login()` valida contra Firebase
   - Si es exitoso, navega a MainActivity
   - Auto-navegación si usuario ya estaba autenticado

3. **Logout**:
   - Disponible en 3 lugares: Drawer, Menú, Bottom Navigation
   - `FirebaseAuth.signOut()` cierra sesión
   - Limpia activity stack y vuelve a LoginActivity

### Configuración Requerida

Para que Firebase funcione correctamente:

1. **google-services.json**: Descargar desde Firebase Console y colocar en `app/`
2. **Firebase Console**: Crear proyecto, habilitar Authentication y Firestore
3. **Reglas de Firestore** (recomendado):
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth.uid == userId;
    }
  }
}
```

## Componentes Clave
- **`AdapterMoto.kt`**: Adaptador del `RecyclerView`. Se encarga de vincular la lista de motos con las vistas (`CardView`) y de gestionar los eventos de clic (eliminar, editar).
- **`ViewHMoto.kt`**: `ViewHolder` para el adaptador, que mantiene las referencias a las vistas de cada elemento de la lista.
- **`MotoDialogFragment.kt`**: `DialogFragment` que muestra un formulario para añadir o editar los detalles de una moto, incluyendo la selección de imágenes.
- **`Controller.kt`**: Clase que actúa como intermediario entre la `MainActivity` y la capa de datos, manejando la lógica para añadir, borrar y actualizar motos.
- **`Dao/DaoMoto.kt`**: Objeto singleton que simula una fuente de datos (repositorio), proporcionando la lista inicial de motos.
- **`models/Moto.kt`**: `data class` de Kotlin que define la estructura del objeto `Moto`.

## Tecnologías y Librerías

- **Lenguaje**: Kotlin
- **Arquitectura**: Clean Architecture + MVVM
- **Gestión de Estado**:
  - `ViewModel`: Para gestionar la lógica de presentación
  - `LiveData`: Para reactividad y observación de cambios
- **Inyección de Dependencias**:
  - `Hilt`: Framework de inyección de dependencias simplificado
- **UI**: 
  - `ViewBinding`: Para acceder a las vistas de forma segura y concisa.
  - `RecyclerView`: Para mostrar listas de datos de manera eficiente.
  - `CardView`: Para el diseño de cada ítem de la lista.
  - `Material Components`: Para los widgets de la interfaz de usuario como botones y campos de texto.
- **Gestión de Imágenes**:
  - `ActivityResultContracts`: API moderna para obtener resultados de actividades, como la selección de imágenes.
  - `Glide`: Librería para cargar y mostrar imágenes de forma eficiente y fluida.
- **Backend & Persistencia (v1.4+)**:
  - `Firebase Authentication`: Para autenticación con email/password
  - `Cloud Firestore`: Base de datos NoSQL en la nube para sincronización en tiempo real
- **Navegación**:
  - `Navigation Component`: Para gestionar la navegación entre fragmentos y actividades

## Cómo Ejecutar el Proyecto

1.  **Clonar el repositorio**: `git clone <URL_DEL_REPOSITORIO>`
2.  **Abrir en Android Studio**: Importa el proyecto en Android Studio.
3.  **Sincronizar Gradle**: Espera a que Android Studio descargue todas las dependencias necesarias.
4.  **Ejecutar**: Inicia la aplicación en un emulador o en un dispositivo físico con Android.
