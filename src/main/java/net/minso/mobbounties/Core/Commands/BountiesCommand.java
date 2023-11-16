package net.minso.mobbounties.Core.Commands;

import net.minso.mobbounties.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BountiesCommand implements CommandExecutor {

    Main main = Main.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("bounties")) {
            //main.gui.openGUI(player);
            player.sendMessage("Opened Gui");
            return true;
        }

        return false;
    }


}
