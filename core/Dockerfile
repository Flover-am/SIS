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

ENV RABBITMQ_HOST=""
ENV RABBITMQ_PORT=""
ENV RABBITMQ_USERNAME=""
ENV RABBITMQ_PASSWORD=""

ENV SPARK_APPID=""
ENV SPARK_APISECRET=""
ENV SPARK_APIKEY=""
ENV NEO4J_URI=""
ENV NEO4J_AUTHENTICATION_USERNAME=""
ENV NEO4J_AUTHENTICATION_PASSWORD=""

ENV SEGMENT_BASEURL=""
WORKDIR /app
COPY core-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["sh", "-c", "java -jar core-0.0.1-SNAPSHOT.jar --server.port=${PORT} --spring.datasource.url=${MYSQL_URL} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD} --spring.data.mongodb.host=${MONGO_HOST} --spring.data.mongodb.port=${MONGO_PORT} --spring.data.mongodb.database=${MONGO_DATABASE} --spring.data.mongodb.username=${MONGO_USER} --spring.data.mongodb.password=${MONGO_PASSWORD} --spring.data.mongodb.authentication-database=${MONGO_AUTHENTICATION_DATABASE} --spring.redis.host=${REDIS_HOST} --spring.redis.port=${REDIS_PORT} --spring.redis.password=${REDIS_PASSWORD} --spring.rabbitmq.host=${RABBITMQ_HOST} --spring.rabbitmq.port=${RABBITMQ_PORT} --spring.rabbitmq.username=${RABBITMQ_USERNAME} --spring.rabbitmq.password=${RABBITMQ_PASSWORD} -Duser.timezone=GMT+08 --spark.appid=${SPARK_APPID} --spark.apisecret=${SPARK_APISECRET} --spark.apikey=${SPARK_APIKEY} --neo4j.uri=${NEO4J_URI} --neo4j.authentication.username=${NEO4J_AUTHENTICATION_USERNAME} --neo4j.authentication.password=${NEO4J_AUTHENTICATION_PASSWORD} --segment.baseurl=${SEGMENT_BASEURL}"]
#ENTRYPOINT ["sh", "-c", "java -jar core-0.0.1-SNAPSHOT.jar --server.port=${PORT} --spring.datasource.url=${MYSQL_URL} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD} --spring.data.mongodb.host=${MONGO_HOST} --spring.data.mongodb.port=${MONGO_PORT} --spring.data.mongodb.database=${MONGO_DATABASE} --spring.data.mongodb.username=${MONGO_USER} --spring.data.mongodb.password=${MONGO_PASSWORD} --spring.data.mongodb.authentication-database=${MONGO_AUTHENTICATION_DATABASE} --spring.redis.host=${REDIS_HOST} --spring.redis.port=${REDIS_PORT} --spring.redis.password=${REDIS_PASSWORD} --spring.rabbitmq.host=${RABBITMQ_HOST} --spring.rabbitmq.port=${RabbitMQ_PORT} --spring.rabbitmq.username=${RabbitMQ_USERNAME} --spring.rabbitmq.password=${RabbitMQ_PASSWORD} -Duser.timezone=GMT+08"]
#ENTRYPOINT ["java","-jar core-0.0.1-SNAPSHOT.jar","--server.port=${PORT}","--spring.datasource.url=${MYSQL_URL}","--spring.datasource.username=${MYSQL_USER}","--spring.datasource.password=${MYSQL_PASSWORD}","--spring.data.mongodb.host=${MONGO_HOST}","--spring.data.mongodb.port=${MONGO_PORT}","--spring.data.mongodb.database=${MONGO_DATABASE}","--spring.data.mongodb.username=${MONGO_USER}","--spring.data.mongodb.password=${MONGO_PASSWORD}","--spring.data.mongodb.authentication-database=${MONGO_AUTHENTICATION_DATABASE}","--spring.redis.host=${REDIS_HOST}","--spring.redis.port=${REDIS_PORT}","--spring.redis.password=${REDIS_PASSWORD}","--spring.rabbitmq.host=${RABBITMQ_HOST}","--spring.rabbitmq.port=${RabbitMQ_PORT}","--spring.rabbitmq.username=${RabbitMQ_USERNAME}","--spring.rabbitmq.password=${RabbitMQ_PASSWORD}"
#,"-Duser.timezone=GMT+08"]

#ENTRYPOINT ["java", "-jar", "core-0.0.1-SNAPSHOT.jar", "--server.port=${PORT}","--spring.datasource.url=$MYSQL_URL","--spring.datasource.username=$MYSQL_USER","--spring.datasource.password=$MYSQL_PASSWORD", "-Duser.timezone=GMT+08"]