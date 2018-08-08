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
 [GET] http://<url>/api/employees?page=<page_number>&pageSize=<page_size>
 ```

  - send sick request from employee

  ```
  [POST] http://<url>/api/employees/sickRequest

   body example:

    {
    	"ssn": "SASAASSA12",
    }
  ```

  - list sick requests by ssn

  ```
  [GET] http://<url>/api/employees/sickRequest/<ssn>?page=<page_number>&pageSize=<page_size>
  ```

These endpoints are available for sick requests microservice (listen on port 8090):

   - list sick requests

   ```
   [GET] http://<url>/api/sickrequests?page=<page_number>&pageSize=<page_size>
   ```

  - send sick request

  ```
  [PUT] http://<url>/api/sickrequests

  body example:

  {
  	"employeeId": "SASAASSA12",
  	"dateRequested": "2018-10-21"
  }

  ```

  - list sick requets by employee ssn

  ```
  [GET] http://<url>/api/sickrequests/<ssn>?page=<page_number>&pageSize=<page_size>
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

http://rhoar-employee-microservice-leave-vacation.<ocp-cluster_ip>
```

```
#for sick requests microservice

http://rhoar-sickrequests-microservice-leave-vacation.<ocp-cluster_ip>
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

 [GET] http://<url>/health
 ```

  ```
  #for sick requests microservice

  [GET] http://<url>/health
  ```

  On OpenShift are available at:

   ```
   #for employee microservice

   [GET] http://rhoar-employee-microservice-leave-vacation.<ocp-cluster_ip>/health
   ```

    ```
    #for sick requests microservice

    [GET] http://rhoar-sickrequests-microservice-leave-vacation.<ocp-cluster_ip>/health
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

After installed, the Jaeger UI will be available at:<br>
```
https://jaeger-query-leave-vacation.<ocp-cluster_ip>/search
```

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
```
https://jaeger-query-leave-vacation.<ocp-cluster_ip>/search
```

![alt text](https://github.com/hifly81/openshift-vacation-leave-request/blob/master/resources/images/jaeger-ui.png)


**Hystrix Circuit Breaker configuration**

This project uses *Spring Cloud Hystrix* to implement a circuit breaker in employee microservice.<br>
*Netflix Turbine* is the module able to aggregate the streams produced by the circuit breaker.<br>
*Hystrix dashboard* can be used to visualize the state and the metrics produced from the circuit breaker (streams).<br>
Hystrix dashboard registers the turbine streams.

The endpoint:  
- list sick requests by ssn

  ```
  [GET] http://<url>/api/employees/sickRequest/<ssn>?page=<page_number>&pageSize=<page_size>
  ```
in case of failure of the sick request microservice will answer with a fallback.

Maven dependency for hystrix is listed in employee *pom.xml* file:
```
 <dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-hystrix</artifactId>
 </dependency>
```

The method: *com.redhat.springboot.vacationleave.employee.service.SickRequestServiceImpl.getRequestsBySSN* is annotated with *@HystrixCommand*:
```
@Override
@HystrixCommand(fallbackMethod = "getRequestsBySSNFallback")
public List<SickRequestDto> getRequestsBySSN(String ssn, PageRequest pageRequest) {
  ```

Two web filters must be enabled to activate hystrix:
#activate hytrix
*com.redhat.springboot.vacationleave.employee.hystrix.HystrixRequestContextFilter*
#propagate the opentracing span to hystrix
*com.redhat.springboot.vacationleave.employee.tracing.SpanContextHystrixRequestVariableFilter*

Streams produced by Hystrix can be aggregated by Netflix Turbine; Turbine aggregates the streams for consumption by Hystrix UI dashboard.

Turbine uses the *fabric8 openshift client* to discover the openshift services (in this example the employee microservice) that produce a hystrix stream.<br>
The fabrix8 openshift client discovers only the services labeled with the property *hystrix.enabled: true*<br>
Turbine is developed on top of a Red Hat EAP image.

In order to install and configure Turbine and Hystrix dashboard on OpenShift these are the required steps:

```bash
#add the cluster-reader role to project leave-vacation
oc policy add-role-to-user cluster-reader system:serviceaccount:leave-vacation:default

#import the Red Hat EAP image from Red Hat registry (required for Turbine)
oc import-image my-jboss-eap-7/eap71-openshift --from=registry.access.redhat.com/jboss-eap-7/eap71-openshift --confirm

#Create the Turbine service with s2i method
oc new-app eap71-openshift~https://github.com/hifly81/openshift-vacation-leave-request --context-dir=turbine --name=turbine

#Create the Hystrix Dashboard (use an image from docker hub)
oc new-app mlabouardy/hystrix-dashboard:latest --name=hystrix-dashboard

#Expose Turbine and Hystrix dashboard routes
oc expose service turbine
oc expose service hystrix-dashboard
```

Hystrix dashboard will be available at URL:<br>
```
http://hystrix-dashboard-leave-vacation.<ocp-cluster_ip>/hystrix
```

Register the turbine stream to the Hystrix dashboard using the turbine service URL:<br>
```
http://<turbine-service-url>/turbine-1.0.0-SNAPSHOT/turbine.stream
```

You can test the list sick requests by ssn endpoint and analyze the state of the circuit (Open/Closed) and the metrics using the Hystrix dashboard

![alt text](https://github.com/hifly81/openshift-vacation-leave-request/blob/master/resources/images/hystrix_dashboard.png)


**Blue-green deployment**

Blue-green is a tecnique that minimize the risk and the downtime of a microservice architecture.<br>
It is based on having currently running two versions of the same microservice and at any time only one of the microservice serves all incoming traffic.

A Blue-green can be applied to different levels: you can have an entire blue and green OpenShift cluster or, as in this example, a single microservice inside a cluster provisioned with a blue-green tecnique.

Imagine that we want to release a new version of the sick requests microservice and direct (when the deployment is ready) the traffic from the previous version of the sick requests microservice (blue) to the new one (green).

The new version of sick requests microservice introduces a modification; inside class
*com.redhat.springboot.vacationleave.sickrequests.dto.SickRequestDto*<br>
the field *private Integer id;* is annotated with *@JsonIgnore* and it will not be serialized/deserialized.

A new git branch named *v2* has been created for the new version of sick requests microservice.

Deploy the new microservice on Openshift:

```bash
git checkout v2
cd sickrequests-microservice
mvn package fabric8:deploy -Popenshift -DskipTests
```

A new app named *rhoar-sickrequests-microservice-v2* will be available on OpenShift.
This new app doesn't provide a *route* and it's not exposed to external traffic.

The existing route *rhoar-sickrequests-microservice* still points to the older version of sick requests microservice *rhoar-sickrequests-microservice*.

Let's modify the route pointing to the new version:

```bash
oc patch route/rhoar-sickrequests-microservice -p '{"spec":{"to":{"name":"rhoar-sickrequests-microservice-v2"}}}'
```

Now every requests directed to:
```
 http://rhoar-sickrequests-microservice-leave-vacation.<ocp-cluster_ip>/
```

will be handled by the new version.

But the employee microservice doesn't use the route to communicate with sick requests microservice; it uses the internal *service* address; let's modify the *rhoar-sickrequests-microservice* service to point to the new version:

```bash
oc patch svc/rhoar-sickrequests-microservice -p '{"spec":{"selector":{"app":"rhoar-sickrequests-microservice-v2"}}}'
```

Now every requests directed to
```
 http://rhoar-employee-microservice-leave-vacation.<ocp-cluster_ip>/api/employees/sickRequest
```

will be handled by the new version.
