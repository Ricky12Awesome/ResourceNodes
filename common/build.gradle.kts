plugins {
  `maven-publish`
}

val archives_base_name: String by rootProject
val architectury_version: String by rootProject
val botarium_version: String by rootProject
val enabled_platforms: String by rootProject
val fabric_loader_version: String by rootProject
val json5k_version: String by rootProject
val kotlinx_serialization_version: String by rootProject
val minecraft_version: String by rootProject

architectury {
  common(enabled_platforms.split(","))
}

loom {
  accessWidenerPath.set(file("src/main/resources/resource_nodes.accesswidener"))
}

dependencies {
  // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
  // Do NOT use other classes from fabric loader
  modImplementation("net.fabricmc:fabric-loader:$fabric_loader_version")

  // Remove the next line if you don't want to depend on the API
  modApi("dev.architectury:architectury:$architectury_version")

  // Kotlin
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:$kotlinx_serialization_version")
  implementation("io.github.xn32:json5k-jvm:$json5k_version")

  // Mods
  modImplementation("earth.terrarium:botarium-common-$minecraft_version:$botarium_version")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      artifactId = archives_base_name

      from(components.getByName("java"))
    }
  }

  // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
  repositories {
    // Add repositories to publish to here.
  }
}