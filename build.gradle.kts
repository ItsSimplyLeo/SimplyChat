plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2" apply false
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19" apply false
    id("maven-publish")
}

group = "cx.leo.simplychat"
version = "1.0.0-SNAPSHOT-overhaul"

allprojects {
    group = "cx.leo.simplychat"
    version = "1.0.0-SNAPSHOT-overhaul"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

subprojects {
    apply(plugin = "java")

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
}