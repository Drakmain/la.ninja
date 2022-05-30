#tomcat
FROM tomcat:9-slim

WORKDIR /usr/local/tomcat/webapps/

ADD backend/target/backend-1.0-SNAPSHOT.war .

EXPOSE 8080

CMD ["catalina.sh", "run"]