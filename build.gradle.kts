import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm")
    `maven-publish`

    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public/")
    mavenCentral()
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenLocal()
        maven("https://libraries.minecraft.net")
        maven("https://maven.aliyun.com/repository/public/")
        mavenCentral()
    }

    dependencies {
        compileOnly("com.mojang:brigadier:1.2.9")
        compileOnly(kotlin("stdlib"))
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
        withType<KotlinCompile> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_1_8)
                freeCompilerArgs.set(listOf("-Xjvm-default=all", "-Xextended-compiler-checks"))
            }
        }
        withType<ShadowJar> {
            relocate("com.mojang.", "me.mkbaka.simplecommand.libs.com.mojang.")
        }
    }

    kotlin {
        sourceSets.all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/MkbaKa/SimpleCommand")
                credentials {
                    username = System.getenv("GITHUB_NAME")
                    password = System.getenv("GITHUB_PACKAGES_TOKEN")
                }
            }
        }
        publications {
            register<MavenPublication>("gpr") {
                artifactId = this@subprojects.name
                groupId = "me.mkbaka.simplecommand"
                version = "${project.version}"

                artifact(this@subprojects.tasks["shadowJar"]) {
                    classifier = null
                }
                artifact(this@subprojects.tasks["kotlinSourcesJar"])
            }
        }
    }
}