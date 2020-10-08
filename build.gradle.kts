plugins {
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    idea
}

group = "com.recipt"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val sourceCompatibility = 1.8
val targetCompatibility = 1.8

subprojects {
    if (project.name == "gateway") {
        apply(plugin = "kotlin-spring")
        apply(plugin = "kotlin")
        apply(plugin = "io.spring.dependency-management")
        apply(plugin = "org.springframework.boot")

        buildscript {
            repositories {
                maven {
                    url = uri("https://repo.spring.io/plugins-snapshot")
                }
                mavenCentral()
            }
            dependencies {
                classpath("org.springframework.boot:spring-boot-gradle-plugin:2.1.7.RELEASE")
                classpath("io.spring.gradle:dependency-management-plugin:1.0.7.BUILD-SNAPSHOT")
                classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
            }
        }

        repositories {
            mavenCentral()
        }

        dependencyManagement {
            imports {
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.SR2")
            }
        }

        dependencies {
            implementation(kotlin("stdlib-jdk8"))
            implementation("org.springframework.cloud:spring-cloud-starter-gateway")
            implementation("org.springframework.boot:spring-boot-configuration-processor")
            annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
            implementation("org.springframework.cloud:spring-cloud-starter-netflix-hystrix")
            implementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner") {
                exclude("org.springframework.boot:spring-boot-starter-web")
            }
            testImplementation("org.springframework.boot:spring-boot-starter-test")

            // jwt
            implementation("io.jsonwebtoken:jjwt:0.9.1")

            // jackson
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
            implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
            implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5")

        }

        tasks {
            compileKotlin {
                kotlinOptions.jvmTarget = "1.8"
            }
            compileTestKotlin {
                kotlinOptions.jvmTarget = "1.8"
            }
            bootJar {
                enabled = false
            }
        }
    }
}

