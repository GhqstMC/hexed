
plugins {
    kotlin("jvm") version "1.7.10"

    // For serialization: remove if not needed
    kotlin("plugin.serialization") version "1.7.10"

    idea
    java
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "lol.nea.mixintesting"
version = "1.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

loom {
    launchConfigs {
        "client" {
            property("mixin.debug", "true")
            property("asmhelper.verbose", "true")
            arg("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
//            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            arg("--mixin", "mixins.mixintesting.json")
        }
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfig("mixins.mixintesting.json")
    }
    mixin {
        defaultRefmapName.set("mixins.mixintesting.refmap.json")
    }
}

sourceSets {
    val dummy by creating
    main {
        compileClasspath += dummy.output
        output.setResourcesDir(file("$buildDir/classes/java/main"))
    }
}


repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
    maven("https://repo.essential.gg/repository/maven-public")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    // For serialization: remove if not needed
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    shadowImpl(kotlin("stdlib-jdk8"))

    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") { isTransitive = false }
    annotationProcessor("org.spongepowered:mixin:0.8.4-SNAPSHOT")

//    shadowImpl("gg.essential:loader-launchwrapper:1.1.3")
//    implementation("gg.essential:essential-1.8.9-forge:5155+gf6c1d3696")

//    modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:1.1.0")
}

tasks {
    processResources {
        filesMatching("mcmod.info") {
            expand(
                mapOf(
                    "modname" to project.name,
                    "modid" to project.name.toLowerCase(),
                    "version" to project.version,
                    "mcversion" to "1.8.9"
                )
            )
        }
    }
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.withType(Jar::class) {
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"

        this["MixinConfigs"] = "mixins.mixintesting.json"

        this["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
        this["TweakOrder"] = "0"
    }
}

val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("all")
    from(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)
}

tasks.shadowJar {
    archiveClassifier.set("all-dev")
    configurations = listOf(shadowImpl)
    doLast {
        configurations.forEach {
            println("Config: ${it.files}")
        }
    }
}

tasks.assemble.get().dependsOn(tasks.remapJar)