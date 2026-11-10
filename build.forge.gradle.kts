plugins {
    java
    id("net.minecraftforge.gradle") version "[7.0.17,8)"
}

version = "${sc.properties["mod.version"]}+${sc.current.version}"
base.archivesName = "${sc.properties["mod.name"]}-forge"

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    else -> JavaVersion.VERSION_21
}

// One game directory per target, so worlds are never opened by an older version
val runDir = rootProject.layout.projectDirectory.dir("run/${project.name}")

minecraft {
    // Minecraft ships unobfuscated from 26.1, older versions need Mojang's names applied
    if (sc.current.parsed < "26.1") mappings("official", sc.current.version)

    runs {
        configureEach {
            workingDir = runDir
            // Dev runs load classes from a folder with no manifest, so name the mixin config here
            args("--mixin.config", "nocc.client.mixins.json")
        }
        register("client")
        register("server") {
            args("--nogui")
        }
    }
}

repositories {
    minecraft.mavenizer(this)
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
    mavenCentral()
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:${sc.current.version}-${sc.properties["deps.forge"]}"))
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
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

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
            register("forge", sc.properties["deps.forge"].substringBefore('.'))
        }
        filesMatching("META-INF/mods.toml") { expand(props) }

        // WorldOptionsScreen only exists on 26.1+
        val extraMixins = if (sc.current.parsed >= "26.1") ", \"WorldOptionScreenMixin\"" else ""
        inputs.property("extra_client_mixins", extraMixins)
        filesMatching("*.mixins.json") { expand("extra_client_mixins" to extraMixins) }

        exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
    }

    withType<Jar> {
        val id = sc.properties["mod.id"]
        inputs.property("mod_id", id)
        from(rootProject.file("LICENSE.txt")) { rename { "LICENSE_$id.txt" } }
    }

    jar {
        // Forge finds mixin configs through the manifest
        manifest.attributes("MixinConfigs" to "nocc.client.mixins.json")
    }
}
