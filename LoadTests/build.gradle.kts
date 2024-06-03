plugins {
    id("java")
    id("io.gatling.gradle") version "3.11.3"
}

group = "com.raphaelcollin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/io.gatling/gatling-core
    implementation("io.gatling:gatling-core:3.11.1")
    // https://mvnrepository.com/artifact/io.gatling/gatling-test-framework
    testImplementation("io.gatling:gatling-test-framework:3.11.1")
    // https://mvnrepository.com/artifact/io.gatling/gatling-app
    implementation("io.gatling:gatling-app:3.11.1")


}

tasks.test {
    useJUnitPlatform()
}