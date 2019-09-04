### discordbot
install python 3.6 or higher

run this command in your commandprompt: `pip install -U discord.py` 

To run this bot you will need to make a token.txt file first. This file should contain:
```
token
your_id
test.db
```
The last line should always be `test.db`.

### Finally starting the bot:
copy and paste `loop.py`,` bot.py` and `token.txt` in the server directory. (directory containing run_server.bat, run_server.sh or server.jar)
`python loop.py`

The bot will run forever until you use !shutdown in your discord server.

### Building a Jar

`gradlew jar` / `./gradlew jar`

Output jar should be in `build/libs`.

### Dependencies:
java sqlite driver: https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/3.28.0

### Installing

Simply place the output jar from the step above in your server's `config/plugins` directory and restart the server.
List your currently installed plugins by running the `plugins` command.
