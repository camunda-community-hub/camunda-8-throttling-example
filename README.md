[![Community Extension](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)
[![](https://img.shields.io/badge/Lifecycle-Incubating-blue)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#incubating-)

# Throttling example for Camunda Platform 8

We've had some customers who would like to be able to throttle process instances.

The goal of this project is to show a naive but working solution to achieve it.

## Repository content

This repository contains a Java application template for Camunda Platform 8 using Spring Boot
and a [docker-compose.yaml](docker-compose.yaml) file for local development. For production setups we recommend to use our [helm charts](https://docs.camunda.io/docs/self-managed/platform-deployment/kubernetes-helm/).

- [Documentation](https://docs.camunda.io)
- [Camunda Platform SaaS](https://camunda.io)
- [Getting Started Guide](https://github.com/camunda/camunda-platform-get-started)
- [Releases](https://github.com/camunda/camunda-platform/releases)
- [Helm Charts](https://helm.camunda.io/)
- [Zeebe Workflow Engine](https://github.com/camunda/zeebe)
- [Contact](https://docs.camunda.io/contact/)

## First steps with the application

The application requires a running Zeebe engine. You can run Zeebe locally using the instructions below for Docker Compose or have a look at our [recommended deployment options for Camunda Platform](https://docs.camunda.io/docs/self-managed/platform-deployment/#deployment-recommendation.).

Run the application via
```
./mvnw spring-boot:run
```

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Using docker-compose

> :information_source: The docker-compose file in this repository uses the latest [compose specification](https://docs.docker.com/compose/compose-file/), which was introduced with docker-compose version 1.27.0+. Please make sure to use an up-to-date docker-compose version.

> :information_source: The Docker required is 20.10.16+

To stand-up a complete Camunda Platform 8 Self-Managed environment locally the [docker-compose.yaml](docker-compose.yaml) file in this repository can be used.

The full enviornment contains these components:
- Zeebe
- Operate
- Tasklist
- Optimize
- Identity
- Elasticsearch
- KeyCloak

Clone this repo and issue the following command to start your environment:

```
docker-compose up -d
```

Wait a few minutes for the environment to start up and settle down. Monitor the logs, especially the Keycloak container log, to ensure the components have started.

Now you can navigate to the different web apps and log in with the user `demo` and password `demo`:
- Operate: [http://localhost:8081](http://localhost:8081)
- Tasklist: [http://localhost:8082](http://localhost:8082)
- Optimize: [http://localhost:8083](http://localhost:8083)
- Identity: [http://localhost:8084](http://localhost:8084)
- Elasticsearch: [http://localhost:9200](http://localhost:9200)
- KeyCloak: [http://localhost:18080](http://localhost:18080)

The workflow engine Zeebe is available using gRPC at `localhost:26500`.

To tear down the whole environment run the following command

```
docker-compose down -v
```
