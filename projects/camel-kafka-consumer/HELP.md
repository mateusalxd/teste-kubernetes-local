# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.github.mateusalxd.camel-kafka-consumer' is invalid and this project uses 'com.github.mateusalxd.kafkaconsumer' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.10/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.10/gradle-plugin/reference/html/#build-image)

### Guides
The following guides illustrate how to use some features concretely:

* [Using Apache Camel with Spring Boot](https://camel.apache.org/camel-spring-boot/latest/spring-boot.html)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

# Build

https://gist.github.com/msauza/6a906e879549e218c54868d81161afcb

```shell
./gradlew clean && \
./gradlew bootjar && \
mkdir build/libs/dependency && \
 (cd build/libs/dependency; jar -xf ../*.jar) && \
docker build -t mateusalxd/camel-kafka-consumer . && \
minikube image load mateusalxd/camel-kafka-consumer
```