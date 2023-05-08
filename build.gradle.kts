plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.5.2"
    id("net.kyori.blossom") version "1.3.1"
}

group = "cx.leo.simplychat"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.alessiodp.com/releases/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
    maven("https://maven.citizensnpcs.co/repo")
    maven("https://jitpack.io/")
}

dependencies {
    // PaperMC-API
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    //compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

    // Cloud Command
    compileOnly("cloud.commandframework", "cloud-paper", "1.8.0")
    compileOnly("cloud.commandframework", "cloud-annotations", "1.8.0")
    compileOnly("cloud.commandframework", "cloud-minecraft-extras", "1.8.0")

    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.2")

    // Vault
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    build {
        dependsOn(shadowJar)
    }
}