plugins {
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
}

dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.1")
    compileOnly("org.geysermc.floodgate:common:2.1.0-SNAPSHOT")
    compileOnly("org.geysermc:core:2.0.0-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:22.0.0")
}

description = "common"