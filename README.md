# Interventions Catalogue Service

[![CircleCI](https://circleci.com/gh/ministryofjustice/interventions-catalogue-service/tree/master.svg?style=svg)](https://circleci.com/gh/ministryofjustice/interventions-catalogue-service)
[![Known Vulnerabilities](https://snyk.io/test/github/ministryofjustice/interventions-catalogue-service/badge.svg)](https://snyk.io/test/github/ministryofjustice/interventions-catalogue-service)
 
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
