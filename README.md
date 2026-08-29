# GymTrack

GymTrack es una aplicación móvil diseñada para entusiastas del fitness que permite explorar un catálogo de ejercicios, gestionar rutinas favoritas y realizar un seguimiento del progreso visual.

## Tecnologías Utilizadas

*   **Kotlin**: Lenguaje de programación principal.
*   **Jetpack Compose**: Kit de herramientas moderno para construir interfaces de usuario nativas.
*   **Retrofit**: Cliente HTTP para consumir la API REST de Supabase.
*   **Supabase**: Backend-as-a-Service para el almacenamiento de datos de ejercicios.
*   **Room**: Librería de persistencia para almacenar favoritos de forma local.
*   **DataStore (Preferences)**: Almacenamiento de configuraciones de usuario (modo oscuro, idioma, etc.).
*   **Navigation Compose**: Gestión de rutas y navegación entre pantallas.
*   **Camera API**: Funcionalidad nativa para capturar fotos de progreso.
*   **Coil**: Carga de imágenes de forma eficiente.

## Arquitectura

La aplicación sigue el patrón **MVVM (Model-View-ViewModel)** y principios de **Clean Architecture**:

`UI (Compose) <-> ViewModel <-> Repository <-> Data Sources (Retrofit/Room/DataStore)`

*   **UI**: Pantallas declarativas que reaccionan al estado expuesto por los ViewModels.
*   **ViewModel**: Gestiona la lógica de presentación y el estado de la UI utilizando `StateFlow`.
*   **Repository**: Actúa como una única fuente de verdad, coordinando datos de la red y de la base de datos local.

## Funcionalidades

*   **Catálogo de Ejercicios**: Obtención de datos en tiempo real desde Supabase mediante Retrofit.
*   **Búsqueda y Filtros**: Filtrado dinámico por nombre o categoría de músculo.
*   **Favoritos Persistentes**: Guardado de ejercicios favoritos en base de datos Room para acceso offline.
*   **Configuraciones Personalizadas**: Persistencia de modo oscuro, idioma y unidades mediante DataStore.
*   **Foto de Progreso**: Integración nativa con la cámara del dispositivo para capturar fotos de entrenamiento.

## Configuración de Supabase

Los datos se obtienen dinámicamente mediante la API REST de Supabase. La configuración de seguridad está centralizada en `RetrofitInstance` utilizando interceptores para la inyección automática de cabeceras de autenticación (`apikey` y `Authorization`).
