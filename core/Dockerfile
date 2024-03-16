FROM openjdk:17-jdk-alpine

LABEL authors="LiDongSheng"

ENV PORT=8080
ENV TZ="Asia/Shanghai"
ARG MYSQL_URL
ARG MYSQL_USER
ARG MYSQL_PASSWORD
WORKDIR /app
RUN echo "MYSQL_URL is: $MYSQL_URL"
COPY core-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java", "-jar", "core-0.0.1-SNAPSHOT.jar", "--server.port=${PORT}","--spring.datasource.url=$MYSQL_URL","--spring.datasource.username=$MYSQL_USER","--spring.datasource.password=$MYSQL_PASSWORD", "-Duser.timezone=GMT+08"]