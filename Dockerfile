# IMAGEN BASE
FROM eclipse-temurin:21.0.7_6-jdk

# PUERTO EXTERNO DEL CONTENEDOR
EXPOSE 8080

# DIRECTORIO DE TRABAJO
WORKDIR /app

# COPIAR ARCHIVOS DE CONFIGURACION Y MVN
COPY ./pom.xml /app
COPY ./.mvn /app/.mvn
COPY ./mvnw /app

# DESCARGAR DEPENDENCIAS
RUN ./mvnw dependency:go-offline

# COPIAR TODO EL PROYECTO (incluye src)
COPY . /app

# CONSTRUIR LA APLICACION
RUN ./mvnw clean install -DskipTests

# EJECUTAR LA APLICACION (verifica el nombre del .jar generado en target)
ENTRYPOINT ["java", "-jar", "/app/target/jimdur-0.0.1-SNAPSHOT.jar"]
