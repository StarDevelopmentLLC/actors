package com.stardevllc.actors.plugin;

import com.stardevllc.actors.Actor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public class ActorsPlugin extends JavaPlugin {
    
    private static final int PAGE_SIZE = 7;
    
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("actors")) {
            return false;
        }
        
        Actor sender = Actor.create(s);
        
        if (!sender.hasPermission("staractors.admin")) {
            sender.sendColoredMessage("&cYou do not have permission to perform that command.");
            return true;
        }
        
        if (!(args.length > 0)) {
            sender.sendColoredMessage("&cYou must provide a sub command");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("list")) {
            if (!sender.hasPermission("staractors.admin.list")) {
                sender.sendColoredMessage("&cYou do not have permission to perform that command.");
                return true;
            }
            
            List<String> actorsList = new LinkedList<>();
            for (Actor actor : Actor.CACHE.values()) {
                actorsList.add("&eType; &a" + actor.getClass().getSimpleName().replace("Actor", "") + "  &eName: &a" + actor.getName());
            }

            if (actorsList.isEmpty()) {
                sender.sendColoredMessage("&cNo results found.");
                return true;
            }
            
            int page = 0;
            if (args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]) - 1;
                } catch (NumberFormatException e) {
                    sender.sendColoredMessage("&cInvalid number provided: " + args[1]);
                    return true;
                }
            }

            int startingOffset = page * PAGE_SIZE;

            if (actorsList.size() < startingOffset) {
                sender.sendColoredMessage("&cNo more results");
                return true;
            }
            
            int totalPages = actorsList.size() / PAGE_SIZE + 1;
            
            sender.sendColoredMessage("&eList of Actors page " + (page + 1) + "/" + totalPages);
            for (int i = 0; i < startingOffset + PAGE_SIZE; i++) {
                if (i + startingOffset < actorsList.size()) {
                    sender.sendColoredMessage("  &8- &r" + actorsList.get(startingOffset + i));
                } else {
                    break;
                }
            }
            if (startingOffset + PAGE_SIZE < actorsList.size()) {
                sender.sendColoredMessage("&eType /" + label + " list " + (page + 1) + " for more results");
            }
        }
        
        return true;
    }
}