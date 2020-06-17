install:
	mvn compile flyway:migrate
	mvn compile

serve:
	mvn compile jetty:run
