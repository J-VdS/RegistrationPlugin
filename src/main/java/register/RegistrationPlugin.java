package register;

import io.anuke.arc.*;
import io.anuke.arc.util.*;
import io.anuke.mindustry.*;
import io.anuke.mindustry.content.*;
import io.anuke.mindustry.entities.type.*;
import io.anuke.mindustry.game.EventType.*;
import io.anuke.mindustry.gen.*;
import io.anuke.mindustry.plugin.Plugin;

public class RegistrationPlugin extends Plugin{

    //register event handlers and create variables in the constructor
    public RegistrationPlugin(){
        //sqliteDB db = new sqliteDB();
        //db.closeConnection();
        
    }

    //register commands that run on the server
    @Override
    public void registerServerCommands(CommandHandler handler){

    }

    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler){

    }
}
