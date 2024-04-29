FROM openjdk:17-jdk-alpine

LABEL authors="LiDongSheng"

ENV PORT=8080
ENV TZ="Asia/Shanghai"
ENV ENVRIONMENT=prod
ENV NACOS_SERVERADDR=''
ENV NACOS_NAMESPACE=''
ENV NACOS_GROUP=''
WORKDIR /app
COPY core-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["sh", "-c", "java -jar core-0.0.1-SNAPSHOT.jar --spring.profiles.active=${ENVRIONMENT} --server.port=${PORT} -Duser.timezone=GMT+08 -Dspring.cloud.nacos.config.server-addr=${NACOS_SERVERADDR} -Dspring.cloud.nacos.config.namespace=${NACOS_NAMESPACE} -Dspring.cloud.nacos.config.group=${NACOS_GROUP} "]
