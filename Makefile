runDev:
	mvn spring-boot:run

seedSudoAdmin:
	mvn spring-boot:run -Dspring-boot.run.main-class=com.backend.hrms.SeederApplication