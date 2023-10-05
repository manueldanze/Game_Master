# Game Master

<br>
<br>

### Table of Contents

* [Description](#description)
* [Installation/ Startup](#installation)
* [Contributing](#contributing)
* [Acknowledgements](#acknowledgements)

<br>
<br>

<!-- DESCRIPTION -->
### Description
GameMaster is a Bullet-hell arena multiplayer pixel-art game with a special twist.

There are 3 Players and a Game Master in an arena-like setting. The Players have to defeat all enemys. The Game Master's goal is to eliminate all players through spawning enemys. The Players have won when the time has expired and at least one player is alive.

At the beginning the players have to choose between different prebuild characters with specific attributes like there is a tank with low damage but great health or a melee damage dealer with high mobility and damage but low health.

Game start: One random player is choosen to take the role of the Game Master which takes controls of the arena and can spawn enemys, the spawn-rate is limited by the heat-level which increases when the Game Master spawns enemys rapidly.

The game network system is a client-server model with one player hosting the server and the others joining in.

<br>
<br>

<!-- INSTALLATION -->
### Installation

#### Build and test
> Execute following commands to create a executable .jar
- Neccessary: build project and a fatJar executable `./gradlew clean build; ./gradlew customFatJar`
- Optional: run tests `./gradlew clean test`
>> Make sure Java 18.0.1 is installed and set as environment variable
<br>

#### Run with executable .jar: Start multiplayer round with friends
- All players must be connected to the same network
- Locate the executable `desktop/build/libs/desktop-1.0.jar` and run with `java -jar desktop-1.0.jar` or doubleclick
- Click through menu till you are prompted to choose "Host-Button" or "Join-Button"
- Choose "Host" if you want to host the server or "Join" if one of the other players has already started the server
- *HOST:* your IP address is displayed > send it to your friends to join > press "Start-Button" to create server and join
- - InGame: press `ESC` to open in-Game-Options > press "Start-Button" to trigger random Game-Master choice and start timer (the "Start-Button" is only available for the Host of the game)
- *JOIN*: enter the IP address of the Host > press "Start-Button" to join > if nothing is entered you create your own game without network features
> Make sure to use the correct IP address to connect, maybe double check with `ipconfig`

#### Run with executable .jar: Simulate a multiplayer round on a single machine
- You need to run two game instances of the game, run the executable two times
- Click through menu till you are prompted to choose "Host" or "Join"
- Choose "Host" on one of the instances and "Join" on the other instance
- 
<br>
    
#### Controls
- `LMB` Shoot/ check menu buttons
- `ESC` Open in-Game-Menu
- `W/A/S/D` Move character

<br>

### Known issues
- When you run the game via the executable .jar and you have hosted a game, make sure that when you stop playing to close
  the game with `ESC` and "Quit-Button" to kill the game AND the server runtime otherwise the server will keep on running in the background.
> When starting a new server you might get this `RuntimeException: java.net.BindException: Address already in use: bind`
> this means an old server is still running in background and you have to kill the java runtime of that server first via Task Manager.
- Player sprite is not disappearing when he is chosen to be GameMaster
- In-Game-Options not clickable in GameMaster mode (important for closing game and server completely, kill server via task manager)
- Killed enemys will not despawn on GameMasters screen
- Healthbars of other players are temporarly not dynamic because of yet unknown bug
- Missing win conditions

<br>
<br>

<!-- CONTRIBUTING -->
### Contributing
The project was created with Java version 18.0.1.1 and Gradle version 8.0.1 in IntelliJ IDEA. 
LibGdx and Box2D were used as the game development framework. 
The assets where created with Adobe Photoshop, Adobe Animate, Aseprite and Tiled, 
also license-free assets from itch.io where used.

<br>
<br>

<!-- ACKNOWLEDGEMENTS -->
### Authors and Acknowledgements
- This game was created by Furkan E., Despoina S., Bianca K., Georg B., Tim D. and Manuel Danze 
as a Java Software Project in 3.Semester Bachlor of Computer Science Media at Stuttgart Media University in 2023.

***
