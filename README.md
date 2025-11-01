# Timer

---

## DE

Timer ist ein hochentwickelter Minecraft-Timer für Spigot/Paper-Server, der direkt in der ActionBar angezeigt wird. Das Plugin unterstützt einen **dynamischen Farbverlauf**, der von der gewählten Basisfarbe zu einer dunkleren Version führt, und bietet einen **Schatteneffekt** für maximale Lesbarkeit. Der Timer zählt hoch, zeigt nur die notwendigen Zeiteinheiten an und kann automatisch bei bestimmten Advancements gestoppt werden.  

---

### Features

- Hochzählender Timer in der ActionBar  
- Farbverlauf innerhalb der gewählten Basisfarbe  
- Schatteneffekt für bessere Sichtbarkeit  
- Dynamische Zeitanzeige:
  - Sekunden immer  
  - Minuten, Stunden, Tage nur wenn > 0  
- Commands:
  - `/timer start` – startet den Timer  
  - `/timer pause` – pausiert den Timer  
  - `/timer restart` – startet den Timer neu  
  - `/timer stop` – stoppt den Timer permanent  
  - `/timer colour <color>` – ändert die Timerfarbe (TabCompletion mit Farbnamen)  
  - `/timer reload` – lädt die Config neu  
  - `/timer goal <advancement>` – setzt ein Ziel für automatische Timerbeendigung  
- TabCompleter für einfache Command-Nutzung  
- Standardfarbe: `dark_green`  
---
## Installation

1. Lade das Plugin herunter und lege die `.jar` in deinen `plugins`-Ordner.  
2. Starte oder lade den Server neu.  
3. Passe die `config.yml` an (optional).  
---
## Config

```yaml
base-color: dark_green
```
---

## EN


Timer is an advanced Minecraft timer plugin for Spigot/Paper servers that displays the timer directly in the ActionBar. It features a **dynamic color gradient** that transitions from the chosen base color to a darker shade, and includes a **shadow effect** for better visibility. The timer counts up, shows only relevant time units, and can automatically stop upon reaching specific advancements.  

---

## Features

- Up-counting timer displayed in the ActionBar  
- Color gradient based on the selected base color  
- Shadow effect for enhanced readability  
- Dynamic time display:
  - Seconds always shown  
  - Minutes, hours, days shown only when > 0  
- Commands:
  - `/timer start` – starts the timer  
  - `/timer pause` – pauses the timer  
  - `/timer restart` – restarts the timer  
  - `/timer stop` – permanently stops the timer  
  - `/timer colour <color>` – changes the timer color (TabCompletion with color names)  
  - `/timer reload` – reloads the config  
  - `/timer goal <advancement>` – sets a goal for automatic timer stopping  
- TabCompleter for easy command usage  
- Default color: `dark_green`  
---
## Installation

1. Download the plugin and place the `.jar` file into your `plugins` folder.  
2. Start or reload the server.  
3. Optionally, adjust the `config.yml` to customize your timer.  
---
## Config

```yaml
base-color: dark_green
