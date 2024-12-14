package com.botmodeengage.whitelistplus.events;

import com.botmodeengage.whitelistplus.WhitelistPlus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PlayerJoinListener implements Listener {
    private WhitelistPlus plugin;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    public PlayerJoinListener(WhitelistPlus plugin){
        this.plugin = plugin;
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = new ObjectMapper();
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event){
        if(!plugin.isWhitelistEnabled()) return;
        String playerName = event.getName();
        if(plugin.getDenied().contains(playerName)){
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text(plugin.getDenyMessage()));
            return;
        }
        if(plugin.getPending().contains(playerName)){
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text(plugin.getAwaitMessage()));
            return;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if(player.isWhitelisted()){
            return;
        }
        plugin.getPending().add(playerName);
        String ip = event.getAddress().getHostAddress();
        String apiUrl = String.format(plugin.getLocationApiUrl(), ip);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(java.time.Duration.ofMillis(plugin.getLocationApiTimeout()))
                .GET()
                .build();
        CompletableFuture<Void> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    String country = "Unknown";
                    if(response.statusCode() == 200){
                        try{
                            JsonNode root = objectMapper.readTree(response.body());
                            if(root.has("country")){
                                country = root.get("country").asText();
                            }
                        } catch(IOException e){
                            country = "Unknown";
                        }
                    }
                    TextComponent accept = Component.text("Accept").color(NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/wl accept " + playerName));
                    TextComponent deny = Component.text("Deny").color(NamedTextColor.RED).clickEvent(ClickEvent.runCommand("/wl deny " + playerName));
                    Component message = Component.text("A player attempted to join from " + country + "\n------------------------------------------\n").append(accept).append(Component.space()).append(deny);
                    for(Player p : getRecipients()){
                        p.sendMessage(message);
                    }
                    plugin.getPending().remove(playerName);
                });
    }

    private Set<Player> getRecipients() {
        Set<Player> recipients = new HashSet<>();
        if (plugin.notifyAllWhitelisted()) {
            for (OfflinePlayer op : Bukkit.getWhitelistedPlayers()) {
                if (op.isOnline()) {
                    recipients.add(op.getPlayer());
                }
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("whitelistplus.receive")) {
                    recipients.add(p);
                }
            }
        }
        return recipients;
    }
}
