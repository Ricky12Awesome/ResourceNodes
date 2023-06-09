plugins {
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

architectury {
  platformSetupLoomIde()
  forge()
}

val archives_base_name: String by rootProject
val architectury_version: String by rootProject
val botarium_version: String by rootProject
val forge_version: String by rootProject
val json5k_version: String by rootProject
val kotlin_for_forge_version: String by rootProject
val minecraft_version: String by rootProject

loom {
  accessWidenerPath.set(project(":common").loom.accessWidenerPath)

  forge {
    convertAccessWideners.set(true)
    extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
  }
}

sourceSets {
  main {
    resources {
      srcDir("src/generated/resources")
    }
  }
}

/**
 * @see: https://docs.gradle.org/current/userguide/migrating_from_groovy_to_kotlin_dsl.html
 * */
val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
val developmentForge: Configuration by configurations.getting

configurations {
  compileClasspath.get().extendsFrom(common)
  runtimeClasspath.get().extendsFrom(common)
  developmentForge.extendsFrom(common)
}

repositories {
  maven {
    name = "Kotlin for Forge"
    url = uri("https://thedarkcolour.github.io/KotlinForForge/")
  }
}

dependencies {
  forge("net.minecraftforge:forge:$forge_version")
  // Remove the next line if you don't want to depend on the API
  modApi("dev.architectury:architectury-forge:$architectury_version")

  common(project(":common", configuration = "namedElements")) {
    isTransitive = false
  }

  shadowCommon(project(":common", configuration = "transformProductionForge")) {
    isTransitive = false
  }

  // Kotlin
  implementation("thedarkcolour:kotlinforforge:$kotlin_for_forge_version")

  common("io.github.xn32:json5k-jvm:$json5k_version") {
    isTransitive = false
  }

  shadowCommon("io.github.xn32:json5k-jvm:$json5k_version") {
    isTransitive = false
  }

  // Mods
  modApi("earth.terrarium:botarium-forge-$minecraft_version:$botarium_version")
}

val javaComponent = components.getByName<AdhocComponentWithVariants>("java")

javaComponent.withVariantsFromConfiguration(configurations["sourcesElements"]) {
  skip()
}

tasks {
  processResources {
    inputs.property("version", project.version)

    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    filesMatching("META-INF/mods.toml") {
      expand("version" to project.version)
    }
  }

  shadowJar {
    exclude("fabric.mod.json")
    exclude("architectury.common.json")

    configurations = listOf(shadowCommon)

    archiveClassifier.set("dev-shadow")
  }

  remapJar {
    inputFile.set(shadowJar.flatMap { it.archiveFile })

    dependsOn(shadowJar)

    archiveClassifier.set("forge")
  }

  jar {
    archiveClassifier.set("dev")
  }

  sourcesJar {
    val commonSources = project(":common").tasks.getByName<Jar>("sourcesJar")

    dependsOn(commonSources)
    from(commonSources.archiveFile.map { zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
  }

  publishing {
    publications {
      create<MavenPublication>("mavenForge") {
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