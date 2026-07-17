# ServiceTech

## 1. Descripción del proyecto

ServiceTech es un sistema de gestión de servicios técnicos, un proyecto desarrollado de forma individual. Está compuesto por una aplicación móvil para clientes y técnicos, junto con un backend desarrollado en Spring Boot que expone una API REST para la administración de la información.

El sistema permite gestionar el ciclo completo de una orden de servicio, desde su creación por parte del cliente hasta su asignación, seguimiento y finalización. Además, incorpora autenticación y autorización mediante **Spring Security** y **JSON Web Token (JWT)** para garantizar un acceso seguro a los recursos del sistema.

## 2. Objetivos

El objetivo principal de ServiceTech es optimizar la gestión de servicios técnicos mediante una plataforma que facilite la interacción entre clientes, técnicos y administradores.

De manera específica, el proyecto busca:

- Digitalizar el proceso de solicitud y atención de servicios técnicos.
- Facilitar la administración de órdenes de servicio y su seguimiento.
- Mejorar la comunicación entre clientes, técnicos y administradores.
- Centralizar la gestión de usuarios, técnicos y tipos de servicio.
- Implementar una arquitectura cliente-servidor basada en una API REST segura.
- Aplicar buenas prácticas de desarrollo utilizando Spring Boot, Android Studio y Spring Security con autenticación JWT.

## 3. Funcionalidades principales

ServiceTech ofrece las siguientes funcionalidades principales:

- Registro e inicio de sesión de usuarios.
- Creación y gestión de órdenes de servicio.
- Asignación de técnicos a las órdenes de servicio.
- Actualización del estado de los servicios.
- Registro y consulta de observaciones realizadas por los técnicos.
- Consulta de servicios activos e historial de servicios.
- Administración de usuarios y tipos de servicio.
- Generación de reportes para técnicos por rango de fechas.
- Autenticación y autorización mediante **Spring Security** con **JWT**.

## 4. Roles del sistema

ServiceTech cuenta con tres tipos de usuarios, cada uno con funcionalidades específicas dentro del proceso de gestión de servicios técnicos.

### Cliente

<img width="274" height="617" alt="image" src="https://github.com/user-attachments/assets/eeca8cc9-722c-4a93-bb8b-6d5ee5175461" />

- Crear una nueva orden de servicio.
- Consultar servicios activos.
- Revisar el historial de servicios.
- Visualizar el estado de sus servivios.

### Técnico

<img width="275" height="616" alt="image" src="https://github.com/user-attachments/assets/59d6bce0-8bbf-4c10-8153-c3a6833c6f2a" />

- Consultar los servicios asignados.
- Actualizar el estado de una orden de servicio.
- Registrar y eliminar observaciones.
- Generar reportes de servicios atendidos por rango de fechas.

### Administrador

<img width="276" height="618" alt="image (2)" src="https://github.com/user-attachments/assets/d06818be-a6d4-468f-9ce4-d8eb485eaf1b" />

- Gestionar usuarios del sistema.
- Visualizar todas las órdenes de servicio.
- Asignar técnicos a las órdenes.
- Gestionar los tipos de servicio.
- Administrar el listado de técnicos.

# 5. Arquitectura General del sistema

ServiceTech implementa una arquitectura cliente-servidor compuesta por una aplicación móvil Android y un backend desarrollado con Spring Boot. La aplicación se comunica con el servidor mediante una API REST, mientras que la información se almacena en una base de datos MySQL. La autenticación y autorización de los usuarios se realiza mediante Spring Security con JWT, garantizando un acceso seguro a los recursos del sistema.

<img width="612" height="591" alt="image (3)" src="https://github.com/user-attachments/assets/3f41a58c-e45a-4196-ad18-c9a000ee8d52" />

## 6. Tecnologías utilizadas

ServiceTech fue desarrollado utilizando las siguientes tecnologías:

| Tecnología | Uso |
| :--- | :--- |
| Android Studio | Desarrollo de la aplicación móvil |
| Kotlin | Desarrollo del cliente Android |
| XML | Diseño de interfaces de usuario |
| Spring Boot | Desarrollo de la API REST |
| Spring Security + JWT | Autenticación y autorización |
| Spring Data JPA | Persistencia de datos |
| Hibernate | Mapeo objeto-relacional (ORM) |
| MySQL | Base de datos relacional |
| Retrofit | Consumo de la API REST desde Android |
| Git y GitHub | Control de versiones y alojamiento del proyecto |

## 7. Estructura del proyecto

El proyecto está dividido en dos componentes principales: una aplicación móvil desarrollada en Android y un backend desarrollado con Spring Boot.

```text
ServiceTech
├── app/                  # Aplicación Android
│   ├── data/
│   │   ├── model/
│   │   ├── remote/
│   │   └── repository/
│   ├── ui/
│   │   ├── admin/
│   │   ├── auth/
│   │   ├── cliente/
│   │   └── tecnico/
│   ├── utils/
│   ├── res/
│   ├── MainActivity
│   └── ServiceTechApp
└── backend/              # API REST Spring Boot
    ├── config/
    ├── controller/
    ├── dto/
    ├── model/
    ├── repository/
    ├── security/
    ├── service/
    └── resources/
```

# 8. Flujo principal del sistema

El proceso principal de ServiceTech comprende la creación, asignación, atención y seguimiento de una orden de servicio. El flujo general se desarrolla de la siguiente manera:

### 1. Creación de la orden de servicio

El cliente inicia una nueva solicitud seleccionando el tipo de servicio y describiendo el problema que requiere atención.

<img width="277" height="617" alt="image (4)" src="https://github.com/user-attachments/assets/d161cfab-d431-49cc-bdcd-8e1d882944a8" />

### 2. Asignación del técnico

El administrador visualiza las órdenes pendientes y asigna el técnico responsable de atender el servicio.

<img width="276" height="621" alt="image (5)" src="https://github.com/user-attachments/assets/ff253b8d-a9ba-4128-a8d4-996fc00ef231" />

### 3. Atención del servicio

El técnico consulta los servicios asignados, actualiza el estado de la orden y registra observaciones sobre el trabajo realizado durante la atención.

<img width="277" height="617" alt="image (6)" src="https://github.com/user-attachments/assets/6f84962d-01e8-41ce-aee7-954afa620069" />

<img width="276" height="617" alt="image (7)" src="https://github.com/user-attachments/assets/30ae6184-0c20-441c-9166-922319924a70" />

### 4. Seguimiento y finalización

El cliente puede consultar en cualquier momento el estado de su servicio registradas por el técnico hasta la finalización de la orden.

<img width="275" height="619" alt="image (8)" src="https://github.com/user-attachments/assets/4dd40a15-5672-420d-890a-702f247bccda" />

# 9. Instalación y ejecución

## Requisitos

- Android Studio
- JDK 17 o superior
- MySQL Server
- Maven
- Git

## Pasos para ejecutar el proyecto

1. Clonar el repositorio.
2. Crear la base de datos MySQL e importar el script SQL del proyecto.
3. Configurar las credenciales de la base de datos en el archivo `application.properties`.
4. Ejecutar el backend con Spring Boot.
5. Abrir el proyecto Android en Android Studio.
6. Configurar la URL base de la API en el cliente Android.
7. Ejecutar la aplicación en un emulador o dispositivo físico.
