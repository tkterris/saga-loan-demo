# saga-loan-demo
Saga loan demo - OpenAPI + Java + Camel

This application demonstrates the Saga EIP, and can be deployed locally, in vanilla Kubernetes, or in OpenShift.

## Building 

The application must be built locally, regardless of the deployment location.

### Local Build

Run a Maven build from the project root:

```
mvn clean install
```

### Container Images

Complete this section if you're deploying to Kubernetes or OpenShift. First, log in to your application 
registry and `registry.redhat.io`:

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

# Optional, if you want to use the custom LRA coordinator
podman build -f custom-lra-coordinator/Dockerfile-lra-coordinator.jvm -t lra-coordinator
podman push lra-coordinator $REG/$REG_PATH/lra-coordinator
```

## Deploying

First, check the `images` section of `k8s/base/kustomization.yaml` to ensure the correct registry locations and tags are being used. 
This includes the five application images, the LRA coordinator image, and the PostGres database image. 

Then, generate a JKS keystore for API gateway:

```
export TLS_KEYSTORE_PASSWORD=sagademo
mkdir -p sagaApiGateway/ssl
rm ./sagaApiGateway/ssl/sagademo.p12
keytool -genkeypair -storetype PKCS12 \
    -alias sagademo -keyalg RSA -keysize 4096 -validity 365 \
    -keystore ./sagaApiGateway/ssl/sagademo.p12 -dname "CN=sagademo" \
    -ext "SAN=DNS:*.sagademo.com" \
    -keypass $TLS_KEYSTORE_PASSWORD -storepass $TLS_KEYSTORE_PASSWORD
```

### Local

Follow the instructions to set up the PostGres database in `local/run-local.sh`, then run that script:

```
local/run-local.sh
```

### Kubernetes

Create the Kubernetes namespace and switch to that context:

```
kubectl apply -f k8s/saga-loan-demo-namespace.yaml
kubectl config set-context --current --namespace=saga-loan-demo
```

If you're using non-public registries, configure a pull secret with the name `quayio`. For example:

```
kubectl create -f pullsecret.yml --namespace=saga-loan-demo
```

Create the Secret for the API gateway TLS keystore, as well as a ConfigMap containing the PostGres
DB startup scripts:

```
kubectl create secret generic api-gateway-cert --from-file=sagademo.p12=./sagaApiGateway/ssl/sagademo.p12 \
    --from-literal=TLS_KEYSTORE_PASSWORD=$TLS_KEYSTORE_PASSWORD
kubectl create secret generic init-db-scripts --from-file=01-user.sql=./loan-demo-model/src/main/resources/sql/postgresql/user.sql \
    --from-file=02-schema.sql=./loan-demo-model/src/main/resources/sql/postgresql/schema.sql \
    --from-file=03-data.sql=./loan-demo-model/src/main/resources/sql/postgresql/data.sql
```

Then, deploy the application:

```
kubectl apply -k k8s/base
kubectl apply -f k8s/api-gateway-ingress.yaml
```

### OpenShift

Create the project:

```
oc new-project saga-loan-demo
```

If you're using non-public registries, configure a pull secret with the name `quayio`. For example:

```
oc create -f pullsecret.yml --namespace=saga-loan-demo
```

Create the Secret for the API gateway TLS keystore, as well as a ConfigMap containing the PostGres
DB startup scripts:

```
oc create secret generic api-gateway-cert --from-file=sagademo.p12=./sagaApiGateway/ssl/sagademo.p12 \
    --from-literal=TLS_KEYSTORE_PASSWORD=$TLS_KEYSTORE_PASSWORD
oc create secret generic init-db-scripts --from-file=01-user.sql=./loan-demo-model/src/main/resources/sql/postgresql/user.sql \
    --from-file=02-schema.sql=./loan-demo-model/src/main/resources/sql/postgresql/schema.sql \
    --from-file=03-data.sql=./loan-demo-model/src/main/resources/sql/postgresql/data.sql
