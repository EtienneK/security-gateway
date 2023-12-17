# Running

`docker run -p 8080:8080 etiennek/hac`

# Docker Build and Deploy

```shell
mvn clean install
cd gateway
mvn com.google.cloud.tools:jib-maven-plugin:build -Dimage=etiennek/hac
```

Or native docker build:
```
mvn -Pnative spring-boot:build-image
docker image push docker.io/etiennek/hac:latest
```
