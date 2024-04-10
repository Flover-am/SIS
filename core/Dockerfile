FROM openjdk:17-jdk-alpine

LABEL authors="LiDongSheng"

ENV PORT=8080
ENV TZ="Asia/Shanghai"
ENV MYSQL_URL=""
ENV MYSQL_USER=""
ENV MYSQL_PASSWORD=""

ENV MONGO_HOST=""
ENV MONGO_PORT=""
ENV MONGO_DATABASE=""
ENV MONGO_USER=""
ENV MONGO_PASSWORD=""
ENV MONGO_AUTHENTICATION_DATABASE=""

ENV REDIS_HOST=""
ENV REDIS_PORT=""
ENV REDIS_PASSWORD=""

ENV RabbitMQ_HOST=""
ENV RabbitMQ_PORT=""
ENV RabbitMQ_USERNAME=""
ENV RabbitMQ_PASSWORD=""

WORKDIR /app
COPY core-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["sh", "-c", "java -jar core-0.0.1-SNAPSHOT.jar --server.port=${PORT} --spring.datasource.url=${MYSQL_URL} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD} --spring.data.mongodb.host=${MONGO_HOST} --spring.data.mongodb.port=${MONGO_PORT} --spring.data.mongodb.database=${MONGO_DATABASE} --spring.data.mongodb.username=${MONGO_USER} --spring.data.mongodb.password=${MONGO_PASSWORD} --spring.data.mongodb.authentication-database=${MONGO_AUTHENTICATION_DATABASE} --spring.redis.host=${REDIS_HOST} --spring.redis.port=${REDIS_PORT} --spring.redis.password=${REDIS_PASSWORD} --spring.rabbitmq.host=${RabbitMQ_HOST} --spring.rabbitmq.port=${RabbitMQ_PORT} --spring.rabbitmq.username=${RabbitMQ_USERNAME} --spring.rabbitmq.password=${RabbitMQ_PASSWORD} -Duser.timezone=GMT+08"]
#ENTRYPOINT ["java","-jar core-0.0.1-SNAPSHOT.jar","--server.port=${PORT}","--spring.datasource.url=${MYSQL_URL}","--spring.datasource.username=${MYSQL_USER}","--spring.datasource.password=${MYSQL_PASSWORD}","--spring.data.mongodb.host=${MONGO_HOST}","--spring.data.mongodb.port=${MONGO_PORT}","--spring.data.mongodb.database=${MONGO_DATABASE}","--spring.data.mongodb.username=${MONGO_USER}","--spring.data.mongodb.password=${MONGO_PASSWORD}","--spring.data.mongodb.authentication-database=${MONGO_AUTHENTICATION_DATABASE}","--spring.redis.host=${REDIS_HOST}","--spring.redis.port=${REDIS_PORT}","--spring.redis.password=${REDIS_PASSWORD}","--spring.rabbitmq.host=${RabbitMQ_HOST}","--spring.rabbitmq.port=${RabbitMQ_PORT}","--spring.rabbitmq.username=${RabbitMQ_USERNAME}","--spring.rabbitmq.password=${RabbitMQ_PASSWORD}"
#,"-Duser.timezone=GMT+08"]

#ENTRYPOINT ["java", "-jar", "core-0.0.1-SNAPSHOT.jar", "--server.port=${PORT}","--spring.datasource.url=$MYSQL_URL","--spring.datasource.username=$MYSQL_USER","--spring.datasource.password=$MYSQL_PASSWORD", "-Duser.timezone=GMT+08"]