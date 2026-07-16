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

<img width="274" height="617" alt="image" src="https://github.com/user-attachments/assets/cf07812b-34e9-44ad-bab0-cba4ac09055c" />

- Crear una nueva orden de servicio.
- Consultar servicios activos.
- Revisar el historial de servicios.
- Visualizar el estado de sus servivios.

### Técnico

<img width="277" height="620" alt="image (1)" src="https://github.com/user-attachments/assets/f3e4e509-89d1-4ac9-bbc1-9aff58cbbe38" />

- Consultar los servicios asignados.
- Actualizar el estado de una orden de servicio.
- Registrar y eliminar observaciones.
- Generar reportes de servicios atendidos por rango de fechas.

### Administrador

<img width="276" height="618" alt="image (2)" src="https://github.com/user-attachments/assets/72582b00-abc0-45cb-9bd1-c3d11eea59ad" />

- Gestionar usuarios del sistema.
- Visualizar todas las órdenes de servicio.
- Asignar técnicos a las órdenes.
- Gestionar los tipos de servicio.
- Administrar el listado de técnicos.

# 5. Arquitectura General del sistema

ServiceTech implementa una arquitectura cliente-servidor compuesta por una aplicación móvil Android y un backend desarrollado con Spring Boot. La aplicación se comunica con el servidor mediante una API REST, mientras que la información se almacena en una base de datos MySQL. La autenticación y autorización de los usuarios se realiza mediante Spring Security con JWT, garantizando un acceso seguro a los recursos del sistema.

<img width="612" height="591" alt="image (3)" src="https://github.com/user-attachments/assets/3f41a58c-e45a-4196-ad18-c9a000ee8d52" />




