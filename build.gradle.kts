plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("org.liquibase.gradle") version "2.2.0"
}

group = "com.gradle.boot"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//resilience4j
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("io.github.resilience4j:resilience4j-spring-boot3:2.0.2")
	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")

	implementation("org.springframework.boot:spring-boot-starter-web:3.1.4")

	implementation("org.liquibase:liquibase-core")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.4")
	//mapstruct
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	runtimeOnly("com.h2database:h2")
	//jackson
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")
	//lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	//test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}
tasks.register<Jar>("fatJar") {
	manifest {
		attributes("Main-Class" to "com.gradle.boot.fintech.FintechApplication")
	}
	archiveClassifier.set("fat")

	from(sourceSets.main.get().output)

	dependsOn(configurations.runtimeClasspath)
	from({
		configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
	})
}
tasks.withType<Test> {
	useJUnitPlatform()
}