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
```

## Deploying

First, check the `images` section of `k8s/base/kustomization.yaml` to ensure the correct registry locations and tags are being used. 
This includes the five application images, the LRA coordinator image, and the PostGres database image. 

### Local

Follow the instructions to set up the PostGres database in `local/run-local.sh`, then run that script:

```
local/run-local.sh
```

### Kubernetes

Create the Kubernetes namespace and switch to that context:

```
kubectl apply -f k8s/base/saga-loan-demo-namespace.yaml
kubectl config set-context --current --namespace=saga-loan-demo
```

If you're using non-public registries, configure a pull secret with the name `quayio`. For example:

```
kubectl create -f pullsecret.yml --namespace=saga-loan-demo
```

Then, deploy the application:

```
kubectl apply -k k8s/base
kubectl apply -f k8s/base/api-gateway-ingress.yaml
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

With an invalid request, we get Saga compensation, as expected. When this command is tried (again, with the correct value of `API_GATEWAY_HOST`):

```
export API_GATEWAY_HOST=api-gateway-saga-loan-demo.apps-crc.testing
# invalid, missing ID
curl -k -X POST -d '{"id":12345,"amount":50.00,"applicantId":1,"loanRequestDate":"2023-12-15T13:27:01","approved":false}' -H "Content-Type: application/json" https://$API_GATEWAY_HOST/redirect
```

We then hit the `deleteLoan` Saga compensation step:

```
create-loan  | 2024-12-03 21:37:19,218 INFO  com.acme.saga.SagaRoute : loan->id: 12345, amount: 50.00, applicantId: 1, approved: false, loanRequestDate: Fri Dec 15 13:27:01 UTC 2023
2024-12-03 21:37:19,222 INFO  route1 : loan endpoint is: http://loan-service:8080/createloan
create-loan  | 2024-12-03 21:37:19,334 ERROR org.apache.camel.processor.errorhandler.DefaultErrorHandler : Failed delivery for (MessageId: 629F1B6FD238177-0000000000000001 on ExchangeId: 629F1B6FD238177-0000000000000001). Exhausted after delivery attempt: 1 caught: org.apache.camel.http.base.HttpOperationFailedException: HTTP operation failed invoking http://loan-service:8080/createloan with statusCode: 304
....
create-loan  | 2024-12-03 21:37:19,352 INFO  route2 : invoking deleteLoan...
create-loan  | 2024-12-03 21:37:19,353 INFO  com.acme.saga.SagaRoute : Deleting loan with id: 12345
```

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
