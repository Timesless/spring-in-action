# spring-in-action

## spring-core 模块
> core模块包含以下内容：
> 1. IoC Container
> 2. AOP
> 3. SpEl(Spring Expression Language)
> 

## spring-dao 模块
> dao模块包含以下内容：
> 1. Transactions
> 2. DAO Support
> 3. JDBC
> 4. O/R Mapping
> 5. XML Marshalling
> 

## spring-web-servlet 模块
> web-servlet模块包含以下内容：
> 1. Spring MVC
> 2. RestTemplate
> 3. WebSocket


## spring-reactive 模块
> reactive模块包含以下内容
> 1. Spring WebFlux
> 2. WebClient
> 3. WebSocket

#### features
+ Spring WebFlux is supported on Tomcat, Jetty, Servlet3.1+ containers, as well as non-Servlet runtimes such as Netty and Undertow
+ In Spring MVC (and servlet applications in general), it is assumed that applications can block the current thread, (for example, for remote calls). 
  For this reason, servlet containers use a large thread pool to absorb potential blocking during request handling.
+ In Spring WebFlux (and non-blocking servers in general), it is assumed that applications do not block.
  Therefore, non-blocking servers use a small, fixed-size thread pool (event loop workers) to handle requests.


## spring-integration 模块
> integration模块包含以下内容：
> 1. JMS
> 2. JCA
> 3. JMX
> 4. Tasks
> 5. Scheduling
> 6. Caching


### 8. Spring Message Queue
Spring 提供3种方式的消息队列
+ JMS(ActiveMQ / Artemis(是ActiveMQ的改进版))
+ AMQP(RabbitMQ)
+ Kafka

