pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.10"

    // Applies the right Fabric Loom variant (remapping before 26.1, plain after)
    id("dev.kikugie.loom-back-compat") version "0.4.3"

    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        /**
         * Registers `versions/{mc}-{loader}` for each loader, built by `build.{loader}.gradle.kts`.
         * Every target also needs its tables in `stonecutter.properties.toml`.
         */
        fun match(mc: String, vararg loaders: String) {
            for (loader in loaders) version("$mc-$loader", mc).buildscript("build.$loader.gradle.kts")
        }

        match("1.21.10", "fabric", "forge", "neoforge")
        match("26.3", "fabric", "forge", "neoforge")
        vcsVersion = "26.3-fabric"
    }
}

rootProject.name = "NoCC"
