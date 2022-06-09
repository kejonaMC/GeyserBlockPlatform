import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow")

}
repositories {
    maven("https://libraries.minecraft.net/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
}


dependencies {
    implementation(project(":common"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.10.2")
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:api:2.1.0-SNAPSHOT")
}
tasks.withType<ShadowJar> {
    dependencies {
        shadow {
            exclude("org.spigotmc:spigot-api")
        }
    }
    println(destinationDirectory.get())
    archiveFileName.set("GeyserBlockPlatform.jar")
    println(archiveFileName.get())
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}

description = "spigot"
