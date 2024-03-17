# SECIII 2024 服务端代码

## 技术栈
SpringBoot + MyBatis-Plus + MySQL

## 运行方法

### 版本细节

- JDK 17
- SpringBoot 2.7.15
- MyBatis-Plus 3.5.3.1
- MySQL >= 8.0.0

### 数据库配置

默认使用端口号`3306`，本地开发使用默认用户名`root`和密码`123456`，数据库名`prism030`

请将`sql`目录下的sql脚本运行并导入数据库

之后运行：

```shell
mvn package
```

```shell
java -jar core/target/core-0.0.1-SNAPSHOT.jar
```