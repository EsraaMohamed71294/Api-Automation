FROM maven
WORKDIR src/test/java/TestReports 
COPY src/test/java/TestReports .
RUN ls
RUN mvn clean install
RUN mvn test