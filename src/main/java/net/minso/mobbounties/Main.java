package net.minso.mobbounties;

import net.milkbowl.vault.economy.Economy;
import net.minso.mobbounties.Core.Commands.BountiesCommand;
import net.minso.mobbounties.Core.GUI.GUI;
import net.minso.mobbounties.Core.Player.PlayerManager;
import net.minso.mobbounties.Core.Quests.QuestManager;
import net.minso.mobbounties.Core.Quests.QuestTracker;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Economy econ = null;

    private static Main main;
    public QuestManager questManager;
    public PlayerManager playerManager;
    public GUI gui;
    public QuestTracker questTracker;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        main = this;
        saveResource("quests.yml", false); //fix this
        questManager = new QuestManager();
        playerManager = new PlayerManager();
        gui = new GUI();
        questTracker = new QuestTracker();
        registerListeners();
        registerCommands();

        playerManager.onEnable();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        playerManager.onDisable();
    }

    private void registerListeners() {
        registerEvent(playerManager);
        registerEvent(gui);
        registerEvent(questTracker);
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

    public static Economy getEconomy() {
        return econ;
    }
}
