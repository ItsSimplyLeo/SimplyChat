plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "cx.leo.simplychat"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
}

dependencies {
    // PaperMC-API
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")

    // Cloud Command
    implementation("cloud.commandframework", "cloud-paper", "1.8.0")
    implementation("cloud.commandframework", "cloud-annotations", "1.8.0")
    implementation("cloud.commandframework", "cloud-minecraft-extras", "1.8.0")

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
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
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