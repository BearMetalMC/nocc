stonecutter active "26.3-fabric"

stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)

    // Applies the version- and loader-specific tables of `stonecutter.properties.toml`
    properties {
        tags(version, loader)
    }

    // `//? if fabric {` / `//? if neoforge {`
    constants {
        match(loader, "fabric", "neoforge")
    }

    replacements {
        // Mojang renamed ResourceLocation to Identifier in 1.21.11
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }
    }
}
