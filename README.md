# Interventions Catalogue Service - ALPHA

[![CircleCI](https://circleci.com/gh/ministryofjustice/interventions-catalogue-service/tree/master.svg?style=svg)](https://circleci.com/gh/ministryofjustice/interventions-catalogue-service)
[![Known Vulnerabilities](https://snyk.io/test/github/ministryofjustice/interventions-catalogue-service/badge.svg)](https://snyk.io/test/github/ministryofjustice/interventions-catalogue-service)

This is a Spring Boot application that provides tha basis for the Interventions Catalogue.

Interventions are persisted in a PostgreSQL database, and the application is secured using DPS auth and OAuth Connect.

Currently 3 basic "types" of information are supported
- Providers
- Intervention Types
- Interventions Sub Types

These can be created/updated/deleted via a REST API

Previous versions of entities are stored using [Hibernate Envers](https://hibernate.org/orm/envers/)

When an entity is changed, a AVRO message in JSON format is placed onto an SNS topic. Queues fed off this can then distribute this information to downstream systems.

### Building

```bash
./gradlew build
```

### Running
Use the docker-compose file in the `interventions-catalogue-ui` project to start the other needed components such as PostgreSQL and localstack.

If you would like events to be created when data changes are made, enable `interventions.snsEnabled` within the configuration.

Then run the below commands to create the SNS topics and SQS queues as needed on localstack.

```
aws --endpoint-url=http://localhost:4575 sns create-topic --name interventions_event

aws --endpoint-url=http://localhost:4576 sqs create-queue --queue-name delius_event_queue

aws --endpoint-url=http://localhost:4575 sns subscribe \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:interventions_event \
    --protocol sqs \
    --notification-endpoint http://localhost:4576/queue/delius_event_queue 

aws --endpoint-url=http://localhost:4576 sqs receive-message --queue-url http://localhost:4576/queue/delius_event_queue
```

Finally the application can be started:

```bash
./gradlew bootRun
```

See the `interventions-catalogue-delius-consumer` project for an example of something consuming these events from the SQS queue.

### REST Endpoints

Tha application endpoints are documented with Swagger.

The Swagger UI is available at http://localhost:8080/swagger-ui.html when the application is running.

Endpoints are secured with HMPPS Auth/OAuth Connect

#### Health

- `/health/ping`: will respond with status `UP` to all requests.  This should be used by dependent systems to check connectivity to interventions-catalogue-service,
rather than calling the `/health` endpoint.
- `/health`: provides information about the application health and its dependencies. 
- `/info`: provides information about the version of deployed application.
