# ResourcePack Manager — Forge

Client-side Minecraft Java mod for **Minecraft 1.21.11**, now built with **Minecraft Forge only**.

The active project is in `resourcepack-manager/forge/`. Older Fabric and NeoForge project folders are retained as legacy files but are no longer built by the ResourcePack Manager workflow. The repository also contains a separate older Holy Church Forge 1.16.5 project.

## Project details

- **Minecraft:** 1.21.11
- **Forge:** 61.2.0
- **Java:** 21
- **Gradle:** 9.6.0
- **Mod version:** 0.2.0
- **In-game key:** `K`

## Features

- Opens Minecraft's built-in resource-pack selection screen.
- Featured links for Faithful 32x, Ashen, and Excalibur resource packs.
- Featured links for Sodium, Lithium, FerriteCore, ImmediatelyFast, and Entity Culling.
- Opens official Modrinth project pages in the browser.

The catalog links to official project pages; it does not bundle or automatically install third-party files. Check each project's Minecraft version, Forge support, dependencies, and license before installing.

## Build

Install **JDK 21** and Gradle 9.6.0, then run:

```bash
cd resourcepack-manager/forge
gradle --no-daemon clean build
```

The resulting JAR is written to `resourcepack-manager/forge/build/libs/`. GitHub Actions builds the Forge project and uploads the JAR as an artifact.

## Status

This is an early development build. The Gradle configuration and Forge-only CI workflow have been added, but the build must pass in GitHub Actions before the JAR can be considered verified.

## References

- [Forge 1.21.11 downloads and MDK](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.21.11.html)
- [ForgeGradle 7 example project](https://github.com/MinecraftForge/MDKExamples/tree/master/traditional-mdk/fg7)
- [Modrinth](https://modrinth.com/)
