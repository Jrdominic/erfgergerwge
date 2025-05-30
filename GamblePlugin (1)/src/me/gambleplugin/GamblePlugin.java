package me.gambleplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public class GamblePlugin extends JavaPlugin {

    private HashMap<UUID, Integer> balances = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("GamblePlugin Enabled!");
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("GamblePlugin Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (command.getName().equalsIgnoreCase("balance")) {
            int balance = balances.getOrDefault(uuid, 0);
            player.sendMessage(ChatColor.GREEN + "Your balance: " + balance + " coins");
            return true;
        }

        if (command.getName().equalsIgnoreCase("addcoins") && player.isOp()) {
            if (args.length != 2) return false;
            Player target = Bukkit.getPlayer(args[0]);
            int amount = Integer.parseInt(args[1]);
            if (target != null) {
                UUID tid = target.getUniqueId();
                balances.put(tid, balances.getOrDefault(tid, 0) + amount);
                player.sendMessage("Added " + amount + " coins to " + target.getName());
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("casino")) {
            int current = balances.getOrDefault(uuid, 0);
            if (current < 50) {
                player.sendMessage(ChatColor.RED + "You need at least 50 coins to play.");
            } else {
                boolean win = Math.random() < 0.5;
                if (win) {
                    balances.put(uuid, current + 50);
                    player.sendMessage(ChatColor.GOLD + "You won! +50 coins.");
                } else {
                    balances.put(uuid, current - 50);
                    player.sendMessage(ChatColor.GRAY + "You lost! -50 coins.");
                }
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("buycoins")) {
            player.sendMessage(ChatColor.YELLOW + "Buy coins at: http://yourwebsite.com/buy");
            return true;
        }

        return false;
    }
}
