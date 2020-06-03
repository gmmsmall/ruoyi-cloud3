FROM java:8
VOLUME /tmp
ADD ./ruoyi-service/ruoyi-fabric/target/ruoyi-fabric-1.1.0-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ENV TZ=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
EXPOSE 9999
CMD ["-jar", "/app.jar"]
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xms256m", "-Xmx400m",  "-XX:PermSize=64m", "-XX:MaxPermSize=128m", "-Xss512k"]