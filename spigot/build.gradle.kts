
plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    api(projects.common)
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

tasks {
    shadowJar {
        val prefix = "${project.group}.${project.name}.shaded"
        relocate("org.spongepowered.configurate", "$prefix.configurate")
        relocate("io.leangen.geantyref", "$prefix.geantyref")

        archiveFileName.set("GeyserBlockPlatform-Spigot.jar")
    }

    build {
        dependsOn(shadowJar)
    }
}
