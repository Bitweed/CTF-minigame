package vendek.minigame_ctf;

import org.bukkit.plugin.java.JavaPlugin;

public final class Minigame_CTF extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("distributePlayers").setExecutor(new PlayerDistributor());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
