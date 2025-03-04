plugins {
    id("java")
    id("com.github.ben-manes.versions") version "0.52.0"
    application
    checkstyle
    jacoco
    id("io.freefair.lombok") version "8.12.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
}

application {
    mainClass.set("hexlet.code.App")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.konghq:unirest-java-bom:4.4.5")
    implementation("com.konghq:unirest-java-core:4.4.5")
    implementation("com.konghq:unirest-modules-gson:4.4.5")
    implementation("com.konghq:unirest-modules-jackson:4.4.5")
    implementation("org.apache.commons:commons-text:1.11.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.apache.commons:commons-lang3:3.15.0")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation ("org.jsoup:jsoup:1.18.3")
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("io.javalin:javalin:6.4.0")
    implementation("gg.jte:jte:3.1.16")
    implementation("io.javalin:javalin-rendering:6.4.0")
    implementation("io.javalin:javalin-bundle:6.4.0")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("net.datafaker:datafaker:2.4.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports { xml.required.set(true) }
}
