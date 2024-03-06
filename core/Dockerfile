FROM openjdk:17-jdk-alpine

LABEL authors="LiDongSheng"

ENV PORT=8080
ENV TZ="Asia/Shanghai"

WORKDIR /app

COPY ./backend/core-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java", "-jar", "core-0.0.1-SNAPSHOT.jar", "--server.port=${PORT}", "-Duser.timezone=GMT+08"]