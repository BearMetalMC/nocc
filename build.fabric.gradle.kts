plugins {
    // Applies the correct Loom variant for the Minecraft version
    id("dev.kikugie.loom-back-compat")
}

// Do not set `group` here, Stonecutter relies on the default
version = "${sc.properties["mod.version"]}+${sc.current.version}"
base.archivesName = "${sc.properties["mod.name"]}-fabric"

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    else -> JavaVersion.VERSION_21
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    // Only does something on obfuscated versions (before 26.1)
    loomx.applyMojangMappings()

    // `mod*` configurations are converted by loom-back-compat on 26.1+
    modImplementation("net.fabricmc:fabric-loader:${sc.properties["deps.fabric_loader"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${sc.properties["deps.fabric_api"]}")
}

loom {
    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        // One game directory per target, so worlds are never opened by an older version
        runDirectory = rootProject.file("run/${project.name}")
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
            register("loader", Regex("\\d+\\.\\d+").find(sc.properties["deps.fabric_loader"])!!.value)
            register("java", requiredJava.majorVersion)
        }
        filesMatching("fabric.mod.json") { expand(props) }

        // WorldOptionsScreen only exists on 26.1+
        val extraMixins = if (sc.current.parsed >= "26.1") ", \"WorldOptionScreenMixin\"" else ""
        inputs.property("extra_client_mixins", extraMixins)
        filesMatching("*.mixins.json") { expand("extra_client_mixins" to extraMixins) }

        exclude("META-INF/neoforge.mods.toml", "META-INF/mods.toml")
    }

    withType<Jar> {
        val id = sc.properties["mod.id"]
        inputs.property("mod_id", id)
        from(rootProject.file("LICENSE.txt")) { rename { "LICENSE_$id.txt" } }
    }
}
