FROM openjdk:17-jdk-slim

WORKDIR /app
ARG JAR_FILE=target/*.jar
ARG GIT_SECRET
ARG GIT_CLIENT_ID=Ov23litOUbZgf6NTZC8Y

COPY ${JAR_FILE} app.jar

ENV NACOS_URL=ld-nacos:8848 \
    MYSQL_URL=ld-mysql:3306 \
    MYSQL_NAME=demo_pd \
    MYSQL_USER=root \
    MYSQL_PASSWORD=123456 \
    REDIS_HOST=ld-redis \
    REDIS_PASSWORD=123456 \
    LDAP_HOST=ld-ldap \
    LDAP_BASE=dc=liz,dc=cn \
    LDAP_USER=cn=admin,dc=liz,dc=cn \
    LDAP_PASSWORD=123456 \
    GIT_SECRET=${GIT_SECRET} \
    GIT_CLIENT_ID=${GIT_CLIENT_ID}

EXPOSE 7571

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${ACTIVE_PROFILE:prod}"]
