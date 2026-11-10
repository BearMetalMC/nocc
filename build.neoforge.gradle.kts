plugins {
    id("net.neoforged.moddev") version "2.0.148"
}

version = "${sc.properties["mod.version"]}+${sc.current.version}"
base.archivesName = "${sc.properties["mod.name"]}-neoforge"

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    else -> JavaVersion.VERSION_21
}

// One game directory per target, so worlds are never opened by an older version
val runDir = rootProject.file("run/${project.name}")

neoForge {
    version = sc.properties["deps.neo_loader"]

    mods {
        register(sc.properties["mod.id"]) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            gameDirectory = runDir
            client()
        }

        register("server") {
            gameDirectory = runDir
            server()
        }
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, value: String) {
            inputs.property(key, value)
            set(key, value)
        }

        val props = buildMap {
            register("id", sc.properties["mod.id"])
            register("name", sc.properties["mod.name"])
            register("version", sc.properties["mod.version"])
            register("minecraft", sc.properties["mod.mc_compat"])
        }
        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }

        // WorldOptionsScreen only exists on 26.1+
        val extraMixins = if (sc.current.parsed >= "26.1") ", \"WorldOptionScreenMixin\"" else ""
        inputs.property("extra_client_mixins", extraMixins)
        filesMatching("*.mixins.json") { expand("extra_client_mixins" to extraMixins) }

        exclude("fabric.mod.json")
    }

    // When a second NeoForge target is added, serialise this task across targets
    // (see neoforge-mutex in stonecutter-template-multiloader) to keep memory use sane.
    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    withType<Jar> {
        val id = sc.properties["mod.id"]
        inputs.property("mod_id", id)
        from(rootProject.file("LICENSE.txt")) { rename { "LICENSE_$id.txt" } }
    }
}
