# 版本号

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

version=1.0.0
# shellcheck disable=SC2086
docker build --build-arg MYSQL_URL=$mysql_url \
      --build-arg MYSQL_USER=$mysql_user \
      --build-arg MYSQL_PASSWORD=$mysql_password \
      --build-arg MONGO_HOST=$mongo_host \
      --build-arg MONGO_PORT=$mogo_port \
      --build-arg MONGO_DATABASE=$mongo_database \
      --build-arg MONGO_USER=$mongo_user \
      --build-arg MONGO_PASSWORD=$mongo_password \
      --build-arg MONGO_AUTHENTICATION_DATABASE=$mongo_authentication_database \
      --build-arg REDIS_HOST=$redis_host \
      --build-arg REDIS_PORT=$redis_port \
      --build-arg REDIS_PASSWORD=$redis_password \
      --build-arg RABBITMQ_HOST=$rabbitmq_host \
      --build-arg RABBITMQ_PORT=$rabbitmq_port \
      --build-arg RABBITMQ_USERNAME=$rabbitmq_username \
      --build-arg RABBITMQ_PASSWORD=$rabbitmq_password \
       -t prism030-backend:$version .

echo "prism030-backend:$version is built successfully!"
pwd
whoami
tree
# 允许prism030-backend不存在而报错
docker stop prism030-backend || true
docker rm prism030-backend || true
echo "prism030-backend is stopped and removed successfully!"
docker run --name=prism030-backend -itd -p 8080:8080  --restart=on-failure:3 prism030-backend:$version
echo "prism030-backend is running successfully!"