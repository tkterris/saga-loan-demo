# saga-loan-demo
Saga loan demo - OpenAPI + Java + Camel

## Building 

### Local Build

Run a Maven build from the project root:

```
mvn clean install
```

### Creating Container Images

First, log in to your application registry and `registry.redhat.io`:

```
export REG=quay.io
export REG_PATH=tterris/saga-demo

podman login registry.redhat.io
podman login $REG
```

After building locally, run the following commands to create container images and push them to your registry.

```
podman build -f api-gateway/Dockerfile.openjdk17 -t api-gateway
podman push api-gateway $REG/$REG_PATH/api-gateway

podman build -f applicant-service/Dockerfile.openjdk17 -t applicant-service
podman push applicant-service $REG/$REG_PATH/applicant-service

podman build -f create-loan/Dockerfile.openjdk17 -t create-loan
podman push create-loan $REG/$REG_PATH/create-loan

podman build -f loan-demo-model/Dockerfile.openjdk17 -t loan-demo-model
podman push loan-demo-model $REG/$REG_PATH/loan-demo-model

podman build -f loan-service/Dockerfile.openjdk17 -t loan-service
podman push loan-service $REG/$REG_PATH/loan-service
```
