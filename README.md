
# user-service
User Microservices to be used by Allioc UI

### To run the application in local
`java -Dspring.profiles.active=local -jar build/libs/user-service-0.0.1-SNAPSHOT.jar`

### To run the application in dev.
`java -Dspring.profiles.active=dev -DMONGO_USER=allioc_dev_db_user_2 -DMONGO_PASSWORD=FYL8BuEq95T2kTy5 -DATLAS_ADDRESS=cluster0.btdhb.mongodb.net -DDATABASE=Allioc -jar build/libs/user-service-0.0.1-SNAPSHOT.jar`

### To run the application in dev

### TODO
Setup Jenkins

### Dev Pipeline
Using Github workflows configuration to push to GCR.

### MongoDB Database
Setup MongoDB Collection for Tenant

### Container Registry configuration

### Kube Config

### Enabling the service on load balancer or API Gateway

