# 🌌 Aetherium Odyssey — Mod Minecraft (Fabric & Forge 1.21.1)

[![GitHub Release](https://img.shields.io/github/v/release/sidounasser/Aetherium-Odyssey?include_prereleases&style=flat-square)](https://github.com/sidounasser/Aetherium-Odyssey/releases)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-blue?style=flat-square)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Loader-Fabric%20%2F%20Forge-orange?style=flat-square)](https://fabricmc.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](LICENSE)

**Aetherium Odyssey** est un mod RPG, Magie, Exploration et Boss ultra complet pour **Minecraft 1.21.1 (Fabric & Forge)**.

## 📦 Téléchargement & Installation

Les versions précompilées finales sont disponibles directement dans la section **[Releases GitHub](https://github.com/sidounasser/Aetherium-Odyssey/releases)** :

### Pour Fabric (Recommandé) :
1. Télécharge **`aetherium-odyssey-1.0.0.jar`** depuis les [Releases](https://github.com/sidounasser/Aetherium-Odyssey/releases).
2. Installe **Fabric Loader** pour Minecraft 1.21.1 et ajoute **Fabric API** dans ton dossier `.minecraft/mods`.
3. Place **`aetherium-odyssey-1.0.0.jar`** dans `.minecraft/mods`.

### Pour Forge :
1. Télécharge **`aetherium-odyssey-forge-1.21.1-3.0.0.jar`** depuis les [Releases](https://github.com/sidounasser/Aetherium-Odyssey/releases).
2. Installe **Minecraft Forge 1.21.1** (version 52.1+).
3. Place le fichier jar dans ton dossier `.minecraft/mods`.

## 🛠️ Compilation depuis les sources

### Compiler la version Fabric :
Nécessite JDK 21 installé.
```bash
# Windows
.\gradlew build

# Linux / macOS
./gradlew build
```
Le fichier JAR généré se trouvera dans `build/libs/aetherium-odyssey-1.0.0.jar`.

### Compiler la version Forge :
```powershell
.\build-forge.ps1
```
Le fichier JAR généré se trouvera dans `build/forge/jar/aetherium-odyssey-forge-1.21.1-3.0.0.jar`.

## ✨ Contenu du Mod
- **3 Nouveaux Minerais multi-dimensionnels** :
  - **Minerai d'Aetherium** (Overworld & Abîmes, `Y -64` à `Y 48`)
  - **Minerai de Pyrosite** (Nether, `Y 10` à `Y 115`)
  - **Minerai de Néantite** (The End, `Y 15` à `Y 70`)
- **4 Armes Légendaires à compétences actives (Clic Droit) & passifs de frappe** :
  - **Lame Astrale d'Aetherium** (*Onde Céleste* + Lévitation)
  - **Espadon Solaire de Pyrosite** (*Supernova Ignée* + Résistance au Feu)
  - **Faux Vampirique du Néant** (*Drain de Vie +2.5 PV/coup* + *Téléportation Pas de l'Ombre* à 14 blocs)
  - **Hallebarde Divine de l'Archonte** (*Jugement de l'Archonte* : Tempête de foudre de zone + fauchage + soin)
- **Outils d'Aetherium** :
  - **Pioche Sismique d'Aetherium** : Minage **3x3** automatique (s'accroupir pour miner en 1x1) + **Sonde Sismique (Radar à minerais)** au Clic Droit !
  - Hache, Pelle et Houe Célestes d'Aetherium.
- **Sceptres Magiques & Reliques Passives** :
  - **Sceptre des Tempêtes** (Foudre ciblée à 32 blocs)
  - **Sceptre des Météores Solaires** (Salve de 3 boules de feu)
  - **Sceptre de Grâce Céleste** (Soin de zone +5 cœurs, purification des effets négatifs & Régénération II)
  - **Talisman des Vents Célestes** (Propulsion aérienne *Dash* + Chute Lente)
  - **Anneau de Vélocité Astrale** (Passif : Vitesse II, Hâte II, Saut Amélioré)
  - **Amulette Immortelle du Phénix** (Passif : Résistance au Feu + Sauvetage automatique sous 4 cœurs)
  - **Charme Magnétique d'Aetherium** (Passif : Aimant à objets et XP dans un rayon de 10 blocs, impulsion à 18 blocs au clic droit)
- **Armure Céleste d'Aetherium (Bonus de Panoplie 4/4 : *Égide Céleste*)** :
  - Immunité aux dégâts de chute, Vision Nocturne, Régénération, Force I, Résistance I, Vitesse I, Immunité au Wither & au Poison, et aura de particules stellaires.
- **Blocs Rituels & Fonctionnels** :
  - **Autel d'Ascension Céleste** : Invocation du Boss et transmutation de Diamants / Pommes Dorées.
  - **Pylône de Régénération Céleste** : Soin de zone permanent pour les joueurs et dégâts sacrés aux monstres proches.
  - **Ascenseur Téléporteur d'Aetherium** : Téléportation verticale instantanée jusqu'à 64 blocs (Clic droit = Monter, S'accroupir = Descendre).
  - **Lampe Stellaire d'Aetherium** & Blocs de stockage lumineux.
- **Le Boss : L'Archonte d'Aetherium (3 Phases)** :
  - Invoqué avec le **Sceau d'Invocation de l'Archonte** sur l'**Autel d'Ascension**.
  - Combat dynamique en **3 Phases** avec barre de boss personnalisée et butin mythique garanti (**Cœur Céleste de l'Archonte**, **Ambroisie Céleste**, lingots).
- **Guide intégré & Commandes** :
  - Objet **Codex d'Aetherium** (Clic droit pour lire l'encyclopédie en jeu).
  - Commandes : `/aetherium guide`, `/aetherium codex`, `/aetherium kit`, `/aetherium summon_boss`.
