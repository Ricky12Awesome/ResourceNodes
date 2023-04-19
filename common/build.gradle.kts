plugins {
  `maven-publish`
}

val archives_base_name: String by rootProject
val enabled_platforms: String by rootProject
val fabric_loader_version: String by rootProject
val architectury_version: String by rootProject


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
  implementation(kotlin("stdlib-jdk8"))
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