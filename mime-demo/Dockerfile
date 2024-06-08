FROM openjdk:8-jdk-alpine
EXPOSE 8109
ENV PARAMS=""
ENV JAVA_OPTS=""
ENV TZ=PRC
RUN sed -i 's/http:/https:/g' /etc/apk/repositories
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
RUN add --update --no-cache ttf-dejavu fontconfig && fc-cache --force
RUN apk add --no-cache bash
RUN rm -rf /var/cache/apk/*
ADD target/mime-demo-1.0.0-SNAPSHOT.jar /data/app.jar
WORKDIR /data
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar $PARAMS"]