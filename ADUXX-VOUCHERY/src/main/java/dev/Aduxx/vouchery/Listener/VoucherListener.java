package dev.Aduxx.vouchery.Listener;

import dev.Aduxx.vouchery.ADUXXVOUCHERY;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VoucherListener implements Listener {

    private ADUXXVOUCHERY plugin;

    public VoucherListener(ADUXXVOUCHERY plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().toString().contains("RIGHT_CLICK")) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            if (item == null || !item.hasItemMeta()) return;

            ItemMeta meta = item.getItemMeta();
            for (String voucherKey : plugin.getConfig().getConfigurationSection("vouchers").getKeys(false)) {
                String voucherName = plugin.getConfig().getString("vouchers." + voucherKey + ".voucher-name").replace('&', '§');

                if (meta.getDisplayName().equals(voucherName)) {
                    if (!player.hasPermission(plugin.getConfig().getString("voucher-permission"))) {
                        player.sendMessage(plugin.getConfig().getString("messages.no-permission").replace('&', '§'));
                        return;
                    }

                    for (String command : plugin.getConfig().getStringList("vouchers." + voucherKey + ".commands")) {
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("%player%", player.getName()));
                    }

                    item.setAmount(item.getAmount() - 1);
                    player.sendMessage(plugin.getConfig().getString("messages.voucher-used").replace('&', '§'));
                    break;
                }
            }
        }
    }
}