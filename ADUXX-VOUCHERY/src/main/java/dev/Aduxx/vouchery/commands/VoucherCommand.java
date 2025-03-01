package dev.Aduxx.vouchery.commands;

import dev.Aduxx.vouchery.ADUXXVOUCHERY;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VoucherCommand implements CommandExecutor {

    private ADUXXVOUCHERY plugin;

    public VoucherCommand(ADUXXVOUCHERY plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getConfig().getString("voucher-permission"))) {
            sender.sendMessage(plugin.getConfig().getString("messages.no-permission").replace('&', '§'));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.getConfig().getString("messages.usage").replace('&', '§'));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(plugin.getConfig().getString("messages.reloaded").replace('&', '§'));
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                sender.sendMessage(plugin.getConfig().getString("messages.no-voucher-name").replace('&', '§'));
                return true;
            }

            String voucherName = args[1];
            plugin.getConfig().set("vouchers." + voucherName + ".name", voucherName);
            plugin.getConfig().set("vouchers." + voucherName + ".voucher-name", "&8Voucher na &b" + voucherName);
            plugin.getConfig().set("vouchers." + voucherName + ".lore", java.util.Arrays.asList(
                    "&7Kliknij PPM, &eaby otrzymać",
                    "&e&L" + voucherName + " na 14 dni&8!"
            ));
            plugin.getConfig().set("vouchers." + voucherName + ".commands", java.util.Arrays.asList(
                    "lp user %player% parent addtemp " + voucherName + " 7D"
            ));
            plugin.getConfig().set("vouchers." + voucherName + ".item", "YELLOW_STAINED_GLASS");
            plugin.saveConfig();

            sender.sendMessage(plugin.getConfig().getString("messages.voucher-created")
                    .replace("{voucher_name}", voucherName)
                    .replace('&', '§'));
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 3) {
                sender.sendMessage(plugin.getConfig().getString("messages.must-provide-player-name").replace('&', '§'));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(plugin.getConfig().getString("messages.player-offline").replace('&', '§'));
                return true;
            }

            String voucherKey = args[2];
            if (!plugin.getConfig().contains("vouchers." + voucherKey)) {
                sender.sendMessage(plugin.getConfig().getString("messages.voucher-not-exist").replace('&', '§'));
                return true;
            }

            ItemStack voucher = new ItemStack(Material.valueOf(plugin.getConfig().getString("vouchers." + voucherKey + ".item")));
            ItemMeta meta = voucher.getItemMeta();
            meta.setDisplayName(plugin.getConfig().getString("vouchers." + voucherKey + ".voucher-name").replace('&', '§'));
            meta.setLore(plugin.getConfig().getStringList("vouchers." + voucherKey + ".lore").stream()
                    .map(line -> line.replace('&', '§'))
                    .collect(java.util.stream.Collectors.toList()));
            voucher.setItemMeta(meta);

            target.getInventory().addItem(voucher);

            return true;
        }

        sender.sendMessage(plugin.getConfig().getString("messages.usage").replace('&', '§'));
        return true;
    }
}