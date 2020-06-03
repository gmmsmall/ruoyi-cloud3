FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jiangxd@mingbyte.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /ruoyi-mb-system

WORKDIR /ruoyi-mb-system

EXPOSE 8111

ADD ./target/ruoyi-mb-system-1.1.0-SNAPSHOT.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar ruoyi-mb-system-1.1.0-SNAPSHOT.jar
