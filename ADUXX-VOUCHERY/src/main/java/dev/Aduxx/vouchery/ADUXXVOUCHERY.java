package dev.Aduxx.vouchery;

import dev.Aduxx.vouchery.commands.VoucherCommand;
import dev.Aduxx.vouchery.Listener.VoucherListener;
import dev.Aduxx.vouchery.vouchery.VoucherTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class ADUXXVOUCHERY extends JavaPlugin {

    @Override
    public void onEnable() {

        this.saveDefaultConfig();


        this.getCommand("voucher").setExecutor(new VoucherCommand(this));


        this.getCommand("voucher").setTabCompleter(new VoucherTabCompleter());

        getServer().getPluginManager().registerEvents(new VoucherListener(this), this);
    }
}