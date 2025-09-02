FROM tomcat:11.0-jre21
COPY ./target/hospital-management-system-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
