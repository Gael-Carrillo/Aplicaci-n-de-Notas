# 📝 Aplicación de Notas

Aplicación móvil Android para gestionar notas personales con categorías y prioridades, desarrollada en **Kotlin** con **Firebase**.

---
## dejare un link para que descarguen el manual 
https://drive.google.com/drive/folders/1g9OZqVounVnuPw_v7FIvQEQ9XLudIh8r?usp=sharing

---

## 🚀 Características

- ✅ Crear, editar y eliminar notas  
- 🏷️ Organización por categorías  
- ⭐ Sistema de prioridades para notas  
- 🔐 Autenticación de usuarios (Login/Registro)  
- ☁️ Sincronización en la nube con Firebase  
- 🔔 Notificaciones push mediante Firebase Cloud Messaging  
- 🎨 Interfaz moderna con Jetpack Compose  
- 🌙 Temas personalizables  

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Kotlin  
- **UI Framework:** Jetpack Compose  
- **Arquitectura:** MVVM (Model-View-ViewModel)  
- **Base de datos:** Firebase Firestore  
- **Autenticación:** Firebase Authentication  
- **Notificaciones:** Firebase Cloud Messaging  
- **Navegación:** Jetpack Navigation Compose  
- **Inyección de dependencias:** Hilt/Dagger (opcional)  

---

## 📋 Requisitos Previos

- Android Studio Hedgehog o superior  
- JDK 11 o superior  
- Cuenta de Firebase (para configuración del proyecto)  
- Dispositivo Android con API 24+ (Android 7.0) o emulador  

---

## ⚙️ Instalación

1. Clonar repositorio  
2. Agregar `google-services.json` en la carpeta `app/`  
3. Compilar en Android Studio  
4. Configurar Firebase:
   - Crear proyecto en Firebase Console  
   - Habilitar **Authentication (Email/Password)**  
   - Habilitar **Cloud Firestore**  

---

## 📁 Estructura del Proyecto

---




---

## 🎯 Funcionalidades Principales

### 🔐 Autenticación
- Registro de nuevos usuarios con email y contraseña  
- Inicio de sesión para usuarios existentes  
- Gestión de sesión persistente  

### 📝 Gestión de Notas
- Crear notas con título, contenido, categoría y prioridad  
- Editar notas existentes  
- Eliminar notas  
- Visualización de todas las notas del usuario  

### 🏷️ Categorías
- Crear y gestionar categorías personalizadas  
- Filtrar notas por categoría  
- Asignar colores y emojis a las categorías  

### ⭐ Prioridades
- Sistema de prioridades (Alta, Media, Baja)  
- Indicadores visuales de prioridad  

---

## 📱 Capturas de Pantalla
*(Agrega aquí imágenes de la aplicación en funcionamiento)*

---

## 📦 Código Fuente

El proyecto incluye modelos, repositorios y servicios listos para integrarse con Firebase:

- `Category.kt` → Modelo de categorías  
- `Note.kt` → Modelo de notas  
- `Priority.kt` → Enum de prioridades  
- `User.kt` → Modelo de usuario  
- `FirebaseRepository.kt` → CRUD y autenticación con Firebase  
- `Navigation.kt` → Sistema de navegación con Compose  
- `MyFirebaseMessagingService.kt` → Manejo de notificaciones push  
- `NotificationReceiver.kt` → Notificaciones programadas  

---

## 👨‍💻 Autor

Proyecto desarrollado como parte de **Game01 - Juego de Plataformas 2D Funcional** y extendido para la **Aplicación de Notas**.  

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Puedes usarlo, modificarlo y distribuirlo libremente.
