FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jiangxd@mingbyte.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /ruoyi-config

WORKDIR /ruoyi-config

EXPOSE 8888

ADD ./ruoyi-config/target/ruoyi-config-1.1.0-SNAPSHOT.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar ruoyi-config-1.1.0-SNAPSHOT.jar
