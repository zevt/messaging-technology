### Create Jar: 
#### mvn clean install -DskipTests 

### Run Application on port 9002 
mvn spring-boot:run -Dspring-boot.run.profiles=generic,local

mvn spring-boot:run -Dspring-boot.run.profiles=generic,local -Dspring-boot.run.arguments="--server.port=9002"



### Run Application on port 9003 
mvn spring-boot:run -Dspring-boot.run.profiles=generic,local -Dspring-boot.run.arguments="--server.port=9003"

java -jar -Dspring.profiles.active=generic,local XXX.jar