plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "cx.leo.simplychat"
version = "1.0.0-SNAPSHOT"

var cloudVersion: String = "2.0.0-beta.7" // For Minecraft Impl
var cloudRcVersion: String = "2.0.0-rc.1" // For Cloud

repositories {
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://ci.ender.zone/plugin/repository/everything/") // Vault
}

dependencies {
    // PaperMC-API
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")

    // Cloud Command
    implementation("org.incendo", "cloud-paper", cloudVersion)
    implementation("org.incendo", "cloud-annotations", cloudRcVersion)
    implementation("org.incendo", "cloud-minecraft-extras", cloudVersion)

    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.5")

    // Vault
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    processResources {
        val props = mapOf(
            "version" to project.version,
        )
        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    shadowJar {
        relocate("org.incendo.cloud", "cx.leo.simplychat.lib.cloud")
        relocate("io.leangen.geantyref", "cx.leo.lib.cloud.io.leangen.geantyref")
    }

    build {
        dependsOn(shadowJar)
    }
}