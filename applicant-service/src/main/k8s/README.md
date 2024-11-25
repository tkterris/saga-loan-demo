# Kube Build / Deploy Instructions

## Building Container

### Build Code First

in `src/main/java` run `mvn package` (you must have mvn and a JDK installed locally)

### Prepare Docker build

Due to the structure of this repo you will need to pull in the jar file to build locally:

```console
# cd into this directory
mkdir target
cp ../../../target/applicantService-0.0.1-SNAPSHOT.jar target/
```

Make sure you a logged into the redhat registry `podman login ...`

### Running the Container build

The Following is with `podman` but you can substitute `docker` as well

```console
# Build the container
podman build -t saga-applicant-service:latest -f Dockerfile.openjdk17 .

# Test the container locally
podman run -p 8083:8083 localhost/saga-applicant-service:latest
```

Hit the local server [http://localhost:8083/sagaloandemo/applicant/](http://localhost:8083/sagaloandemo/applicant/)

Something like this should show:

```json
{
  "_links" : {
    "profile" : {
      "href" : "http://localhost:8083/sagaloandemo/applicant/profile"
    }
  }
}
```

## Push the Container to a registry

Make sure you are logged into your registry of choice: `podman login ...`

```console
podman push localhost/saga-applicant-service:latest some-registry/folder/folder/saga-applicant-service:latest
```

## Deploying to Openshift

### Create pull secret

You are going to need to figure out your registry pull secret you pushed the container into and copy the data into a yaml file

my-pull-secret.yaml:

```yaml
apiVersion: v1
data:
  .dockerconfigjson: ?????
kind: Secret
metadata:
  name: my-pull-secret
type: kubernetes.io/dockerconfigjson
```

apply the secret:

```console
oc apply -f my-pull-secret.yaml
```

### Prepare the deployment

Depending on where your image lives you will need to edit this line of `deployment.yaml`, it needs to be your image URL

```yaml
   spec:
      containers:
      - image: quay.io/bwheatley/saga/saga-applicant-service:latest
```

### Apply the Yamls

```console
oc apply -f deployment.yaml
oc apply -f service.yaml
oc apply -f route.yaml
```

## Verify Deployment

Wait for the pod to come up and ensure it's in `Running` status

```console
oc get pods --selector app=saga-applicant-service
```

Check the route:

```console
oc get routes --selector app=saga-applicant-service
```

Copy the `HOST/PORT` in your browser and add `/sagaloandemo/applicant/` to the end of the URL

Something like this should show:

```json
{
  "_links" : {
    "profile" : {
      "href" : "http://localhost:8083/sagaloandemo/applicant/profile"
    }
  }
}
```