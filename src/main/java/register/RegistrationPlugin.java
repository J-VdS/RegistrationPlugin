package register;

import io.anuke.arc.*;
import io.anuke.arc.util.*;
import io.anuke.mindustry.*;
import io.anuke.mindustry.content.*;
import io.anuke.mindustry.entities.type.*;
import io.anuke.mindustry.game.EventType.*;
import io.anuke.mindustry.game.Team;
import io.anuke.mindustry.gen.*;

import io.anuke.mindustry.net.Packets;
import io.anuke.mindustry.plugin.Plugin;

public class RegistrationPlugin extends Plugin{

    //register event handlers and create variables in the constructor
    public RegistrationPlugin(){
        Events.on(PlayerJoin.class, event ->{
            //kick players if gameover happened
            sqliteDB db = new sqliteDB();
            if (!event.player.isAdmin && db.isAdmin(event.player.uuid)){
                Vars.netServer.admins.adminPlayer(event.player.uuid, event.player.usid);
                event.player.isAdmin = true;
            } else if(event.player.isAdmin && !db.isAdmin(event.player.uuid)){
                Vars.netServer.admins.unAdminPlayer(event.player.uuid);
                event.player.isAdmin = false;
            }
            //if gameover kick
            if (Vars.state.gameOver){
                Call.onInfoMessage(event.player.con.id, "You got kicked because it was gameover. \n\nYou can try again in [accent]30 seconds[].");
                Call.onKick(event.player.con.id, Packets.KickReason.kick);
                return;
            }
            //check if uuid in db
            if (db.uuidCheck(event.player.uuid)){
                event.player.sendMessage("[green]Login succes via your device information.[]\n[sky]Enjoy your game![]");
                db.closeConnection();
                return;
            } else {
                db.closeConnection();
            }

            Team no_core = getTeamNoCore(event.player);
            event.player.setTeam(no_core);
            Call.onPlayerDeath(event.player);
            Call.onInfoMessage(event.player.con.id, "[sky]You will need to login with [][accent]/login <username> <password>[][sky] to get access to the server.[] More info on the indielm discordserver.");
        });

        Events.on(PlayerLeave.class, event ->{
           sqliteDB db = new sqliteDB();
           db.playerLeave(event.player.uuid);
           db.closeConnection();
        });

        Events.on(GameOverEvent.class, event ->{
            //put the players who aren't yet logged in back on a team with no core
            sqliteDB db = new sqliteDB();
            for (Player p: Vars.playerGroup.all()){
                System.out.println(p.name);
                if (!db.loggedIn(p.uuid)){
                    //kick the players
                    Call.onInfoMessage(p.con.id, "You got kicked because it was gameover. \nAsk for help how to login on the indielm discordserver.\n\nYou can try again in [accent]30 seconds[].");
                    Call.onKick(p.con.id, Packets.KickReason.kick);
                }
            }
            db.closeConnection();
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
                //check if isAdmin in db
                if (db.isAdmin(args[0], args[1]) && player.isAdmin == false){
                    Vars.netServer.admins.adminPlayer(player.uuid, player.usid);
                    player.sendMessage("[sky]Adminstatus applied");
                    player.isAdmin = true;
                }
                //set team normal way
                player.sendMessage("[green]succes![] Use [accent] /team [] to change team.");
                if (Vars.state.rules.pvp){
                    //assign team
                    changeTeam(player);
                } else {
                    player.setTeam(Vars.defaultTeam);
                    Call.onPlayerDeath(player);
                }
                player.sendMessage("Some device information is stored to skip the login procedure next time.");

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
