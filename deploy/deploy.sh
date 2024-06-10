#!/bin/bash

version=1.0.0
environment=$1
#nacos_serveraddr=$2
#nacos_namespace=$3
#nacos_group=$4
# echo 所有的环境变量
echo "version: $version"
echo "environment: $environment"
#echo "nacos_serveraddr: $nacos_serveraddr"
#echo "nacos_namespace: $nacos_namespace"
#echo "nacos_group: $nacos_group"

image_name=prism030-backend-${environment}
container_name=prism030-backend-${environment}
port=8080
if [ $environment = 'prod' ]; then
  port=8080
  echo "deploying to prod"
elif [ $environment = 'dev' ]; then
  port=8081
  echo "deploying to dev"
else
  echo "environment is not correct"
  exit 0
fi
# 构建 Docker 镜像
docker build -t $image_name:$version .

echo "$image_name:$version is built successfully!"

# 停止并删除容器
docker stop $container_name || true
docker rm $container_name || true
echo "$container_name is stopped and removed successfully!"

# 运行容器，并传递环境变量
docker run --name=$container_name -itd -p $port:8080 --restart=on-failure:3 \
-e ENVIRONMENT=$environment \
-e version=$version \
$image_name:$version

echo "$container_name is running successfully!"
