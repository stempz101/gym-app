FROM tomcat:9.0.86-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/gym-app-*.war /usr/local/tomcat/webapps/gym-app.war
EXPOSE 8080:8080
ENTRYPOINT ["catalina.sh", "run"]
