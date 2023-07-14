
plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    api(projects.common)
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        val prefix = "${project.group}.${project.name}.shaded"
        relocate("org.spongepowered.configurate", "$prefix.configurate")
        relocate("io.leangen.geantyref", "$prefix.geantyref")

        archiveFileName.set("GeyserBlockPlatform-Bungeecord.jar")
    }

    build {
        dependsOn(shadowJar)
    }
}
