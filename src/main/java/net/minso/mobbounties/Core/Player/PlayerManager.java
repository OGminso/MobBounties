package net.minso.mobbounties.Core.Player;

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


        //LoadPlayerFile(Player.uuid)
        // - check if player has file if not create file and file with random quests

        //open open gui check cooldown times plus load new quest aswell as check perms for 4-6
        //claim quests once complete in gui.

        //even listener for mob kills
        //

        //on disconnect save file
        // on reload save and stop server save


        //Gui layout
        // - Enchanted once done
        //bedrock if not avaialable
        //barrier if on cooldown.
        //filled map if active quest.

        //papi support
        // show title
        //show progress


}
