val generatedResourcesDir = "${layout.buildDirectory.get()}/generated-resources"

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
	id("org.openapi.generator") version "7.4.0"
	id("org.liquibase.gradle") version "2.2.0"
}

group = "com.loc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.liquibase:liquibase-core")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.15")
	implementation("io.swagger.core.v3:swagger-models:2.2.15")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

	runtimeOnly("mysql:mysql-connector-java:8.0.33")
    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("mysql:mysql-connector-java:8.0.33")
    liquibaseRuntime("info.picocli:picocli:4.6.3")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/db/changelog/db.changelog-master.xml",
            "url" to "jdbc:mysql://localhost:3306/inventory_service",
            "username" to "root",
            "password" to "mysql",
            "driver" to "com.mysql.cj.jdbc.Driver"
        )
    }
    runList = "main"
}

// OpenAPI Generator
openApiGenerate {
	inputSpec.set("$rootDir/src/main/resources/static/openapi.yaml")
	generatorName.set("kotlin-spring")
    modelPackage.set("com.loc.inventoryservice.model")
    outputDir.set("$generatedResourcesDir/openapi")

    configOptions.put("useSpringBoot3", "true")
    configOptions.put("dateLibrary", "java8")
    configOptions.put("delegatePattern", "true")
    configOptions.put("interfaceOnly", "true")
    configOptions.put("modelMutable", "true")
    configOptions.put("useTags", "true")
    configOptions.put("enumPropertyNaming", "UPPERCASE")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

sourceSets {
    main {
        kotlin {
            srcDir("$generatedResourcesDir/openapi/src/main/kotlin")
        }
    }
}

tasks.compileKotlin {
    dependsOn(tasks.openApiGenerate)
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
