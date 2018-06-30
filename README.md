**Spring Boot and RHOAR - Leave Vacation Sample App**

A sample app to manage leave vacation requests for employees, developed with Spring Boot and Openshift RHOAR.

The app is composed by 2 microservices:
 - employee service
 - sick requests service

The employee service communicates with sick requests service in order to send specific employee's leave requests

Both microservices use a dedicated postgres database to persist their data.


**Test the application locally from a web browser**

Execute the maven command for each microservices (go to the specific microservice root folder):

```bash
mvn spring-boot:run
```

These endpoints are available for employee microservice (listen on port 8080):
 - list employees with leave list

 ```
 [GET] http://localhost:8080/api/employees?page=<page_number>&pageSize=<page_size>
 ```

These endpoints are available for sick requests microservice (listen on port 8090):

  - send sick request

  ```
  [PUT] http://localhost:8090/api/sickrequests

  body example:

  {
  	"employeeId": "1",
  	"dateRequested": "2018-10-21"
  }

  ```


**Execute the test locally**

Execute the maven command for each microservices (go to the specific microservice root folder):

```bash
mvn verify
```


**Create project and apps on OpenShift**

```bash
oc login -u developer -p developer

#Create OCP project
oc new-project leave_vacation --display-name="Leave Vacation App"

#Create database for employee service

oc new-app -e POSTGRESQL_USER=luke -e POSTGRESQL_PASSWORD=secret -e POSTGRESQL_DATABASE=my_data openshift/postgresql-92-centos7 --name=my-database

#Create database for sick requests service

oc new-app -e POSTGRESQL_USER=luke -e POSTGRESQL_PASSWORD=secret -e POSTGRESQL_DATABASE=my_data openshift/postgresql-92-centos7 --name=my-database-sickrequests
```


**OpenShift resources**

OCP resources are inside the folder src/main/fabric8:
 - credentials-secret.yml --> create a secret to be used for postgres username and password
 - deployment.yml --> define the deployment configuration
 - route.yml --> create a route for external communication


**Deploy on OpenShift**

Deploy on OCP will use the fabric8 maven plugin and maven profile openshift.

OCP resources inside the folder src/main/fabric8 will be created in OCP project leave_vacation.

Execute the maven command for each microservices (go to the specific microservice root folder):

```bash
mvn package fabric8:deploy -Popenshift -DskipTests
```


**Health check**

An health check can detect when our application is responding correctly.
Spring Boot provides a feature for this called Actuator, which exposes health data under the path /health.
Just add it to your pom.xml

```
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
