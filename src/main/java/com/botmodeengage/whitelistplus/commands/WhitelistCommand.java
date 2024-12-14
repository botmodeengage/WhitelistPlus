package com.botmodeengage.whitelistplus.commands;

import com.botmodeengage.whitelistplus.WhitelistPlus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class WhitelistCommand implements CommandExecutor {
    private WhitelistPlus plugin;

    public WhitelistCommand(WhitelistPlus plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length == 0){
            sender.sendMessage(ChatColor.YELLOW + "WhitelistPlus Help:");
            sender.sendMessage("/wlp help - Lists available commands");
            sender.sendMessage("/wlp add <usernames> - Add user(s) to whitelist");
            sender.sendMessage("/wlp remove <usernames> - Remove user(s) from whitelist");
            sender.sendMessage("/wlp awaitmsg <message> - Set await message");
            sender.sendMessage("/wlp denymsg <message> - Set deny message");
            sender.sendMessage("/wlp on - Enable whitelist");
            sender.sendMessage("/wlp off - Disable whitelist");
            sender.sendMessage("/wlp status - Show whitelist status");
            return true;
        }
        switch(args[0].toLowerCase()){
            case "help":
                sender.sendMessage(ChatColor.YELLOW + "WhitelistPlus Help:");
                sender.sendMessage("/wlp help - Lists available commands");
                sender.sendMessage("/wlp add <usernames> - Add user(s) to whitelist");
                sender.sendMessage("/wlp remove <usernames> - Remove user(s) from whitelist");
                sender.sendMessage("/wlp awaitmsg <message> - Set await message");
                sender.sendMessage("/wlp denymsg <message> - Set deny message");
                sender.sendMessage("/wlp on - Enable whitelist");
                sender.sendMessage("/wlp off - Disable whitelist");
                sender.sendMessage("/wlp status - Show whitelist status");
                break;
            case "add":
                if(args.length < 2){
                    sender.sendMessage(ChatColor.RED + "Usage: /wlp add <usernames>");
                    return true;
                }
                for(int i=1;i<args.length;i++){
                    String name = args[i].replace(",", "").trim();
                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);
                    if(!player.isWhitelisted()){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + name);
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "Added to whitelist.");
                break;
            case "remove":
                if(args.length < 2){
                    sender.sendMessage(ChatColor.RED + "Usage: /wlp remove <usernames>");
                    return true;
                }
                for(int i=1;i<args.length;i++){
                    String name = args[i].replace(",", "").trim();
                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);
                    if(player.isWhitelisted()){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist remove " + name);
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "Removed from whitelist.");
                break;
            case "awaitmsg":
                if(args.length < 2){
                    sender.sendMessage(ChatColor.RED + "Usage: /wlp awaitmsg <message>");
                    return true;
                }
                String awaitMsg = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace("&", "§");
                plugin.getConfig().set("await-message", awaitMsg);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.GREEN + "Await message set.");
                break;
            case "denymsg":
                if(args.length < 2){
                    sender.sendMessage(ChatColor.RED + "Usage: /wlp denymsg <message>");
                    return true;
                }
                String denyMsg = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace("&", "§");
                plugin.getConfig().set("deny-message", denyMsg);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.GREEN + "Deny message set.");
                break;
            case "on":
                plugin.getConfig().set("whitelist-status", true);
                plugin.saveConfig();
                Bukkit.setWhitelist(true);
                sender.sendMessage(ChatColor.GREEN + "Whitelist enabled.");
                break;
            case "off":
                plugin.getConfig().set("whitelist-status", false);
                plugin.saveConfig();
                Bukkit.setWhitelist(false);
                sender.sendMessage(ChatColor.GREEN + "Whitelist disabled.");
                break;
            case "status":
                boolean status = plugin.isWhitelistEnabled();
                String statusMsg = ChatColor.YELLOW + "Whitelist is " + (status ? ChatColor.GREEN + "on" : ChatColor.RED + "off");
                sender.sendMessage(statusMsg);
                break;
            case "accept":
                if(args.length < 2){
                    sender.sendMessage(ChatColor.RED + "Usage: /wlp accept <username>");
                    return true;
                }
                String acceptName = args[1];
                OfflinePlayer acceptPlayer = Bukkit.getOfflinePlayer(acceptName);
                if(!acceptPlayer.isWhitelisted()){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + acceptName);
                }
                plugin.getPending().remove(acceptName);
                plugin.getDenied().remove(acceptName);
                if(acceptPlayer.isOnline()){
                    acceptPlayer.getPlayer().sendMessage(plugin.getAwaitMessage());
                }
                sender.sendMessage(ChatColor.GREEN + acceptName + " has been whitelisted.");
                break;
            case "deny":
                if(args.length < 2){
                    sender.sendMessage(ChatColor.RED + "Usage: /wlp deny <username>");
                    return true;
                }
                String denyName = args[1];
                plugin.getDenied().add(denyName);
                plugin.getPending().remove(denyName);
                OfflinePlayer denyPlayer = Bukkit.getOfflinePlayer(denyName);
                if(denyPlayer.isOnline()){
                    denyPlayer.getPlayer().kick(Component.text(plugin.getDenyMessage()));
                }
                sender.sendMessage(ChatColor.GREEN + denyName + " has been denied from whitelist.");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /wlp help for help.");
                break;
        }
        return true;
    }
}
