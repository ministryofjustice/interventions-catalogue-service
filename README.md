# Interventions Catalogue Service

[![CircleCI](https://circleci.com/gh/ministryofjustice/interventions-catalogue-service/tree/master.svg?style=svg)](https://circleci.com/gh/ministryofjustice/interventions-catalogue-service)
[![Known Vulnerabilities](https://snyk.io/test/github/ministryofjustice/interventions-catalogue-service/badge.svg)](https://snyk.io/test/github/ministryofjustice/interventions-catalogue-service)

This is a Spring Boot application that provides tha basis for the Interventions Catalogue.

Interventions are persisted in a PostgreSQL database, and the application is secured using DPS auth and OAuth Connect.


### Building

```bash
./gradlew build
```

### Running

```bash
./gradlew bootRun
```

#### Health

- `/health/ping`: will respond with status `UP` to all requests.  This should be used by dependent systems to check connectivity to interventions-catalogue-service,
rather than calling the `/health` endpoint.
- `/health`: provides information about the application health and its dependencies. 
- `/info`: provides information about the version of deployed application.

#### Local stack

Create topic

```
aws --endpoint-url=http://localhost:4575 sns create-topic --name interventions_event

aws --endpoint-url=http://localhost:4576 sqs create-queue --queue-name delius_event_queue

aws --endpoint-url=http://localhost:4575 sns subscribe \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:interventions_event \
    --protocol sqs \
    --notification-endpoint http://localhost:4576/queue/delius_event_queue 

aws --endpoint-url=http://localhost:4576 sqs receive-message --queue-url http://localhost:4576/queue/delius_event_queue
```
