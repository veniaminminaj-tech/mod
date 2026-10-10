# ResourcePack Manager — Forge 1.21.11

A client-side Minecraft Java mod project using **Minecraft Forge only**.

## Compatibility

| Component | Version |
|---|---|
| Minecraft Java Edition | 1.21.11 |
| Forge | 61.2.0 |
| Java | 21 |
| Gradle | 9.6.0 |
| Mod version | 0.2.0 |

## Features

- Press **K** in-game to open ResourcePack Manager.
- Open Minecraft's built-in resource-pack selection screen.
- Open featured resource-pack project pages: Faithful 32x, Ashen, and Excalibur.
- Open performance-mod project pages: Sodium, Lithium, FerriteCore, ImmediatelyFast, and Entity Culling.
- Links open official Modrinth project pages in the browser.

The catalog links to the projects; it does not bundle or automatically install third-party files. Check each project's Minecraft version, Forge support, dependencies, and license before installing. Not every listed performance mod or texture pack necessarily has a Forge build for Minecraft 1.21.11.

## Build

Install **JDK 21**. From this directory run:

```bash
gradle --no-daemon clean build
```

The JAR is generated in `build/libs/`. GitHub Actions builds the Forge project and uploads the JAR as an artifact.

## Controls

- **K** — open ResourcePack Manager.

## Project structure

- `forge/` — the active Forge 1.21.11 project.
- The active CI workflow is `.github/workflows/resourcepack-manager-forge.yml` at repository root.

## References

- [Forge 1.21.11 downloads and MDK](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.21.11.html)
- [ForgeGradle example for Minecraft 1.21.11](https://github.com/MinecraftForge/MDKExamples/tree/master/traditional-mdk/fg7)
- [Modrinth](https://modrinth.com/)
