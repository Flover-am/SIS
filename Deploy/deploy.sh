# 版本号

url=$1
username=$2
password=$3

version=1.0.0
# shellcheck disable=SC2086
docker build --build-arg MYSQL_URL=$url --build-arg MYSQL_USER=$username --build-arg MYSQL_PASSWORD=$password -t prism030-backend:$version .
echo "prism030-backend:$version is built successfully!"
pwd
whoami
tree
# 允许prism030-backend不存在而报错
docker stop prism030-backend || true
docker rm prism030-backend || true
echo "prism030-backend is stopped and removed successfully!"
docker run --name=prism030-backend -itd -p 8080:8080 --restart=on-failure:3 prism030-backend:$version
echo "prism030-backend is running successfully!"