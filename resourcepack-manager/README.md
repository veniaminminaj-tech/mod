# ResourcePack Manager — Minecraft 1.21.11

A client-side Minecraft mod project for organizing resource packs, with separate **Fabric** and **NeoForge** builds.

## Compatibility

| Component | Target |
|---|---|
| Minecraft Java Edition | 1.21.11 |
| Java | 21 |
| Fabric API | 0.141.1+1.21.11 |
| NeoForge | 21.11.45 |
| Mod version | 0.1.0 (early development) |

## Current functionality

- Press **K** in-game to open the manager screen.
- The manager can route to Minecraft's built-in resource-pack selection screen.
- Fabric and NeoForge are maintained as independent Gradle projects.

## Planned / not yet verified

The following should not be considered complete until implemented and tested in-game:

- Saving named resource-pack groups between launches.
- An in-game Modrinth catalog with search and downloads.
- One-click activation of saved groups.
- Shader-loader detection and compatible shader-pack integration.

Shader support depends on the installed shader mod; there is no single universal Minecraft API for activating shader packs across loaders.

## Build instructions

Install **JDK 21** first. Run commands from the repository root.

### Fabric

```bash
cd resourcepack-manager/fabric
gradle build
```

### NeoForge

```bash
cd resourcepack-manager/neoforge
gradle build
```

Look for the resulting JAR in the project's `build/libs/` directory. If Gradle is not installed, open the selected project in IntelliJ IDEA and configure it with a Gradle installation compatible with the project's plugins.

## Controls

- **K** — open ResourcePack Manager.

## References

- [Fabric documentation](https://docs.fabricmc.net/)
- [Fabric 1.21.11 build reference](https://github.com/FabricMC/fabric-docs/blob/main/reference/1.21.11/build.gradle)
- [NeoForge 1.21.11 MDK](https://github.com/NeoForgeMDKs/MDK-1.21.11-NeoGradle)
- [NeoForge documentation](https://docs.neoforged.net/docs/1.21.11/)
- [Modrinth API](https://docs.modrinth.com/api/operations/searchprojects/)
