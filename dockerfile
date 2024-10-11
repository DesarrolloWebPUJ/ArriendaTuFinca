FROM openjdk:17
FROM maven:3.8.4-openjdk-17

# Copia el código fuente de la aplicación a la imagen
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . /app

# Establece el directorio de trabajo
WORKDIR /app

# Construye el archivo JAR de la aplicación
RUN ./mvnw clean install -DskipTests

# Ejecuta la aplicación Spring Boot cuando se inicia el contenedor
CMD ["java", "-jar", "target/arriendatufinca-0.0.1-SNAPSHOT.jar"]
