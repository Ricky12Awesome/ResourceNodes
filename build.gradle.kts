import dev.architectury.plugin.ArchitecturyPlugin
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  kotlin("jvm") version "1.8.20" apply false
  id("architectury-plugin") version "3.4-SNAPSHOT"
  id("dev.architectury.loom") version "1.1-SNAPSHOT" apply false
}

group = "ricky12awesome"
version = "1.0-SNAPSHOT"

val archives_base_name: String by rootProject
val architectury_version: String by rootProject
val enabled_platforms: String by rootProject
val kotlin_version: String by rootProject
val maven_group: String by rootProject
val minecraft_version: String by rootProject
val mod_version: String by rootProject

architectury {
  minecraft = minecraft_version
}

subprojects {
  apply(plugin = "dev.architectury.loom")

  val loom: LoomGradleExtensionAPI by project.extensions

  dependencies {
    "minecraft"("com.mojang:minecraft:$minecraft_version")
    "mappings"(loom.officialMojangMappings())
  }
}

allprojects {
  apply<JavaPlugin>()
  apply<ArchitecturyPlugin>()
  apply<MavenPublishPlugin>()
  apply<KotlinPluginWrapper>()

  base.archivesName.set(archives_base_name)
  version = mod_version
  group = maven_group

  repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.

    maven {
      name = "Botarium"
      url = uri("https://nexus.resourcefulbees.com/repository/maven-public/")
    }

    maven {
      name = "Curse Maven"
      url = uri("https://cursemaven.com")

      content {
        includeGroup("curse.maven")
      }
    }

    maven {
      name = "MSRandom"
      url = uri("https://maven.msrandom.net/repository/root/")
    }
  }

  dependencies {
    "compileClasspath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
  }

  tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
  }

  java {
    withSourcesJar()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
  }
}