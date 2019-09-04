package register;

import io.anuke.arc.*;
import io.anuke.arc.util.*;
import io.anuke.mindustry.*;
import io.anuke.mindustry.content.*;
import io.anuke.mindustry.entities.type.*;
import io.anuke.mindustry.game.EventType.*;
import io.anuke.mindustry.game.Team;
import io.anuke.mindustry.gen.*;
import io.anuke.mindustry.plugin.Plugin;

public class RegistrationPlugin extends Plugin{

    //register event handlers and create variables in the constructor
    public RegistrationPlugin(){
        //sqliteDB db = new sqliteDB();
        //db.closeConnection();
        Events.on(PlayerJoin.class, event ->{

            //check if playeruuid, ip already in db
            Team no_core = getTeamNoCore(event.player);
            event.player.setTeam(no_core);
            Call.onPlayerDeath(event.player);
            event.player.sendMessage("[sky]You will need to login with [accent]/log_in[] to get access to the server.[] More info on the indielm discordserver.");
        });
    }

    //register commands that run on the server
    @Override
    public void registerServerCommands(CommandHandler handler){

    }

    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler){
        handler.<Player>register("login", "<username> <password>", "log in to get access to the server",
                (args, player)->{
            //command to log in
            if (!Vars.state.teams.get(player.getTeam()).cores.isEmpty()){
                player.sendMessage("[orange]You are already logged in");
                return;
            }
            sqliteDB db = new sqliteDB();
            if (db.check(args[0], args[1], player.uuid, "0.0.0.0")){
                //set team normal way
                player.sendMessage("[green]succes![] Use [accent] /team [] to change team.");
                if (Vars.state.rules.pvp){
                    //assign team
                    changeTeam(player);
                } else {
                    player.setTeam(Vars.defaultTeam);
                    Call.onPlayerDeath(player);
                }
                player.sendMessage("Your ip and uuid are saved to skip the log_in procedure next time.");

            } else {
                player.sendMessage("[scarlet]Wrong login and pwd combination[]\nOnly use lowercase letters.");
            }
            db.closeConnection();
        });


        handler.<Player>register("team", "", "change team", (args, player)->{
            if (!Vars.state.rules.pvp){
                player.sendMessage("[scarlet]Only available in pvp.");
                return;
            } else if (Vars.state.teams.get(player.getTeam()).cores.isEmpty()){
                player.sendMessage("[accent]Login first []");
                return;
            }

            //change team
            changeTeam(player);
        });
    }

    private Team getTeamNoCore(Player player){
        int index = player.getTeam().ordinal()+1;
        while (index != player.getTeam().ordinal()){
            if (index >= Team.all.length){
                index = 0;
            }
            if (Vars.state.teams.get(Team.all[index]).cores.isEmpty()){
                return Team.all[index]; //return a team without a core
            }
            index++;
        }
        //if no team without a core found, return assigned team
        //TODO: make a new team
        return player.getTeam();
    }

    private void changeTeam(Player player){
        int index = player.getTeam().ordinal()+1;
        while (index != player.getTeam().ordinal()){
            if (index >= Team.all.length){
                index = 0;
            }
            if (!Vars.state.teams.get(Team.all[index]).cores.isEmpty()){
                player.setTeam(Team.all[index]);
                break;
            }
            index++;
        }
        //kill player
        Call.onPlayerDeath(player);
    }
}
