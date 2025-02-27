# Zipkin Tracing with Java Microservices

This guide walks you through setting up **Spring Boot microservices** with **Zipkin tracing** using Java 17, Gradle, and Docker.

## 📌 Prerequisites

- ✅ Java 17+ (Ensure Java toolchain is configured)
- ✅ Spring Boot 3.3+
- ✅ Docker (for running Zipkin)
- ✅ Maven or Gradle
- ✅ `curl` or `Spring Boot CLI`

---

## 🛠 Step 1: Install Java (if not installed)

Check if Java is installed:
```sh
java -version
```
If Java is not found, install it:
```sh
sudo dnf install java-17-openjdk -y
```
Verify installation:
```sh
java -version
```
If Java 21 is installed and causing conflicts, set Java 17 as the default:
```sh
sudo alternatives --config java
```

---

## ⚙️ Step 2: Ensure Gradle Uses the Correct JDK

If Java 17 is installed but Gradle is not recognizing it, explicitly set the `JAVA_HOME` variable:
```sh
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
export PATH=$JAVA_HOME/bin:$PATH
```
Verify:
```sh
echo "JAVA_HOME is set to: $JAVA_HOME"
java -version
```
Also, ensure Gradle uses Java 17 by adding this in `gradle.properties`:
```ini
org.gradle.java.home=/usr/lib/jvm/java-17-openjdk
```

---

## 🔧 Step 3: Configure Java Toolchain in Gradle
Edit `build.gradle` and add:
```gradle
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
```
Then run:
```sh
./gradlew --version
```
To verify Java 17 is recognized.

---

## 🚀 Step 4: Create and Configure Service A
### 🏗 Initialize Service A with Spring Initializr
```sh
curl -G https://start.spring.io/starter.zip \
    --data-urlencode dependencies=web,actuator,micrometer-tracing,micrometer-tracing-bridge-brave,zipkin-reporter-brave \
    -d type=maven-project -d language=java -d bootVersion=3.3.0 -o service-a.zip
unzip service-a.zip -d service-a
cd service-a
```
### 📝 Update `build.gradle` for Service A
Ensure `service-a/build.gradle` has:
```gradle
application {
    mainClass = 'com.example.servicea.ServiceAApplication'
}
```
### 📝 Create `ServiceAApplication.java`
```java
package com.example.servicea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceAApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAApplication.class, args);
    }
}
```
### 📝 Create `ServiceAController.java`
```java
package com.example.servicea;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RestController
@RequestMapping("/service-a")
public class ServiceAController {
    private final RestTemplate restTemplate;

    public ServiceAController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @GetMapping("/call-service-b")
    public String callServiceB() {
        return restTemplate.getForObject("http://localhost:8081/service-b/hello", String.class);
    }
}

@Configuration
class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
```

---

## 🚀 Step 5: Create and Configure Service B
### 🏗 Initialize Service B with Spring Initializr
```sh
curl -G https://start.spring.io/starter.zip \
    --data-urlencode dependencies=web,actuator,micrometer-tracing,micrometer-tracing-bridge-brave,zipkin-reporter-brave \
    -d type=maven-project -d language=java -d bootVersion=3.3.0 -o service-b.zip
unzip service-b.zip -d service-b
cd service-b
```
### 📝 Update `build.gradle` for Service B
Ensure `service-b/build.gradle` has:
```gradle
application {
    mainClass = 'com.example.serviceb.ServiceBApplication'
}
```
### 📝 Create `ServiceBApplication.java`
```java
package com.example.serviceb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceBApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceBApplication.class, args);
    }
}
```
### 📝 Create `ServiceBController.java`
```java
package com.example.serviceb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service-b")
public class ServiceBController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Service B";
    }
}
```

---

## ▶️ Step 6: Start Both Services
### Start Service B
```sh
cd service-b
./gradlew bootRun
```
### Start Service A
```sh
cd ../service-a
./gradlew bootRun
```

---

## ✅ Step 7: Verify Everything Works
### 🔍 Call Service A to Reach Service B
```sh
curl http://localhost:8080/service-a/call-service-b
```
✅ **Expected Output:**
```
Hello from Service B
```
### 🔍 Check Zipkin Traces
Open **[http://localhost:9411](http://localhost:9411)** and click **Find Traces**.

---

## 🎯 Conclusion
🎉 Your microservices are now **working with Zipkin tracing**! If you face any issues, ensure:
- ✅ Both services are **running on the correct ports**.
- ✅ The **controller mappings** are correct.
- ✅ **Service A calls the correct URL** for Service B.

🚀 **Happy coding!**






🚀 **Diagram**







