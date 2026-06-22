<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.4.1"/>
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf"/>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Tailwind%20CSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white" alt="Tailwind CSS"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"/>
  <img src="https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=white" alt="Render"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
</p>

# 📦 MiniCoreLogistica_Mullo

**Sistema Mini Core de Logística y Cálculo de Tarifas de Distribución**

Aplicación web full-stack desarrollada con **Spring Boot 3.4.1** que implementa el patrón arquitectónico **MVC (Modelo-Vista-Controlador)** para gestionar y calcular los costos de distribución logística por repartidor, zona geográfica y peso de envío. La interfaz de usuario está construida con **Thymeleaf** y **Tailwind CSS**, conectándose a una base de datos **PostgreSQL** alojada en **Supabase**.

---

## 🧮 Lógica Core — Cálculo de Costos

El corazón del sistema reside en la clase `LogisticaService.java`, donde se implementa la fórmula de cálculo financiero para cada envío individual:

```
Costo por Envío = peso_kg × tarifa_por_kg
```

El **costo total por repartidor** se obtiene mediante la sumatoria acumulada de todos sus envíos dentro del rango de fechas seleccionado:

```
Costo Total Repartidor = Σ (envío[i].pesoKg × envío[i].zona.tarifaPorKg)
```

> Todas las operaciones monetarias se realizan con `java.math.BigDecimal` y sus métodos `.multiply()` y `.add()` para evitar errores de precisión de punto flotante.

### Algoritmo de Procesamiento

1. Se obtienen **todos los repartidores** registrados (`repartidorRepository.findAll()`).
2. Se filtran los **envíos por rango de fechas** (`envioRepository.findByFechaEnvioBetween()`).
3. Los envíos se **agrupan por ID de repartidor** usando `Collectors.groupingBy()`.
4. Para cada repartidor se calcula: cantidad de envíos, total de kg, zonas únicas, tarifas aplicadas y costo total.
5. Los repartidores sin envíos en el periodo reciben valores en cero con indicadores `"—"`.

---

## 🏗️ Arquitectura MVC

El proyecto sigue estrictamente el patrón **Modelo-Vista-Controlador** con separación clara de responsabilidades en los siguientes paquetes:

```
src/main/java/com/minicore/logistica/
├── LogisticaApplication.java          → Punto de entrada (@SpringBootApplication)
├── model/                             → 🔷 MODELO (Entidades JPA)
│   ├── Repartidor.java                   Tabla "repartidor" (id, nombre, email)
│   ├── Zona.java                          Tabla "zonas" (id, nombreZona, tarifaPorKg)
│   └── Envio.java                         Tabla "envios" (id, repartidor, zona, pesoKg, fechaEnvio)
├── repository/                        → 🔷 REPOSITORIOS (Spring Data JPA)
│   ├── RepartidorRepository.java          JpaRepository<Repartidor, Integer>
│   └── EnvioRepository.java              JpaRepository<Envio, Integer> + findByFechaEnvioBetween()
├── dto/                               → 🔷 DTO (Transferencia de datos)
│   └── ResumenRepartidorDTO.java          Objeto inmutable para la vista del reporte
├── service/                           → 🔷 SERVICIO (Lógica de negocio)
│   └── LogisticaService.java              Método obtenerReporteCostos(fechaInicio, fechaFin)
└── controller/                        → 🔷 CONTROLADOR (Capa web)
    └── LogisticaController.java           @GetMapping("/") con filtrado por fechas

src/main/resources/
├── application.properties             → Configuración de BD (PostgreSQL/Supabase)
└── templates/
    └── index.html                     → 🔷 VISTA (Thymeleaf + Tailwind CSS)
```

### Responsabilidades por Capa

