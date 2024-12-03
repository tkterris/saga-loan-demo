# saga-loan-demo-manifests
k8s manifests for the saga-loan-demo application

#If using Minikube
minikube config set rootless true
minikube start --driver=podman --container-runtime=containerd

#Get Secret YAML from the REG, e.g. "Robot Accounts" in Quay.io
kubectl create -f pullsecret.yml --namespace=NAMESPACEHERE

kubectl apply -f k8s/manifests/base/saga-loan-demo-namespace.yaml
kubectl config set-context --current --namespace=saga-loan-demo
kubectl apply -k k8s/manifests/base
