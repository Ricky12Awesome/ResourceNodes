plugins {
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

architectury {
  platformSetupLoomIde()
  fabric()
}

loom {
  accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

val archives_base_name: String by rootProject
val architectury_version: String by rootProject
val fabric_api_version: String by rootProject
val fabric_language_kotlin_version: String by rootProject
val fabric_loader_version: String by rootProject

/**
 * @see: https://docs.gradle.org/current/userguide/migrating_from_groovy_to_kotlin_dsl.html
 * */
val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
val developmentFabric: Configuration by configurations.getting

configurations {
  compileClasspath.get().extendsFrom(common)
  runtimeClasspath.get().extendsFrom(common)
  developmentFabric.extendsFrom(common)
}

dependencies {
  modImplementation("net.fabricmc:fabric-loader:$fabric_loader_version")
  modApi("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")

  // Remove the next line if you don't want to depend on the API
  modApi("dev.architectury:architectury-fabric:$architectury_version")

  common(project(":common", configuration = "namedElements")) {
    isTransitive = false
  }

  shadowCommon(project(":common", configuration = "transformProductionFabric")) {
    isTransitive = false
  }

  modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_language_kotlin_version")
}

val javaComponent = components.getByName<AdhocComponentWithVariants>("java")

javaComponent.withVariantsFromConfiguration(configurations["sourcesElements"]) {
  skip()
}

tasks {
  processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
      expand("version" to project.version)
    }
  }

  shadowJar {
    exclude("architectury.common.json")

    configurations = listOf(shadowCommon)
    archiveClassifier.set("dev-shadow")
  }

  remapJar {
    injectAccessWidener.set(true)
    inputFile.set(shadowJar.flatMap { it.archiveFile })

    dependsOn(shadowJar)

    archiveClassifier.set("fabric")
  }

  jar {
    archiveClassifier.set("dev")
  }

  sourcesJar {
    val commonSources = project(":common").tasks.sourcesJar.get()

    dependsOn(commonSources)
    from(commonSources.archiveFile.map { zipTree(it) })
  }

  publishing {
    publications {
      create<MavenPublication>("mavenFabric") {
        artifactId = "$archives_base_name-${project.name}"

        from(javaComponent)
      }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
      // Add repositories to publish to here.
    }
  }
}
