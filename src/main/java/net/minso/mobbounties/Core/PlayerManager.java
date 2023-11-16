package net.minso.mobbounties.Core;

import net.minso.mobbounties.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class PlayerManager implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createPlayerFile(player);
    }

    private void createPlayerFile(Player player) {
        String uuid = player.getUniqueId().toString();
        File playerFile = new File(main.getDataFolder() + File.separator + "players", uuid + ".yml");

        if (!playerFile.exists()) {
            try {
                playerFile.getParentFile().mkdirs(); // Create parent directories if they don't exist
                playerFile.createNewFile();
                saveDefaultPlayerConfig(playerFile, player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        private void saveDefaultPlayerConfig(File playerFile, Player player) {
            FileConfiguration config = main.getConfig();
            config.options().copyDefaults(true);

            // Customize default player data as needed
            config.set("uuid", player.getUniqueId().toString());

            main.saveConfig();
        }



}
