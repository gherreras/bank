# Set the base image
FROM openjdk
# Dockerfile author / maintainer 
MAINTAINER makayppe

# Update application repository list 
ENV SERVER_PORT=9444
ENV PATH_XLST=/home/transform
COPY target/transform-0.0.1-SNAPSHOT.jar barclaysTransformService.jar
COPY XSLT/ /home/transform/
RUN ls /home/transform/
EXPOSE 9444
CMD ["java","-jar","barclaysTransformService.jar"]

