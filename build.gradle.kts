
plugins {
    `java-library`
    id("idea")
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

allprojects {
    group = "com.github.camotoy.geyserblockplatform"
    version = "1.1-SNAPSHOT"
    description = "Prevent specific Bedrock platforms from joining your server"

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
    }
}

subprojects {
    apply(plugin = "java-library")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }

    dependencies {
        annotationProcessor("org.projectlombok:lombok:1.18.28")
        compileOnly("org.projectlombok:lombok:1.18.28")

        compileOnly("org.jetbrains:annotations:24.0.1")

        compileOnly("org.geysermc.api:base-api:1.0.0-SNAPSHOT")
    }

    repositories {
        mavenCentral()
        maven("https://repo.opencollab.dev/main/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.spongepowered.org/maven/")
    }

    tasks.processResources {
        filesMatching(listOf("bungee.yml", "spigot.yml")) {
            expand(
                "description" to project.description,
                "version" to project.version,
            )
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
