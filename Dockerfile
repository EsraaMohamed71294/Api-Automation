FROM maven
COPY . .
RUN ls
RUN mvn clean install
RUN mvn test