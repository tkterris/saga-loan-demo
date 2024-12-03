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

