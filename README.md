# ResourcePack Manager

A client-side Minecraft mod project for **Minecraft Java Edition 1.21.11**, targeting **Fabric** and **NeoForge**.

The active project is in `resourcepack-manager/`. The repository root also contains an older Holy Church Forge 1.16.5 project; it is separate from ResourcePack Manager.

## Project details

- **Minecraft:** 1.21.11
- **Java:** 21
- **Mod release version:** 0.1.0 (early development)
- **Loaders:** Fabric and NeoForge
- **In-game key:** `K`

## Features and development status

- Minecraft-style manager screen / entry point.
- Opens the game's built-in resource-pack selection screen.
- Separate loader projects for Fabric and NeoForge.

Planned work that is **not yet complete or runtime-verified**:
- Save and organize custom resource-pack groups.
- Browse and download packs from Modrinth in-game.
- Activate saved groups in one click.
- Detect supported shader loaders and integrate shader-pack management where possible.

## Build

Install **JDK 21** and use IntelliJ IDEA or Gradle.

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

Built JAR files are normally written to each loader project's `build/libs/` directory.

## Notes

- Resource packs can be managed through Minecraft's native pack system.
- Shader-pack activation depends on a compatible shader mod; Minecraft does not provide a universal shader-pack activation API.
- This is an early development build. Test in a backup profile/world before using it in a regular installation.

## References

- [Fabric documentation](https://docs.fabricmc.net/)
- [NeoForge documentation for 1.21.11](https://docs.neoforged.net/docs/1.21.11/)
- [Modrinth API documentation](https://docs.modrinth.com/api/operations/searchprojects/)
