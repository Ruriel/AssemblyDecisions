# Getting Started
### Introduction
This is a simple Spring Boot application that simulates voting sessions.

### Environment Variables
You can change the values of the .env file to run the application in a different context or port.
The variables are:

    POSTGRES_HOST: Address where the Postgres instance is running. Defaults to localhost.
    POSTGRES_DB: Name of the database. Defaults to assemblydb.
    POSTGRES_USER: Postgres username. Defaults to postgres.
    POSTGRES_PASSWORD: Postgres password. Defaults to postgres.
    POSTGRES_INTERNAL_PORT: Port to be used within the container. Defaults to 5432.
    POSTGRES_EXTERNAL_PORT: Port that will be used outside the container. Defaults to 5432.
    SERVER_EXTERNAL_PORT: Port where the application will run. Defaults to 8081.
    SERVER_INTERNAL_PORT: Port where the application runs internally. Defaults to 8081.
    CONTEXT_PATH: Base URL for each endpoint. Defaults to /api.
    TZ: Timezone to be used by the application. Defaults to America/Sao_Paulo.

### Requirements
    Java 17.0.6
    Docker 20.10.22 (for production only)
    Postgres 15.2 or compatible
### How to run
To run the application in production mode, you have to run the following command:
```
docker-compose up -d
```
Be aware it will create a data folder in the root of the project. If you're using Windows, be sure to run docker-compose 
using Windows Powershell.

If you already have a PostgresSQL instance running outside Docker, you can run docker-compose-localdatabase instead.
```
docker-compose -f docker-compose-localdatabase.yaml up -d
```
Just be aware to fill all POSTGRES environment variables.

For development mode, just import the application on Intellij or Eclipse as a Gradle Project.
It will use H2 as database.

### Reference Documentation
For further reference on the application (in PT-BR), refer to the file documentation/Observações sobre a API.pdf file.

Details about the REST API can be found on the swagger.json file.