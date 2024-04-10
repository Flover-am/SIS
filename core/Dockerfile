FROM openjdk:17-jdk-alpine

LABEL authors="LiDongSheng"

ENV PORT=8080 \
    TZ="Asia/Shanghai" \
    MYSQL_URL="" \
    MYSQL_USER="" \
    MYSQL_PASSWORD="" \

    MONGO_HOST="" \
    MONGO_PORT="" \
    MONGO_DATABASE="" \
    MONGO_USER="" \
    MONGO_PASSWORD="" \
    MONGO_AUTHENTICATION_DATABASE="" \

    REDIS_HOST="" \
    REDIS_PORT="" \
    REDIS_PASSWORD="" \

    RabbitMQ_HOST="" \
    RabbitMQ_PORT="" \
    RabbitMQ_USERNAME="" \
    RabbitMQ_PASSWORD=""
WORKDIR /app
RUN echo "MYSQL_URL is: $MYSQL_URL"
COPY core-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java","-jar core-0.0.1-SNAPSHOT.jar","--server.port=${PORT}","--spring.datasource.url=${MYSQL_URL}","--spring.datasource.username=${MYSQL_USER}","--spring.datasource.password=${MYSQL_PASSWORD}","--spring.data.mongodb.host=${MONGO_HOST}","--spring.data.mongodb.port=${MONGO_PORT}","--spring.data.mongodb.database=${MONGO_DATABASE}","--spring.data.mongodb.username=${MONGO_USER}","--spring.data.mongodb.password=${MONGO_PASSWORD}","--spring.data.mongodb.authentication-database=${MONGO_AUTHENTICATION_DATABASE}","--spring.redis.host=${REDIS_HOST}","--spring.redis.port=${REDIS_PORT}","--spring.redis.password=${REDIS_PASSWORD}","--spring.rabbitmq.host=${RabbitMQ_HOST}","--spring.rabbitmq.port=${RabbitMQ_PORT}","--spring.rabbitmq.username=${RabbitMQ_USERNAME}","--spring.rabbitmq.password=${RabbitMQ_PASSWORD}","-Duser.timezone=GMT+08"]

#ENTRYPOINT ["java", "-jar", "core-0.0.1-SNAPSHOT.jar", "--server.port=${PORT}","--spring.datasource.url=$MYSQL_URL","--spring.datasource.username=$MYSQL_USER","--spring.datasource.password=$MYSQL_PASSWORD", "-Duser.timezone=GMT+08"]