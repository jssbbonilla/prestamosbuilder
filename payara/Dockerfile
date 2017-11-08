FROM payara/server-full

ENV JDBC_URL=https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.0.8.zip  
ENV JDBCL_PKG=mysql-connector-java-5.0.8.zip
ADD pass.txt /pass.txt
ADD mysql-connector-java-5.0.8-bin.jar /opt/payara41/glassfish/lib/mysql-connector-java-5.0.8-bin.jar
COPY ./wait-for-mariadb.sh ./
COPY ./sistema-prestamos_v3-1.0-SNAPSHOT.war /opt/payara41/glassfish/domains/domain1/autodeploy/
RUN /opt/payara41/bin/asadmin start-domain







