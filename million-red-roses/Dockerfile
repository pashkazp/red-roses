# to make million-red-roses.jar
# $ mvn clean package
#
# to build docker
# $ docker build -t red-roses:0.0.9 .
#
# to run docker
# $ docker run --name red-roses -d -p 8080:8080 -t red-roses:0.0.9
# 

FROM adoptopenjdk/openjdk13:alpine-jre
WORKDIR /opt/app
COPY target/million-red-roses.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]