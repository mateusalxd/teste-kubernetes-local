# Instalações

- Kubernetes (minikube) - <https://minikube.sigs.k8s.io/docs/start/>
- Extensão do Kubernetes para VSCode - <https://marketplace.visualstudio.com/items?itemName=ms-kubernetes-tools.vscode-kubernetes-tools>
- Kubectl - <https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/>
- Helm - <https://helm.sh/docs/intro/install/>
- ArgoCD - <https://argo-cd.readthedocs.io/en/stable/cli_installation/>

## Ferramentas/Sistemas

### minikube

#### comandos

Definir o driver padrão ([veja mais](https://minikube.sigs.k8s.io/docs/drivers/))

```bash
minikube config set driver docker
```

Iniciar cluster kubernetes

```bash
minikube start
```

Finalizar cluster kubernetes

```bash
minikube stop
```

Subir site para gerenciamento

```bash
minikube dashboard
```

## Passo a passo

```bash
minikube config set driver docker
minikube start

minikube dashboard # execute em um terminal separado

# Somente exemplo de enviar imagem para o registry local
docker run -d -p 5000:5000 --restart=always --name registry registry:2
docker pull hello-world
docker tag hello-world:latest localhost:5000/hello-world:latest
docker push localhost:5000/hello-world:latest

kubectl config get-contexts
kubectl config current-context

kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "LoadBalancer"}}'
kubectl port-forward svc/argocd-server -n argocd 8080:443 # execute em um terminal separado
# acesse https://localhost:8080/ com usuário admin e com a senha recuperada com o comando abaixo
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d; echo

cd charts
helm create teste1-chart

argocd login localhost:8080

argocd app create teste1 \
--repo https://github.com/mateusalxd/teste-kubernetes-local.git \
--path charts/teste1-chart \
--dest-server https://kubernetes.default.svc \
--dest-namespace local \
--sync-policy auto \
--sync-option CreateNamespace=true

docker pull bitnami/zookeeper:3.7.1
minikube image load bitnami/zookeeper:3.7.1

docker pull bitnami/kafka:3.1.1
minikube image load bitnami/kafka:3.1.1

docker pull provectuslabs/kafka-ui:latest
minikube image load provectuslabs/kafka-ui:latest

kubectl port-forward deployments/kafka-ui -n kafka-local 8081:8080
```

## Links úteis

- [Criar um registro de imagens localmente](https://docs.docker.com/registry/deploying/)
- [Iniciar com ArgoCD](https://argo-cd.readthedocs.io/en/stable/getting_started/)
- [Organizando o acesso ao cluster usando arquivos kubeconfig](https://kubernetes.io/pt-br/docs/concepts/configuration/organize-cluster-access-kubeconfig/)
- [Como criar um helm chart](https://phoenixnap.com/kb/create-helm-chart)
- [Informar diretório local como source no ArgoCD](https://github.com/argoproj/argo-cd/issues/3432)
- [Configurações gerais (bastante coisa aqui)](https://robertbrem.github.io/Microservices_with_Kubernetes/01_Setup/01_Host_setup/)
- [Rodar Kafka no minikube](https://technology.amis.nl/platform/kubernetes/running-apache-kafka-on-minikube/)
