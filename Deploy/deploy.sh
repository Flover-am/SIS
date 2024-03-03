# 版本号
version=1.0.0
docker build -t prism036-backend:$version .
echo "prism036-backend:$version is built successfully!"
# 允许prism036-backend不存在而报错
docker stop prism036-backend || true
docker rm prism036-backend || true
echo "prism036-backend is stopped and removed successfully!"
docker run --name=prism036-backend -itd -p 8080:8080 prism036-backend:$version
echo "prism036-backend is running successfully!"