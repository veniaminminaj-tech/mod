# ResourcePack Manager — Minecraft 1.21.11

A client-side resource-pack organizer with a Minecraft-style screen, a resource-pack picker entry point, and separate Fabric / NeoForge builds.

## Target versions verified
- Minecraft: 1.21.11
- Java: 21
- Fabric: official 1.21.11 docs reference Fabric API 0.141.1+1.21.11 and the remapping Loom plugin for 1.21.11 and older.
- NeoForge: official 1.21.11 MDK lists NeoForge 21.11.45.

References:
- https://docs.fabricmc.net/develop/porting/current
- https://github.com/FabricMC/fabric-docs/blob/main/reference/1.21.11/build.gradle
- https://github.com/NeoForgeMDKs/MDK-1.21.11-NeoGradle
- https://docs.neoforged.net/docs/1.21.11/

## Builds
Each loader is an independent Gradle project: `fabric/` and `neoforge/`.
Use JDK 21 and open the relevant directory in IntelliJ IDEA.

## Controls
Press **K** in-game to open ResourcePack Manager.

## Current scope
This is an initial integration branch. The Fabric UI opens a manager screen and routes to the native pack picker. Saved group persistence, the in-game Modrinth catalog, automated activation of named groups, and shader-loader adapters remain to be implemented and runtime-tested. Shader packs cannot be activated universally: the mod must detect a compatible shader mod and otherwise only manage/install shader ZIPs.

Modrinth API docs: https://docs.modrinth.com/api/operations/searchprojects/
