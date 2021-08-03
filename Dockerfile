FROM adoptopenjdk:16-jre-hotspot
RUN mkdir /opt/app
COPY target/nursing-home-0.0.1-SNAPSHOT.jar /opt/app/nursinghome.jar
CMD ["java","-jar","opt/app/nursinghome.jar"]