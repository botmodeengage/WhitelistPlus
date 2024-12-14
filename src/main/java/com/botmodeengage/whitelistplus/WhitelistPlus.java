package com.botmodeengage.whitelistplus;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import com.botmodeengage.whitelistplus.commands.WhitelistCommand;
import com.botmodeengage.whitelistplus.events.PlayerJoinListener;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WhitelistPlus extends JavaPlugin {
    private Set<String> pending;
    private Set<String> denied;

    @Override
    public void onEnable(){
        int pluginId = 24139; // <-- Replace with the id of your plugin
        Metrics metrics = new Metrics(this, pluginId);
        saveDefaultConfig();
        pending = new HashSet<>();
        denied = new HashSet<>();
        Objects.requireNonNull(getCommand("wlp")).setExecutor(new WhitelistCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    public String getAwaitMessage(){
        return getConfig().getString("await-message", "&eYou are not yet whitelisted. A request has been sent.").replace("&", "§");
    }

    public String getDenyMessage(){
        return getConfig().getString("deny-message", "&cYou have been denied from the whitelist. Try again later.").replace("&", "§");
    }

    public boolean isWhitelistEnabled(){
        return getConfig().getBoolean("whitelist-status", true);
    }

    public boolean notifyAllWhitelisted(){
        return getConfig().getBoolean("notify-all-whitelisted", false);
    }

    public Set<String> getPending(){
        return pending;
    }

    public Set<String> getDenied(){
        return denied;
    }

    public String getLocationApiUrl(){
        return getConfig().getString("location-api.url", "http://ip-api.com/json/%s");
    }

    public int getLocationApiTimeout(){
        return getConfig().getInt("location-api.timeout", 5000);
    }
}
