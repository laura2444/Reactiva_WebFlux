#IMAGEN DEL MODELO--------------------------------------------------------------
#con esto se le dice a docker que se quiere crear una imagen a partir de una que ya existe que es la de eclipse temurin con la version 17 de java
FROM eclipse-temurin:17.0.11_9-jdk

#PUERTO DONDE SE EJECUTA EL CONTENEDOR
EXPOSE 9001

#DEFINIR DIRECTORIO RAIZ DE CONTENEDOR-------------------------------------------
#es una carpeta dentro del conteendor en donde voy a guardar archivos necesarios
WORKDIR /app

#COPIAR Y PEGAR ARCHIVOS DENTRO DEL CONTENEDOR--------------------------------------
#archivos que voya meter dentro del contenedor, comando copy funciona: COPY quevoyacopiar endondelovoyapegar

#se necesita pom por todas las dependencias de maven
COPY ./pom.xml /app

#maven wrapper_ para poder ejecutar comandos de maven.maven wrapper es una version de maven que viene embebido con springboot, se encuentra en la carpeta mvn, es portable
#ademas, dentro de la carpeta app creo otra carpeta llamada mvn que es donde voy a pegar el maven wrapper
COPY ./.mvn /app/.mvn

#ejecutables de maven wrapper _ mvnw para linux y mvnw.cmd para windows
COPY ./mvnw /app

#DESCARGAR DEPENDENCIAS------------------------------------
#comando de maven dentro del contenedor porque ya tengo maven en el contenedor
#con esto le decimos al maven portable que descargue las dependencias dentro del contenedor
RUN ./mvnw dependency:go-offline

#COPIAR EL CODIGO FUENTE DENTRO DEL CONTENEDOR-------------------------------
COPY ./src /app/src

#CONSTRUIR NUESTRA APLICACION----------------------------------------------
#con este comando construimos nuestra aplicacion y le decimos que no incluya los test unitarios porque no tenemos
RUN ./mvnw clean install -DskipTests
#LEVANTAR APLICACION CUANDO EL CONTENEDOR INICIE
#ya no se puede usar el run porque ese se utilizaba para construir la imagen
#el entrypoint ejecuta el comando SOLAMENTE cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "/app/target/webhook-0.0.1-SNAPSHOT.jar"]


#SIGUIENTE PASO- CONSTRUIR LAS IMAGENES DOCKER
#ejecutamos el comando docker build -t "nombreImagen" .          el punto es para poner el directorio donde esta el dockerfil, el punto significa que estamos ubicados en la ruta actualmente
