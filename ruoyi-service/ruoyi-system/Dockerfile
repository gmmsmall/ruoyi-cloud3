FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jiangxd@mingbyte.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /ruoyi-system

WORKDIR /ruoyi-system

EXPOSE 8001

ADD ./ruoyi-system/target/ruoyi-system-1.1.0-SNAPSHOT.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar ruoyi-system-1.1.0-SNAPSHOT.jar
