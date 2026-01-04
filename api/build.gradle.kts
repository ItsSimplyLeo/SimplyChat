plugins {
    id("java")
    `maven-publish`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
    withJavadocJar()
}

// TODO: publish ;)