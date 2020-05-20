install:
	mvn compile flyway:migrate
	mvn compile

serve:
	. .env
	mvn compile jetty:run
