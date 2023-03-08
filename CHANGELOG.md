# Changelog
All notable changes to this project will be documented in this file.

## Naming convention:
- Added: new Features
- Changed: changes in existing features
- Deprecated: features that will be removed in the future
- Removed: deprecated features that where removed in actual version
- Fixed: bug-fixes
- Security prompt user to update in case of safety issues

## [1.0.0] - 2023-02-17

### Added
- Enemys take and deal damage
- Different bullet colors and shapes with networking functionality
- Maximum bullet range
- Heat level mechanic for GameMaster
- In-game-options
- Sound effects and music
- Choose random GameMaster and start timer mechanic
- Demo Junit test
- .yml file with 2 jobs for gitlab CI/CD pipeline 

### Fixed
- ClassCastException regardes to Box2D
- Hostile bullet duplicating bug
- VrPlayer healthbar visibility


## [0.0.9] - 2023-01-04

### Added
- Game master player that can spawn enemy units during gameplay
- Universal enemy sprite to replace placeholder character sprite
- Separate player sprite for each player class
- Health bar is now networked and shown to all other players
- Enemy Networking
- Various sound assets

### Fixed
- Local and networked bullet speeds are now synchronized
- Bullet speed is no longer unusually slow


## [0.0.8.1] - 2022-12-28

### Fixed
- Speed of networked bullets being faster than local bullets

### Changed
- HealthSystem methods moved to GameCharacter


## [0.0.8] - 2022-12-18

### Added
- Different weapon types based on their projectile pattern: Linear (original "Weapon") Arc, Radial, Wave
- Weapon magazine size
- Right-click "discharge" attack for weapons. Only implemented for radial weapons at the moment
- Networking for weapon projectiles
- Functional health for player characters
- Username & IP-Address selection
- Plenty of new UI assets

## Changed
- Weapons now use a fire rate instead of a shoot delay
- Reloading is now based on a proper timer

## Fixed
- Weapons always aiming towards the center of the screen due to them using screen coordinates instead of world coordinates
- Radial weapons not distributing their bullets in a ring equally
- Bullets crashing the game by being updated while the Box2D World instance is in a locked state
- Menu screens now correctly resize with the window



## [0.0.7] - 2022-12-05
### Added
- Networking for Characters
- enemy AI beginnings
- start Screen
- HUD

### Changed
- Changed hitboxes to Box2D
- walking by force



## [0.0.6] - 2022-11-07
### Added
- Dummy-assets/ animations: background, bullet, rangedDD, weapon
- weapon texture around character
- weapon atlas
- weapon rotation
- bullet rotation
- temporary weaponEnd calculation

### Changed
- Scaling of character and turning
- bullet speed

### Fixed
- loadingscreen animation
- bullet rotation



## [0.0.5] - 2022-11-03
### Added
- Map
- Collision with Map
- shooting method
- Bullet collision with map



## [0.0.4] - 2022-10-28
### Added
- TextureAtlas, AssetManager, LoadingScreen asset
### Fixed
- Character middle calculation



## [0.0.3] - 2022-10-26
### Added
- camera
- viewport
- aiming mechanism
- character middle variable
### Changed
- aim method doesn't require delta time



## [0.0.2] - 2022-10-25
### Added
- class structure
- simple movement with dummy asset



## [0.0.1] - 2022-10-13
### Added
- initial libgdx project created with libgdx project generator: https://libgdx.com/wiki/start/project-generation

## [Unreleased]
