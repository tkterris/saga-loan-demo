# saga-loan-demo-manifests
k8s manifests for the saga-loan-demo application

## Setup

First, create the Kubernetes namespace and switch to that context:

```
kubectl apply -f k8s/manifests/base/saga-loan-demo-namespace.yaml
kubectl config set-context --current --namespace=saga-loan-demo
```

If you're using non-public registries, configure a pull secret with the name `quayio`. For example:

```
kubectl create -f pullsecret.yml --namespace=NAMESPACEHERE
```

Check the `images` section of `k8s/manifests/base/kustomization.yaml` to ensure the correct registry locations and tags are being used. 
This includes the five application images, the LRA coordinator image, and the PostGres database image.

## Deployment

Run the following command to apply the configuration:

```
kubectl apply -k k8s/manifests/base
```

## Testing

### Happy Path

From within the cluster, requests can be sent to the create-loan application to test Saga functionality. The following command should work:

```
curl -X POST -d '{"id":1,"amount":50.00,"applicantId":1,"loanRequestDate":"2023-12-15T13:27:01","approved":false}' -H "Content-Type: application/json" http://create-loan:8080/saga/route
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

With an invalid request, we get Saga compensation, as expected. When this command is tried:

```
# invalid, missing ID
curl -X POST -d '{"id":12345,"amount":50.00,"applicantId":1,"loanRequestDate":"2023-12-15T13:27:01","approved":false}' -H "Content-Type: application/json" http://create-loan:8080/saga/route
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

