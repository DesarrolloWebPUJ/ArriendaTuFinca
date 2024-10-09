# 1. Usar una imagen base de OpenJDK para construir el proyecto
FROM maven:3.8.4-openjdk-17 AS build

# Establecer el directorio de trabajo
WORKDIR /app

# 2. Copiar el archivo pom.xml y el código fuente al contenedor
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src

# 3. Ejecutar mvn clean package para compilar el proyecto y crear el archivo WAR
RUN mvn clean package -DskipTests

# 4. Usar una imagen base de Tomcat para ejecutar la aplicación
FROM tomcat:10-jdk17-corretto

# Eliminar las aplicaciones predeterminadas de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el archivo server.xml modificado
COPY server-config/server.xml /usr/local/tomcat/conf/server.xml

# 5. Copiar el archivo WAR generado al directorio webapps de Tomcat
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# 6. Exponer el puerto 8081
EXPOSE 8081

# 7. Definir el comando para ejecutar Tomcat
CMD ["catalina.sh", "run"]