runDev:
	mvn clean spring-boot:run -e

seedSudoAdmin:
	mvn clean spring-boot:run -e -Dspring-boot.run.profiles=seed