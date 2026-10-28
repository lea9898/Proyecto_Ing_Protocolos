# Induztek Protocolos - Android Studio

Adaptación de `MiRegistroRoles` al caso académico **Induztek Ingeniería SpA**. Proyecto Kotlin + Jetpack Compose para Android.

## Abrir
1. Descomprime el ZIP.
2. Abre la carpeta `MiRegistroRoles` desde Android Studio.
3. Espera la sincronización de Gradle y ejecuta en un emulador o dispositivo Android.

## Identidad visual
La interfaz utiliza una paleta industrial: azul petróleo para navegación y acciones principales, turquesa para elementos secundarios, naranja de seguridad para estados pendientes y un fondo gris azulado claro para facilitar la lectura. También se ajustaron los colores de los campos y del modo oscuro.

## Roles conservados (5)
1. Técnico de terreno
2. Ingeniero de pruebas
3. Jefe técnico
4. Usuario cliente
5. Administrador

## Módulo anadido: Ingeniería de Protocolos
En el menú lateral se agregó **Ingeniería de Protocolos**, con secciones para:
- Consultar clientes de ejemplo del Anexo 9.
- Consultar equipos de ejemplo del Anexo 9.
- Buscar protocolos por número, TAG o cliente.
- Consultar protocolos históricos de ejemplo de TR-01.
- Registrar un protocolo de resistencia de aislación con cliente, instalación, equipo, técnico, fecha, temperatura, humedad, lecturas a 1 y 10 minutos y observaciones.
- Calcular el índice de polarización (IP = lectura 10 min / lectura 1 min) y un resultado inicial usando RN-06: lectura a 1 min menor a 100 MΩ o IP < 1 = Rechazado; IP de 1 a menos de 2 = Observado; IP >= 2 = Aprobado.
- Guardar protocolos localmente en SQLite, por lo que el registro creado permanece en el dispositivo sin conexión.
- Marcar registros nuevos como **Registrado en terreno / Pendiente de formalización**.
- Validar lecturas positivas, temperatura entre -20 y 70 °C, humedad entre 0 y 100 %, y número de protocolo no repetido.

## Importante: alcance de esta versión
Este ZIP es un MVP académico inicial, no una implementación completa de todos los RF/HU del documento. El módulo de protocolos implementa actualmente el flujo de registro y consulta básica para la prueba de aislación. Aún deben desarrollarse y probarse formalmente: autenticación y permisos por rol; CRUD completo de clientes, instalaciones y equipos; formularios de todos los tipos de prueba de RN-05; formalización/observación/anulación con trazabilidad; acta de visita y constancia de recepción con validación de RUT; comparación histórica general para todos los tipos de prueba; gestión de instrumentos/calibración; filtros por estado/técnico/cliente; aislamiento de datos para cada cuenta cliente; generación/compartición de PDF; y sincronización con servidor cuando vuelva la senal.

Los nombres, clientes, equipos y mediciones de ejemplo proceden de los anexos entregados y son datos ficticios de uso académico.