| Capa | Paquete | Responsabilidad |
|------|---------|-----------------|
| **Modelo** | `com.minicore.logistica.model` | Entidades JPA que mapean las tablas `repartidor`, `zonas` y `envios` con relaciones `@ManyToOne` |
| **Repositorio** | `com.minicore.logistica.repository` | Interfaces Spring Data JPA que proveen operaciones CRUD y consultas derivadas por rango de fechas |
| **DTO** | `com.minicore.logistica.dto` | Objeto de transferencia inmutable (`ResumenRepartidorDTO`) que encapsula los datos calculados para la vista |
| **Servicio** | `com.minicore.logistica.service` | Lógica core de agrupación, cálculo de costos con `BigDecimal` y generación del reporte |
| **Controlador** | `com.minicore.logistica.controller` | Manejo de peticiones HTTP GET, binding de parámetros de fecha y envío de atributos al modelo de Thymeleaf |
| **Vista** | `templates/index.html` | Interfaz responsive con formulario de filtrado, tabla condicional de resultados y estado vacío inicial |

---

## 🗃️ Modelo de Datos

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│  repartidor  │       │    envios    │       │    zonas     │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id_repartidor│◄──────│ id_repartidor│       │ id_zona      │
│ nombre       │  FK   │ id_zona      │──────►│ nombre_zona  │
│ email        │       │ peso_kg      │  FK   │ tarifa_por_kg│
└──────────────┘       │ fecha_envio  │       └──────────────┘
                       │ id_envio     │
                       └──────────────┘
```

---

## 🚀 Guía de Instalación Local

### Prerrequisitos

- **Java 17** (JDK) instalado y configurado en `JAVA_HOME`
- **Maven 3.8+** instalado
- **Git** instalado

### Pasos

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/sekai882/MiniCoreLogistica_Mullo.git
   cd MiniCoreLogistica_Mullo
   ```

2. **Compilar el proyecto (generar el JAR):**
   ```bash
   mvn clean package -DskipTests
   ```

3. **Ejecutar la aplicación:**
   ```bash
   java -jar target/logistica-0.0.1-SNAPSHOT.jar
   ```
   O alternativamente con Maven:
   ```bash
   mvn spring-boot:run
   ```

4. **Acceder a la aplicación:**
   Abrir el navegador en [http://localhost:8080](http://localhost:8080)

### Ejecución con Docker

```bash
docker build -t minicore-logistica .
docker run -p 8080:8080 minicore-logistica
```

---

## 🐳 Despliegue en la Nube

El proyecto incluye un `Dockerfile` multi-etapa optimizado para despliegue en plataformas PaaS:

| Etapa | Imagen Base | Función |
|-------|-------------|---------|
| **Build** | `maven:3.8.8-eclipse-temurin-17` | Compilación del proyecto con Maven |
| **Runtime** | `eclipse-temurin:17-jdk-jammy` | Ejecución del JAR en puerto 8080 |

---

## 🔗 Enlaces del Proyecto

| Recurso | Enlace |
|---------|--------|
| 🌐 **Proyecto en Vivo** | [https://minicore-logistica-mullo.onrender.com](https://minicore-logistica-mullo.onrender.com) |
| 📂 **Repositorio GitHub** | [https://github.com/sekai882/MiniCoreLogistica_Mullo](https://github.com/sekai882/MiniCoreLogistica_Mullo) |
| 🎥 **Video Explicativo** | [https://youtu.be/t0ECiZal6K4](https://youtu.be/t0ECiZal6K4) |

---

## 📚 Documentación y Referencias

- [Documentación Oficial de Spring Boot](https://docs.spring.io/spring-boot/index.html)
- [Spring Boot Tutorial Full Course — Amigoscode (YouTube)](https://www.youtube.com/watch?v=9SGDpanrc8U)
- [Spring Boot Tutorial for Beginners — freeCodeCamp (YouTube)](https://www.youtube.com/watch?v=vtPkZShrvXQ)

---

## 📬 Contacto

| | Datos |
|---|---|
| 👤 **Autor** | Josué Mullo |
| 📧 **Correo Institucional** | josue.mullo@udla.edu.ec |
| 📧 **Correo Personal** | josuemullo10@hotmail.com |

---

<p align="center">
  <sub>Desarrollado con ☕ Java 17 y 💚 Spring Boot — Universidad de Las Américas (UDLA)</sub>
</p>
