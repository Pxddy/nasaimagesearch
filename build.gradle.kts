buildscript {

}// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    val agpVersion = "8.8.1"

    id("com.android.application") version agpVersion apply false
    id("com.android.library") version agpVersion apply false
    id("com.google.dagger.hilt.android") version "2.55" apply false
    id("org.jetbrains.kotlin.android") version "2.1.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10" apply false
    id("com.google.devtools.ksp") version "2.1.10-1.0.30" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}