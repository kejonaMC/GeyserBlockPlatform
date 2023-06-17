
plugins {
    id("com.github.johnrengelman.shadow")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    api(projects.common)

    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

tasks {
    shadowJar {
        val prefix = "${project.group}.${project.name}.shaded"
        relocate("org.spongepowered.configurate", "$prefix.configurate")
        relocate("io.leangen.geantyref", "$prefix.geantyref")

        archiveFileName.set("GeyserBlockPlatform-Velocity.jar")
    }

    build {
        dependsOn(shadowJar)
    }
}
