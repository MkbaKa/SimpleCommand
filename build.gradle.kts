import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm")
}

group = "me.mkbaka.simplecommand"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public/")
    mavenCentral()
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenLocal()
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
}