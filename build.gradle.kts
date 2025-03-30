buildscript {

}// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    val agpVersion = "8.9.0"

    id("com.android.application") version agpVersion apply false
    id("com.android.library") version agpVersion apply false
    id("com.google.dagger.hilt.android") version "2.56" apply false
    id("org.jetbrains.kotlin.android") version "2.1.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20" apply false
    id("com.google.devtools.ksp") version "2.1.20-1.0.32" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}