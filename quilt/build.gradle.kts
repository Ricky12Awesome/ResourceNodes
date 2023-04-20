plugins {
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
  maven("https://maven.quiltmc.org/repository/release/")
  mavenCentral()
}

architectury {
  platformSetupLoomIde()
  loader("quilt")
}

loom {
  accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

val archives_base_name: String by rootProject
val architectury_version: String by rootProject
val botarium_version: String by rootProject
val fabric_language_kotlin_version: String by rootProject
val minecraft_version: String by rootProject
val quilt_fabric_api_version: String by rootProject
val quilt_loader_version: String by rootProject

/**
 * @see: https://docs.gradle.org/current/userguide/migrating_from_groovy_to_kotlin_dsl.html
 * */
val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
val developmentQuilt: Configuration by configurations.getting

configurations {
  compileClasspath.get().extendsFrom(common)
  runtimeClasspath.get().extendsFrom(common)
  developmentQuilt.extendsFrom(common)
}

dependencies {
  modImplementation("org.quiltmc:quilt-loader:$quilt_loader_version")
  modApi("org.quiltmc.quilted-fabric-api:quilted-fabric-api:$quilt_fabric_api_version")
  // Remove the next few lines if you don't want to depend on the API
  modApi("dev.architectury:architectury-fabric:$architectury_version") {
    // We must not pull Fabric Loader from Architectury Fabric
    exclude(group = "net.fabricmc")
    exclude(group = "net.fabricmc.fabric-api")
  }

  common(project(":common", configuration = "namedElements")) {
    isTransitive = false
  }
  shadowCommon(project(":common", configuration = "transformProductionQuilt")) {
    isTransitive = false
  }

  // Kotlin
  modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_language_kotlin_version")

  // Mods
  modApi("earth.terrarium:botarium-fabric-$minecraft_version:$botarium_version")
}

val javaComponent = components.getByName<AdhocComponentWithVariants>("java")

javaComponent.withVariantsFromConfiguration(configurations["sourcesElements"]) {
  skip()
}

tasks {
  processResources {
    inputs.property("group", rootProject.property("maven_group"))
    inputs.property("version", project.version)

    filesMatching("quilt.mod.json") {
      expand(
        "group" to rootProject.property("maven_group"),
        "version" to project.version
      )
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

    archiveClassifier.set("quilt")
  }

  jar {
    archiveClassifier.set("dev")
  }

  sourcesJar {
    val commonSources = project(":common").tasks.getByName<Jar>("sourcesJar")

    dependsOn(commonSources)
    from(commonSources.archiveFile.map { zipTree(it) })
  }

  publishing {
    publications {
      create<MavenPublication>("mavenQuilt") {
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