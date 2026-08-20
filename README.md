# SHINE Hit Messages

Client-side Fabric mod for Minecraft 1.21.8 / Fabric Loader 0.19.3.

Shows a short combat-feedback text on the **right side of the screen**:

| Situation                              | Text   | Color  |
|-----------------------------------------|--------|--------|
| Real vanilla critical hit                | CRIT   | Red    |
| Normal hit while sprinting (non-crit)    | YEAHH  | Green  |
| Any other normal hit                     | AHH..  | Yellow |

Each message stays visible for ~0.65 seconds. Purely visual — it never touches
damage, timing, or any gameplay logic, and it never draws in chat, the action
bar, or screen center.

## How to build the JAR yourself

You need **Java 21** and an internet connection (Gradle needs to download
Minecraft 1.21.8, the Yarn mappings, and Fabric API — none of that can be
bundled in this project ahead of time due to Mojang/Fabric distribution
terms).

1. Open a terminal in this project folder.
2. Run:
   - Windows: `gradlew.bat build`
   - macOS/Linux: `./gradlew build`
3. The first run downloads dependencies and decompiles/remaps Minecraft, so
   it can take several minutes. Subsequent builds are fast.
4. The finished mod jar will be at:
   `build/libs/shine-hit-messages-1.0.0.jar`

(This project doesn't include the Gradle wrapper jar binary — if
`gradlew`/`gradlew.bat` complains about a missing wrapper, run
`gradle wrapper --gradle-version 8.10` once with a system-installed Gradle,
or open the folder in IntelliJ IDEA with the Fabric/Gradle plugin, which sets
this up automatically.)

## Installing the built JAR

1. Make sure you have **Fabric Loader 0.19.3** (or newer, matching Minecraft
   1.21.8) installed via the Fabric installer.
2. Install **Fabric API** for 1.21.8 in your mods folder as well (this mod
   depends on it).
3. Copy `shine-hit-messages-1.0.0.jar` into:
   - Windows: `%appdata%\.minecraft\mods`
   - macOS: `~/Library/Application Support/minecraft/mods`
   - Linux: `~/.minecraft/mods`
4. Launch Minecraft using the Fabric profile.

## Project layout

```
build.gradle
settings.gradle
gradle.properties
src/main/java/com/shine/hitmod/ShineHitMessagesClient.java
src/main/resources/fabric.mod.json
```
