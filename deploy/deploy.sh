#!/bin/bash

mysql_url=$1
mysql_user=$2
mysql_password=$3

mongo_host=$4
mongo_port=$5
mongo_database=$6
mongo_user=$7
mongo_password=$8
mongo_authentication_database=$9

redis_host=${10}
redis_port=${11}
redis_password=${12}

rabbitmq_host=${13}
rabbitmq_port=${14}
rabbitmq_username=${15}
rabbitmq_password=${16}

spark_appid=${17}
spark_apisecret=${18}
spark_apikey=${19}

neo4j_uri=${20}
neo4j_authentication_username=${21}
neo4j_authentication_password=${22}
segment_baseurl=${23}
version=1.0.0

# echo 所有的环境变量
echo "mysql_url: $mysql_url"
echo "mysql_user: $mysql_user"
echo "mysql_password: $mysql_password"

echo "mongo_host: $mongo_host"
echo "mongo_port: $mongo_port"
echo "mongo_database: $mongo_database"
echo "mongo_user: $mongo_user"
echo "mongo_password: $mongo_password"
echo "mongo_authentication_database: $mongo_authentication_database"

echo "redis_host: $redis_host"
echo "redis_port: $redis_port"
echo "redis_password: $redis_password"

echo "rabbitmq_host: $rabbitmq_host"
echo "rabbitmq_port: $rabbitmq_port"
echo "rabbitmq_username: $rabbitmq_username"
echo "rabbitmq_password: $rabbitmq_password"

echo "spark_appid: $spark_appid"
echo "spark_apisecret: $spark_apisecret"
echo "spark_apikey: $spark_apikey"

echo "neo4j_uri: $neo4j_uri"
echo "neo4j_authentication_username: $neo4j_authentication_username"
echo "neo4j_authentication_password: $neo4j_authentication_password"
echo "segment_baseurl: $segment_baseurl"
echo "version: $version"

# 构建 Docker 镜像
docker build -t prism030-backend:$version .

echo "prism030-backend:$version is built successfully!"

# 停止并删除容器
docker stop prism030-backend || true
docker rm prism030-backend || true
echo "prism030-backend is stopped and removed successfully!"

# 运行容器，并传递环境变量
docker run --name=prism030-backend -itd -p 8080:8080 --restart=on-failure:3 \
-e MYSQL_URL=$mysql_url \
-e MYSQL_USER=$mysql_user \
-e MYSQL_PASSWORD=$mysql_password \
-e MONGO_HOST=$mongo_host \
-e MONGO_PORT=$mongo_port \
-e MONGO_DATABASE=$mongo_database \
-e MONGO_USER=$mongo_user \
-e MONGO_PASSWORD=$mongo_password \
-e MONGO_AUTHENTICATION_DATABASE=$mongo_authentication_database \
-e REDIS_HOST=$redis_host \
-e REDIS_PORT=$redis_port \
-e REDIS_PASSWORD=$redis_password \
-e RABBITMQ_HOST=$rabbitmq_host \
-e RABBITMQ_PORT=$rabbitmq_port \
-e RABBITMQ_USERNAME=$rabbitmq_username \
-e RABBITMQ_PASSWORD=$rabbitmq_password \
-e SPARK_APPID=$spark_appid \
-e SPARK_APISECRET=$spark_apisecret \
-e SPARK_APIKEY=$spark_apikey \
-e NEO4J_URI=$neo4j_uri \
-e NEO4J_AUTHENTICATION_USERNAME=$neo4j_authentication_username \
-e NEO4J_AUTHENTICATION_PASSWORD=$neo4j_authentication_password \
-e SEGMENT_BASEURL=$segment_baseurl \
-e version=$version \
prism030-backend:$version

echo "prism030-backend is running successfully!"
