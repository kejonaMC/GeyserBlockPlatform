
plugins {
    id("com.github.johnrengelman.shadow")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    api(projects.common)
    compileOnly("org.geysermc.geyser:api:2.1.2-SNAPSHOT")
}

tasks {
    shadowJar {
        val prefix = "${project.group}.${project.name}.shaded"
        relocate("org.spongepowered.configurate", "$prefix.configurate")
        relocate("io.leangen.geantyref", "$prefix.geantyref")

        archiveFileName.set("GeyserBlockPlatform-Extension.jar")
    }

    build {
        dependsOn(shadowJar)
    }
}
