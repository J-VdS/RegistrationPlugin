### Building a Jar

`gradlew jar` / `./gradlew jar`

Output jar should be in `build/libs`.

### Dependencies:
java sqlite driver: https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/3.28.0

### Installing

Simply place the output jar from the step above in your server's `config/plugins` directory and restart the server.
List your currently installed plugins by running the `plugins` command.
