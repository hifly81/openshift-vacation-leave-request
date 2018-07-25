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

  - send sick request from employee

  ```
  [POST] http://localhost:8080/api/employees/sickRequest

   body example:

    {
    	"ssn": "SASAASSA12",
    }
  ```

These endpoints are available for sick requests microservice (listen on port 8090):

  - send sick request

  ```
  [PUT] http://localhost:8090/api/sickrequests

  body example:

  {
  	"employeeId": "SASAASSA12",
  	"dateRequested": "2018-10-21"
  }

  ```


**Execute the test locally**

Execute the maven command for each microservices (go to the specific microservice root folder):

```bash
mvn verify
```


**Create project and apps on OpenShift**

Info to run a local OpenShift cluster:<br>
https://github.com/openshift/origin/blob/master/docs/cluster_up_down.md

Info to install and run Minishift:<br>
https://github.com/minishift/minishift

```bash
oc login -u developer -p developer

#Create OCP project
oc new-project leave-vacation --display-name="Leave Vacation App"

#Create database for employee service

oc new-app -e POSTGRESQL_USER=luke -e POSTGRESQL_PASSWORD=secret -e POSTGRESQL_DATABASE=my_data openshift/postgresql-92-centos7 --name=my-database

#Create database for sick requests service

oc new-app -e POSTGRESQL_USER=luke -e POSTGRESQL_PASSWORD=secret -e POSTGRESQL_DATABASE=my_data openshift/postgresql-92-centos7 --name=my-database-sickrequests
```


**OpenShift resources**

OpenShift resources are inside the folder src/main/fabric8 and they will created during the deployment:
 - credentials-secret.yml --> create a secret to be used for postgres username and password
 - deployment.yml --> define the deployment configuration
 - route.yml --> create a route for external communication


**Deploy on OpenShift**

Deploy on OpenShift will use the fabric8 maven plugin and maven profile openshift.

OpenShift resources inside the folder src/main/fabric8 will be created in OpenShift project leave_vacation.

Application specific properties when the microservice is deployed on OpenShift are in properties file: src/main/resources/application-openshift.properties

Execute the maven command for each microservices (go to the specific microservice root folder):

```bash
mvn package fabric8:deploy -Popenshift -DskipTests
```

Endpoints will be available via OCP routes:

```
#for employee microservice

http://rhoar-employee-microservice-leave-vacation.ocp-cluster_ip
```

```
#for sick requests microservice

http://rhoar-sickrequests-microservice-leave-vacation.ocp-cluster_ip
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

On local web browser are available at:

 ```
 #for employee microservice

 [GET] http://localhost:8080/health
 ```

  ```
  #for sick requests microservice

  [GET] http://localhost:8090/health
  ```

  On OpenShift are available at:

   ```
   #for employee microservice

   [GET] http://rhoar-employee-microservice-leave-vacation.ocp-cluster_ip/health
   ```

    ```
    #for sick requests microservice

    [GET] http://rhoar-sickrequests-microservice-leave-vacation.ocp-cluster_ip/health
    ```

**OpenTracing and Jaeger configuration**

This project uses Jaeger to trace calls to microservices. Jaegar is an OpenTracing implementation. More details are available at:
https://github.com/jaegertracing/jaeger-openshift

In order to use Jaeger on OpenShift, all-in-one templates are available.

The template will install an application with the jaeger components ready to be used (jaeger-agent, jaeger-collector, jaeger-query, zipkin).

After login to the leave vacation OpenShift project install the all-in-one template:

```bash

# development template, in memory storage for tracing, not production-ready

oc process -f https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/all-in-one/jaeger-all-in-one-template.yml | oc create -f -
```
For a production-ready installation follow the instructions at:<br>
https://github.com/jaegertracing/jaeger-openshift (Production setup section).

After installed, The Jaeger UI will be available at:<br>
https://jaeger-query-leave-vacation.ocp-cluster_ip/search

So far the employee microservice creates the tracing data for server requests (rest endpoints) and also client requests to sick request microservice (RestTemplate).

The employee microservice uses the Jaeger agent to collect tracing data; this agent is deployed as a *sidecar* container in Openshift deployment file:<br>
*src/main/fabric8/deployment.yml*

```bash
- image: jaegertracing/jaeger-agent
  name: jaeger-agent
  ports:
  - containerPort: 5775
    protocol: UDP
  - containerPort: 5778
  - containerPort: 6831
    protocol: UDP
  - containerPort: 6832
    protocol: UDP
  command:
  - /go/bin/agent-linux
  - '--collector.host-port=jaeger-collector.leave-vacation.svc:14267'
```

The Jaeger configuration properties are in file:<br>
*src/main/reources/application-openshift.properties*

```bash
jaeger.service-name=rhoar-employee-microservice
jaeger.sampler-type=ratelimiting
jaeger.sampler-param=1
jaeger.reporter-log-spans=true
opentracing.servlet.skipPattern=/health|/favicon\.ico
```
The employee microservice uses jaeger and opentracing api to trace the data.<br>
Maven dependencies are listed in *pom.xml* file:

```
<!-- OpenTracing -->
<dependency>
  <groupId>io.opentracing.contrib</groupId>
  <artifactId>opentracing-spring-web</artifactId>
</dependency>
<dependency>
  <groupId>io.opentracing.contrib</groupId>
  <artifactId>opentracing-web-servlet-filter</artifactId>
</dependency>
<dependency>
  <groupId>io.opentracing</groupId>
  <artifactId>opentracing-api</artifactId>
</dependency>
<dependency>
  <groupId>com.uber.jaeger</groupId>
  <artifactId>jaeger-core</artifactId>
</dependency>
```

The employee application defines a web filter and a rest template interceptor to trace data.

The web filter:<br> *com.redhat.springboot.vacationleave.employee.tracing.TracingFilterConfiguration*<br>
registers the url patterns and the skip patterns to be traced.

The rest template interceptor:<br>
*com.redhat.springboot.vacationleave.employee.tracing.TracingRestHandlerInterceptor*<br>
injects the recorder for every external calls that will use the Rest Template (in this example the call to the sick request microservice).

The Jaeger tracer configuration is created in class:<br>
*com.redhat.springboot.vacationleave.employee.tracing.JaegerTracerConfiguration*

Tracing data (for every URL endpoint) will be available in Jaeger query UI available at:<br>
https://jaeger-query-leave-vacation.ocp-cluster_ip/search

![alt text](https://github.com/hifly81/openshift-vacation-leave-request/blob/master/resources/images/jaeger-ui.png)
