# Worlds

A lightweight yet powerful plugin for Paper servers (version 1.21+), designed to make world management simple, fast, and flexible. With Worlds, server administrators can create, import, and manage multiple worlds with ease, while controlling world-specific settings like game rules and plugin-provided flags.

---

## 🚀 Features

- **Create and Import Worlds**  
  Easily generate new worlds or import existing ones. Supports both normal and flat world types, along with custom environments.

- **World Editing**  
  Modify worlds after creation, including game rules and plugin-provided flags.

- **Lightweight Design**  
  Despite its simplicity, Worlds provides all the essential features without unnecessary overhead.

- **Pre-generate Chunks**  
  Pre-render chunks around world spawns to reduce lag and prevent chunk generation issues for players.

- **Flexible Configuration**  
  Each world can have its own settings saved in a structured JSON configuration, allowing easy backups and edits.

- **Async & Performance-Optimized**  
  Long-running tasks like pre-generating chunks are handled asynchronously, keeping your server responsive.

---

## 🧩 Dependencies

**Worlds** depends on [MendingCore](https://github.com/mending-dev/MendingCore). Please ensure you have this plugin installed on your server for **Worlds** to function properly.

---

## 📦 Commands

The plugin provides a main `/world` command with several subcommands:

- `/world create <name>` – Creates a new world.
- `/world import <name>` – Imports an existing world folder.
- `/world delete <name> [true/false]` – Deletes a world; pass `true` to also remove the world folder.
- `/world flag <world> <flag> <value>` – Edit settings and flags for a specific world.
- `/world teleport <world> [player]` – Teleport a player to a world's spawn-location.
- `/world list` – List all worlds.

> Note: All commands require the appropriate permissions (e.g. worlds.command.create), which can be configured via your server’s permission system.

---

## 🗒️ Configuration

Worlds stores world settings in a JSON-based configuration file. Each world entry includes:

- World type and environment
- Game rules (e.g., `doDaylightCycle`, `keepInventory`)
- Difficulty
- Built-in flags
- Pre-generate chunk radius

Example:

```json
{
  "world": {
    "type": "NORMAL",
    "environment": "NORMAL",
    "difficulty": "PEACEFUL",
    "gameRules": {
      "doDaylightCycle": true,
      "keepInventory": false
    },
    "flags": {
      "pvp": false
    },
    "preGenerateChunkRadius": 100
  }
}
```
---

## 📚 Documentation & Support

Detailed documentation, examples, and tutorials will be added soon alongside upcoming features.  
For questions or feedback, please open an issue on the GitHub repository.

---

## ⚖️ License

Worlds is licensed under the MIT License. See [LICENSE](LICENSE) for details.