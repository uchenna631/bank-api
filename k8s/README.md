# bank-api Kubernetes deployment instructions

## 1. Build and tag Docker image
```
docker build -t bank-api:v1 .
```

## 2. Push image to registry (optional for cloud deployment)
```
docker tag bank-api:v1 <your-registry>/bank-api:v1
docker push <your-registry>/bank-api:v1
```

## 3. Create Kubernetes secret for MongoDB URI
```
kubectl apply -f k8s/mongo-secret.yaml
```

## 4. Deploy application and service
```
kubectl apply -f k8s/bank-api-deployment.yaml
```

## 5. Verify deployment
```
kubectl get pods
kubectl get svc
```

## 6. Access application
- Use the ClusterIP service internally, or expose via Ingress/LoadBalancer as needed.

---
**Note:**
- Update `mongo-secret.yaml` with your actual MongoDB connection string.
- Update `bank-api-deployment.yaml` image field if using a remote registry.
- Application health endpoints are available at `/actuator/health`.
