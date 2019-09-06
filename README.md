### Discordbot
install python 3.6 or higher

run this command in your commandprompt: `pip install -U discord.py` 

To run this bot you will need to make a token.txt file first. This file should contain:
```
token
serverid
test.db
```
The last line should always be `test.db`.
You also need to add 2 roles: Registered and Admin. This is neccessary, otherwise the bot will not add the roles! The prefix of the bot is `..`

### Finally starting the bot:
copy and paste `loop.py`,` bot.py`, `sqlite_mindustry.py` and `token.txt` in the server directory. (directory containing run_server.bat, run_server.sh or server.jar). You can test the bot via `python bot.py`. If no error appears, close the python application and run:
`python loop.py`

The bot will run forever until you use ..shutdown in your discord server.

### Filestructure
```
server
 |- config
 |    |- plugins
 |          |- registrationplugin.jar
 |    |- ...
 |- server.jar
 |- run_server.sh
 |- run_server.bat
 |- loop.py
 |- bot.py
 |- sqlite_mindustry.py
 |- token.txt
 
```
When you startup the bot or a player joins `test.db` gets generated.

### commands
In-game:
* `/login <username> <password>`
* `/team` 
Discord:
* `..signup <username> <password>` --> needs to be in DM
* `..showlogin`
* `..changelogin <new_username> <new_password>`
* `..deletelogin`

### Building a Jar

`gradlew jar` / `./gradlew jar`

Output jar should be in `build/libs`.

### Dependencies:
java sqlite driver: https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/3.28.0

### Installing

Simply place the output jar from the step above in your server's `config/plugins` directory and restart the server.
List your currently installed plugins by running the `plugins` command.

### TODO
* [ ] make a new team instead of searching for one without a core (This only happens during pvp when there are 6 different teams on the map)
* [ ] when a player gets admin in game he needs to remain it after leaving.
