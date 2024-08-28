# Notification Service API

## Description
The repository contains API for subscription management for [OSS Common Notification Service](https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.oss.common.service/eric-notification-service). NS provides event notifications to subscribed external users.

Notification Service API includes subscription API resources and contract tests for them.

**OpenAPI specification** can be found [here](/src/v1).

**Contact tests** can be found [here](/src/test).

**Contact tests stubs** can be found [here](https://arm1s11-eiffel052.eiffel.gic.ericsson.se:8443/nexus/content/repositories/eo-releases).

### Technologies
NS API is created with:
 - Java 11
 - Spring Boot
 - Spring Cloud Contract
 - OpenAPI (Swagger)
 - Maven 3.6.3

## Usage

### API updates

1. Make updates in `notification-service-openapi.yaml`
2. Run `mvn clean install`. API resources (Java classes in /target folder, OpenAPI files in /v1 folder) will be re-generated during building.
3. Make sure that you include re-generated OpenAPI files from /v1 in a commit with updates to avoid inconsistency

### Contract tests updates
1. Make updates in contacts or contacts base
2. Run `mvn test-compile` to apply changes
3. Run `mvn clean install` to verify that contract tests are ok
4. Download a stubs runner `wget -O stub-runner.jar 'https://search.maven.org/remote_content?g=org.springframework.cloud&a=spring-cloud-contract-stub-runner-boot&v=2.2.4.RELEASE'`
5. Run a stub `java -jar stub-runner.jar --stubrunner.ids=com.ericsson.oss.common.service:eric-oss-notification-service-api:<VERSION_FROM_POM>:<ANY_PORT> -stubrunner.repositoryRoot="~/.m2/repository"  --stubrunner.stubsMode=LOCAL`
6. Send request to the stub and verify that it behaves as expected

or

Run TestApplication.java in test folder.
This will automatically find latest stubs in repository and start the stub runner

## CI Pipeline

[Pre-Code pipeline](https://fem5s11-eiffel052.eiffel.gic.ericsson.se:8443/jenkins/job/notification_service_api_PRE_CODE)

[Release pipeline](https://fem5s11-eiffel052.eiffel.gic.ericsson.se:8443/jenkins/view/Laika/job/notification_service_api_RELEASE/)

New version of API and contract tests stubs is released for each merged PR.

## Contribution Workflow
1. A **contributor** updates the artifact in the local repository and documentation if necessary.
2. A **contributor** pushes the update to Gerrit for review.
3. A **contributor** invites the **Laika (group)** (mandatory) and **other relevant parties** (optional) to the Gerrit review, and makes no further changes to the document until it is reviewed.

## Contact Information
Photon Team ("Team Photon" <pdleowanop@pdl.internal.ericsson.com>)

### PO
Massimo La Rosa (Photon Team) (massimo.larosa@ericsson.com)

#### Team Members - Photon Team

Sabrina Lettere (SM) (sabrina.lettere@ericsson.com)

Francesco Lazzeri (francesco.lazzeri@ericsson.com)

Gianmarco Bruno (gianmarco.bruno@ericsson.com)

Luca Auditore (luca.auditore@ericsson.com)

Mario Molinari (mario.a.molinari@ericsson.com)

Paolo Franceschini (paolo.franceschini@ericsson.com)

Riccardo Pirastru (riccardo.pirastru@ericsson.com)
