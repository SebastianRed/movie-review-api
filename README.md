<div align="center">
  <h1>🎬 Movie Review API</h1>
  <p>
    <img src="https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk" alt="Java" />
    <img src="https://img.shields.io/badge/Spring%20Boot-4.0.3-6DB33F?style=for-the-badge&logo=spring-boot" alt="Spring Boot" />
    <img src="https://img.shields.io/badge/MySQL-8+-4479A1?style=for-the-badge&logo=mysql" alt="MySQL" />
    <img src="https://img.shields.io/badge/JWT-Auth-black?style=for-the-badge&logo=jsonwebtokens" alt="JWT" />
  </p>
  <strong>API REST para registrar usuarios, autenticar con JWT, crear reseñas de películas y series, y consultar información desde TMDb con Spring Boot, JPA y WebClient.</strong>
</div>

---

## 🚀 Funcionalidades

En este repositorio encontrarás una implementación completa de una API de reseñas para contenido audiovisual:

**🔐 Autenticación con JWT**: permite registrar usuarios e iniciar sesión para obtener un token de acceso.

**✍️ CRUD de reseñas**: los usuarios autenticados pueden crear, editar y eliminar sus propias reseñas.

**🎥 Integración con TMDb**: expone endpoints para buscar películas y series, además de consultar detalles y contenido popular.

**📊 Resumen por contenido**: devuelve todas las reseñas de una película o serie junto al promedio de calificación.

**✅ Validación y manejo de errores**: incluye validaciones con Jakarta Validation y respuestas centralizadas para errores de negocio, autorización y APIs externas.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 25
* **Framework Backend:** Spring Boot 4
* **Seguridad:** Spring Security + JWT
* **Persistencia:** Spring Data JPA
* **Base de datos:** MySQL
* **Cliente HTTP:** Spring WebClient
* **Validaciones:** Jakarta Validation
* **Build Tool:** Maven

---

## 🔐 Autenticación

Los endpoints `/api/auth/**` y `/api/tmdb/**` son públicos.

También son públicos:

* `GET /api/reviews/content`
* `GET /api/reviews/user/{username}`

El resto de endpoints de reseñas requiere un token JWT en el header:

```text
Authorization: Bearer <token>
```

---

## 🗂️ Estructura del Proyecto

```text
src/
├── main/
│   ├── java/cl/sebastianrojo/moviereview/
│   │   ├── MovieReviewApiApplication.java          # Punto de entrada Spring Boot
│   │   ├── controller/
│   │   │   ├── AuthController.java                 # Registro y login
│   │   │   ├── ReviewController.java               # Endpoints CRUD y consultas de reseñas
│   │   │   └── TmdbController.java                 # Endpoints proxy hacia TMDb
│   │   ├── dto/
│   │   │   ├── auth/                               # Requests y responses de autenticación
│   │   │   ├── review/                             # DTOs de creación, edición y resumen de reseñas
│   │   │   └── tmdb/                               # Modelos para respuestas de TMDb
│   │   ├── entity/
│   │   │   ├── User.java                           # Usuario persistido con rol
│   │   │   ├── Review.java                         # Reseña asociada a un usuario y contenido externo
│   │   │   ├── Role.java                           # Roles USER y ADMIN
│   │   │   └── ContentType.java                    # Tipos MOVIE y SERIES
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java         # Manejo global de errores
│   │   │   ├── ResourceNotFoundException.java      # Recurso inexistente
│   │   │   ├── DuplicateResourceException.java     # Conflictos por duplicados
│   │   │   ├── UnauthorizedAccessException.java    # Accesos no permitidos
│   │   │   └── ExternalApiException.java           # Errores al consumir TMDb
│   │   ├── repository/
│   │   │   ├── UserRepository.java                 # Acceso a usuarios
│   │   │   └── ReviewRepository.java               # Acceso a reseñas y agregados
│   │   ├── security/
│   │   │   ├── SecurityConfig.java                 # Reglas de seguridad y CORS
│   │   │   ├── JwtFilter.java                      # Filtro de autenticación JWT
│   │   │   ├── JwtUtil.java                        # Generación y validación de tokens
│   │   │   └── CustomUserDetailsService.java       # Adaptador de usuarios para Spring Security
│   │   └── service/
│   │       ├── AuthService.java                    # Lógica de registro y login
│   │       ├── ReviewService.java                  # Lógica de negocio de reseñas
│   │       └── TmdbService.java                    # Integración con la API de TMDb
│   └── resources/
│       ├── application.yml                         # Configuración principal
│       └── application.properties                  # Archivo adicional de configuración
└── test/
    └── java/cl/sebastianrojo/moviereview/
        └── MovieReviewApiApplicationTests.java     # Test base del contexto Spring
```

---

## 💡 Flujo General

1. El usuario se registra o inicia sesión en `/api/auth/*` y recibe un token JWT.
2. El cliente usa ese token para acceder a los endpoints protegidos de reseñas.
3. Al crear una reseña, el sistema valida que el usuario no haya comentado antes el mismo contenido.
4. Las reseñas se guardan en MySQL y se relacionan con el usuario autenticado.
5. Los endpoints de TMDb permiten buscar películas o series y enriquecer la experiencia del frontend con datos externos.
6. El endpoint de resumen por contenido calcula promedio de calificaciones y devuelve las reseñas asociadas.

---

## 📌 Notas

* Cada usuario solo puede crear una reseña por contenido y tipo (`MOVIE` o `SERIES`).
* Las calificaciones están limitadas entre `1` y `5`.
* La API está preparada para consumir TMDb en español usando `language=es-ES`.
* La configuración actual permite CORS para frontends locales en `http://localhost:5173` y `http://localhost:3000`.