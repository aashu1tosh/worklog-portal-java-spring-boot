runDev:
	mvn clean spring-boot:run

runDebugDev:
	mvn clean spring-boot:run -e

runCleanInstallDev:
	mvn clean install spring-boot:run -e

seedSudoAdmin:
	mvn clean spring-boot:run -e -Dspring-boot.run.profiles=seed
