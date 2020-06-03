FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jiangxd@mingbyte.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /ruoyi-fabric

WORKDIR /ruoyi-fabric

EXPOSE 9999

ADD ./target/ruoyi-fabric-1.1.0-SNAPSHOT.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar ruoyi-fabric-1.1.0-SNAPSHOT.jar
