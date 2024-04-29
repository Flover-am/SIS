FROM openjdk:17-jdk-alpine

LABEL authors="LiDongSheng"

ENV PORT=8080
ENV TZ="Asia/Shanghai"
ENV ENVRIONMENT=prod
#ENV NACOS_SERVERADDR=''
#ENV NACOS_NAMESPACE=''
#ENV NACOS_GROUP=''
#RUN echo "NACOS_SERVERADDR=${NACOS_SERVERADDR} , NACOS_NAMESPACE=${NACOS_NAMESPACE} , NACOS_GROUP=${NACOS_GROUP}"
WORKDIR /app
COPY core-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["sh", "-c", "java -jar core-0.0.1-SNAPSHOT.jar --spring.profiles.active=${ENVRIONMENT} --server.port=${PORT} -Duser.timezone=GMT+08"]
