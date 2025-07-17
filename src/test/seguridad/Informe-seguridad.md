# Informe de Seguridad del Proyecto

**Proyecto:** Jimdur Repuestos
**Equipo:** [Nombre del equipo]  
**Fecha:** [15/07/2025]

---

## 1. Introducción

Este informe documenta el análisis de seguridad realizado sobre el proyecto actual, utilizando herramientas automáticas y revisión manual, basado en los riesgos definidos por OWASP Top 10.

## 2. Metodología

Para el análisis de seguridad de este proyecto se ha utilizado la herramienta automática OWASP ZAP, la cual permite identificar vulnerabilidades comunes en aplicaciones web mediante escaneo y pruebas automatizadas. Además, se complementó el análisis con una revisión manual del código y de la configuración de seguridad de la aplicación.

## 3. Análisis de Riesgos (OWASP Top 10)

A continuación, se presentan los principales riesgos de seguridad identificados en el proyecto tras la ejecución de un análisis automatizado con OWASP ZAP y una revisión manual. Cada riesgo se describe junto con su nivel de prioridad, una explicación del hallazgo y recomendaciones específicas para su mitigación. Esta información permite priorizar acciones de mejora y fortalecer la seguridad de la aplicación.

### 3.1. Cabecera Content Security Policy (CSP) no configurada (alerta con prioridad media)
- **Hallazgos**: 
*La Política de seguridad de contenido (CSP) es una capa adicional de seguridad que ayuda a detectar y mitigar ciertos tipos de ataques, incluidos Cross Site Scripting (XSS) y ataques de inyección de datos. Estos ataques se utilizan para todo, desde el robo de datos hasta la desfiguración del sitio o la distribución de malware. CSP proporciona un conjunto de encabezados HTTP estándar que permiten a los propietarios de sitios web declarar fuentes de contenido aprobadas que los navegadores deberían poder cargar en esa página; los tipos cubiertos son JavaScript, CSS, marcos HTML, fuentes, imágenes y objetos incrustados como applets de Java, ActiveX, archivos de audio y video.*
- **Recomendaciones**: 
*Asegúrese de que su servidor web, servidor de aplicaciones, balanceador de carga, etc. esté configurado para establecer la cabecera Content-Security-Policy.*  

### 3.2. ID de sesión en la Reescritura de URL(alerta con prioridad media)
- **Hallazgos:**  
*La reescritura de URL se utiliza para rastrear el ID de sesión del usuario. El ID de sesión puede ser revelado a través del encabezado cross-site referer. Además, el ID de sesión puede almacenarse en el historial del navegador o en los registros del servidor.*
- **Recomendaciones:**  
*Para contenido seguro, ponga el ID de sesión en una cookie. Para ser aún más seguro considere usar una combinación de cookie y URL rewrite.*

### 3.3. Método de autenticación débil(alerta con prioridad media)
- **Hallazgos:**  
*Método HTTP de autenticación básica o por intercambio de hash, se ha utilizado sobre una conexión no segura. Las credenciales pueden ser leu00eddas y luego reutilizadas por alguien con acceso a la red.*
- **Recomendaciones:**  
*Proteger la conexión usando HTTPS o usar un mecanismo de autenticación más fuerte.*

### 3.4. Cookie sin el atributo SameSite (alerta con baja prioridad)
- **Hallazgos:**  
*Se ha establecido una cookie sin el atributo SameSite, lo que significa que la cookie puede ser enviada como resultado de una solicitud 'cross-site'. El atributo SameSite es una medida eficaz para contrarrestar la falsificación de peticiones entre sitios, la inclusión de scripts entre sitios y los ataques de sincronización.*
- **Recomendaciones:**  
*Asegúrese que el atributo SameSite está establecido como 'lax' o idealmente 'strict' para todas las cookies.*

### 3.5. Inclusión de archivos fuente JavaScript entre dominios (alerta con baja prioridad) 
- **Hallazgos:**  
*La página incluye uno o más archivos de script de un dominio de terceros.*
- **Recomendaciones:**  
*Asegúrese de que los archivos fuente JavaScript se cargan solo desde fuentes de confianza, y que las fuentes no pueden ser controladas por los usuarios finales de la aplicación.*

### 3.6. Aplicación Web Moderna (alertas informativas)
- **Hallazgos:**  
*La aplicación parece ser una aplicación web moderna. Si necesita explorarla automáticamente, el Ajax Spider puede ser más eficaz que el estándar.*
- **Recomendaciones:**  
*Se trata de una alerta informativa, por lo que no es necesario realizar ningún cambio.*

### 3.7. Divulgación de información - Comentarios sospechosos (alertas informativas)
- **Hallazgos:**  
*La respuesta parece contener comentarios sospechosos que podrían ayudar a un atacante.*
- **Recomendaciones:**  
*Eliminar todos los comentarios que muestren información que pueda ayudar a un atacante y solucionar el problema al que se refieren.*

### 3.8. Petición de Autenticación Identificada (alertas informativas)
- **Hallazgos:**  
*La petición en cuestión se ha identificado como una petición de autenticación. El campo "Otra información" contiene un conjunto de líneas key=vvalue que identifican cualquier campo relevante. Si la solicitud está en un contexto que tiene un método de autenticación configurado como "Detección automática", esta regla cambiará la autenticación para que coincida con la petición identificada.*
- **Recomendaciones:**  
*Se trata de una alerta informativa y no de una vulnerabilidad, por lo que no hay nada que corregir.*

### 3.9. Respuesta de Gestión de Sesión Identificada (alertas informativas)
- **Hallazgos:**  
*Se ha identificado que la respuesta dada contiene un token de gestión de sesión. El campo 'Other Info' contiene un conjunto de tokens de cabecera que pueden utilizarse en el método Header Based Session Management (gestión de sesión basado en cabecera). Si la petición se encuentra en un contexto que tiene un método Session Management establecido en "Auto-Detect", esta regla cambiará la gestión de sesión para utilizar los tokens identificados.*
- **Recomendaciones:**  
*Se trata de una alerta informativa y no de una vulnerabilidad, por lo que no hay nada que corregir.*


## 4. Conclusiones Generales

El análisis de seguridad realizado sobre el proyecto Jimdur Repuestos permitió identificar varios riesgos relevantes, principalmente relacionados con la configuración de cabeceras de seguridad, gestión de sesiones, métodos de autenticación y exposición de información a través de comentarios en el código. La mayoría de los hallazgos corresponden a configuraciones que pueden ser mejoradas para fortalecer la protección frente a ataques comunes como XSS, CSRF y fugas de información.

**Principales riesgos detectados:**
- Ausencia de la cabecera Content Security Policy (CSP).
- Uso de ID de sesión en la URL.
- Métodos de autenticación débiles.
- Cookies sin el atributo SameSite.
- Inclusión de archivos JavaScript desde dominios externos.
- Comentarios sospechosos en el código fuente.

**Estado general:**  
El proyecto presenta un nivel de seguridad aceptable para un entorno de desarrollo, pero requiere la implementación de las recomendaciones propuestas para alcanzar un estándar adecuado en producción.

**Próximos pasos recomendados:**
- Configurar correctamente las cabeceras de seguridad (CSP, SameSite, etc.).
- Revisar y fortalecer los mecanismos de autenticación y gestión de sesiones.
- Limitar la inclusión de recursos externos solo a fuentes confiables.
- Eliminar información sensible de los comentarios en el código.
- Realizar revisiones periódicas de seguridad y pruebas automatizadas antes de cada despliegue.


---

*Este informe fue generado como parte del proceso de mejora continua de la seguridad del proyecto.*
