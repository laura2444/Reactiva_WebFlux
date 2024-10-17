#IMAGEN DEL MODELO--------------------------------------------------------------
#con esto se le dice a docker que se quiere crear una imagen a partir de una que ya existe que es la de eclipse temurin con la version 17 de java
FROM eclipse-temurin:11-jdk

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Definir el directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml y las dependencias de Maven
COPY ./pom.xml /app

# Descargar las dependencias de Maven
RUN mvn dependency:go-offline

# Copiar el código fuente del proyecto
COPY ./src /app/src

# Construir la aplicación
RUN mvn clean install -DskipTests

# Exponer el puerto en el contenedor
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se levanta
ENTRYPOINT ["java", "-jar", "/app/target/resolve-enigma-api-1.0.0.jar"]



#SIGUIENTE PASO- CONSTRUIR LAS IMAGENES DOCKER
#ejecutamos el comando docker build -t "nombreImagen" .          el punto es para poner el directorio donde esta el dockerfil, el punto significa que estamos ubicados en la ruta actualmente