```

Then, deploy the application:

```
oc apply -k k8s/base
oc create route passthrough api-gateway --service=api-gateway
```

## Testing

### Happy Path

Requests can be sent to the api-gateway application to test Saga functionality. The following command should work, ensuring that `API_GATEWAY_HOST` matches the local hostname and context, the Kuberntes Ingress hostname, or the OpenShift Route hostname:

```
export API_GATEWAY_HOST=api-gateway-saga-loan-demo.apps-crc.testing
curl -k -X POST -d '{"id":1,"amount":50.00,"applicantId":1,"loanRequestDate":"2023-12-15T13:27:01","approved":false}' -H "Content-Type: application/json" https://$API_GATEWAY_HOST/redirect
```

We can see that it completes as expected:

```
2024-12-03 21:37:06,910 INFO  route1 : invoked addLoan endpoint...
2024-12-03 21:37:06,910 INFO  com.acme.saga.SagaRoute : loan request id: 1
create-loan  | 2024-12-03 21:37:06,910 INFO  com.acme.saga.SagaRoute : loan->id: 1, amount: 50.00, applicantId: 1, approved: false, loanRequestDate: Fri Dec 15 13:27:01 UTC 2023
2024-12-03 21:37:06,988 INFO  route1 : loan endpoint is: http://loan-service:8080/createloan
create-loan  | 2024-12-03 21:37:08,638 INFO  com.acme.saga.SagaRoute : createLoan DTO response: CreateLoanResponseDTO(loanId=1, requestAmount=50.00, applicantId=1, loanCreationDate=Tue Dec 03 21:37:08 UTC 2024, approved=null, comment=null - successfully added...)
2024-12-03 21:37:08,639 INFO  com.acme.saga.SagaRoute : new loan id: 1 with reference loan id: 1 added to inprocess cache...
2024-12-03 21:37:08,641 INFO  route1 : Loan added...
create-loan  | 2024-12-03 21:37:08,641 INFO  route3 : invoked updateLoanLimit endpoint...
create-loan  | 2024-12-03 21:37:08,641 INFO  com.acme.saga.SagaRoute : loan request id: 1
2024-12-03 21:37:08,641 INFO  com.acme.saga.SagaRoute : loan->id: 1, amount: 50.00, applicantId: 1, approved: false, loanRequestDate: Fri Dec 15 13:27:01 UTC 2023
create-loan  | 2024-12-03 21:37:08,642 INFO  route3 : updateLoanLimit endpoint is: http://applicant-service:8080/updateloanlimit
2024-12-03 21:37:09,413 INFO  route3 : invoked updateLoanLimit...
create-loan  | 2024-12-03 21:37:09,623 INFO  route4 : Saga loan process has completed...
```

### Saga Compensation

In order to test Saga compensation, we can simulate one of the services being down. Submit the same request as in the happy path:

```
export API_GATEWAY_HOST=api-gateway-saga-loan-demo.apps-crc.testing
curl -k -X POST -d '{"id":1,"amount":50.00,"applicantId":1,"loanRequestDate":"2023-12-15T13:27:01","approved":false}' -H "Content-Type: application/json" https://$API_GATEWAY_HOST/redirect
```

However, before it completes (there is a 60 second pause) delete the `applicant-service` Deployment:

```
# Kubernetes
kubectl delete deployment applicant-service
```
```
# OpenShift
oc delete deployment applicant-service 
```

We then hit the `deleteLoan` Saga compensation step, as expected:

```
create-loan  | 2024-12-03 21:37:19,352 INFO  route2 : invoking deleteLoan...
create-loan  | 2024-12-03 21:37:19,353 INFO  com.acme.saga.SagaRoute : Deleting loan with id: 1
```

### LRA Coordinator Failure

If the LRA Coordinator fails, the LRA will fail. However, Saga guarantees LRA compensation with eventual 
consistency. That is, the LRA compensation will eventually occur if a timeout is configured (see `SagaRoute`). 

To test this, submit a request:

```
export API_GATEWAY_HOST=api-gateway-saga-loan-demo.apps-crc.testing
curl -k -X POST -d '{"id":1,"amount":50.00,"applicantId":1,"loanRequestDate":"2023-12-15T13:27:01","approved":false}' -H "Content-Type: application/json" https://$API_GATEWAY_HOST/redirect
```

Before it completes, delete the `lra-coordinator` Deployment:

```
# Kubernetes
kubectl delete deployment lra-coordinator
```
```
# OpenShift
oc delete deployment lra-coordinator
```

Then, after a few minutes, recreate it by applying the Kustomize template:

```
# Kubernetes
kubectl apply -k k8s/base
```
```
# OpenShift
oc apply -k k8s/base
```

Eventually, the LRA will be compensated, as expected.

## Cleanup

Local:

```
local/stop-local.sh
```

In Kubernetes:

```
kubectl delete namespace saga-loan-demo
```

In OpenShift:

```
oc delete project saga-loan-demo
```
