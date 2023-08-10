plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.5.2"
}

group = "cx.leo.simplychat"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
}

dependencies {
    // PaperMC-API
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")

    // Cloud Command
    implementation("cloud.commandframework", "cloud-paper", "1.8.0")
    implementation("cloud.commandframework", "cloud-annotations", "1.8.0")
    implementation("cloud.commandframework", "cloud-minecraft-extras", "1.8.0")

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

    shadowJar {
        relocate("cloud.commandframework", "cx.leo.simplychat.lib.cloud")
    }

    build {
        dependsOn(shadowJar)
    }
}