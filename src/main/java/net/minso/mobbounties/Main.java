package net.minso.mobbounties;

import net.minso.mobbounties.Core.PlayerManager;
import net.minso.mobbounties.Core.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main main;
    private QuestManager questManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        main = this;
        saveResource("quests.yml", false);
        questManager = new QuestManager();
        playerManager = new PlayerManager();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }





    private void registerListeners() {
        registerEvent(new GUI());
        registerEvent(playerManager);
    }

    private void registerCommands() {
        getCommand("bounties").setExecutor(new BountiesCommand());
    }

    private void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    public static Main getInstance() {
        return main;
    }

    public String getPluginName() {
        return getDescription().getName();
    }
}
