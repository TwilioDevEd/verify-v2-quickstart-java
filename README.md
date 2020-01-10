> We are currently in the process of updating this sample template. If you are encountering any issues with the sample, please open an issue at [github.com/twilio-labs/code-exchange/issues](https://github.com/twilio-labs/code-exchange/issues) and we'll try to help you.


This application example demonstrates how to Simple phone verification with Java,
Servlets, and Twilio Verify.

## Local Development

1. First clone this repository and `cd`into it.

   ```bash
   $ git clone git@github.com:TwilioDevEd/verify-v2-quickstart-java.git
   $ cd verify-v2-quickstart-java
   ```

1. Copy the sample configuration file and edit it to match your configuration.

    ```bash
    $ cp .env.example .env
    ```

   You'll need to set your `TWILIO_ACCOUNT_SID` and `TWILIO_AUTH_TOKEN` from the
   [Twilio Console](https://www.twilio.com/console). Also create a new verification
    service [here](https://www.twilio.com/console/verify/services) and set the 
    service `VERIFICATION_SID` in your `.env`.

   Once you have populated all the values, load the variables with `source`.

    ```bash
    $ source .env
    ```

    _If you are using a different operating system, make sure that all the variables
     from the `.env` file are loaded into your environment._

1. Execute the migrations.

   ```bash
   $ mvn compile flyway:migrate
   ```

1. Run the application.
   ```bash
   $ mvn compile jetty:run
   ```

1. Check it out at [http://localhost:8080/](http://localhost:8080/)


## Run the tests

```
mvn clean test
```

## Meta

* No warranty expressed or implied. Software is as is. Diggity.
* [MIT License](http://www.opensource.org/licenses/mit-license.html)
* Lovingly crafted by Twilio Developer Education.
