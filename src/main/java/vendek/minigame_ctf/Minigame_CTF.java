package vendek.minigame_ctf;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import vendek.minigame_ctf.pre_game.StartCTF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Minigame_CTF extends JavaPlugin implements Listener {

    private final ScoreUpdater scoreUpdater = new ScoreUpdater("world");
    private static Minigame_CTF instance;
    private static List<GameManager> allGameManagers = new ArrayList<>();
    
    public static Plugin getInstance() {
        return instance;
    }
    public static List<GameManager> getGameManagers() {
        return allGameManagers;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        this.getCommand("showBossBars").setExecutor(new BossBarManager());
        this.getCommand("start-ctf").setExecutor(new StartCTF());

        getServer().getPluginManager().registerEvents(this, this);

        // Получаем все миры из конфига
        ConfigurationSection worldsConfig = getConfig().getConfigurationSection("worlds");
        if (worldsConfig != null) {
            // Создаем объекты миров с режимом, взяв данные из конфига
            for (String key : worldsConfig.getKeys(false)) {
                String worldName = worldsConfig.getString(key + ".worldName");
                GameManager gm = new GameManager(Bukkit.getWorld(worldName));
                getServer().getPluginManager().registerEvents(gm, this);
                // Добавим объект режима в список
                allGameManagers.add(gm);
            }
        } else {
            getLogger().warning("Конфигурация миров не найдена!");
        }
        Bukkit.getLogger().info("Список миров режима CTF: " + allGameManagers.toString());


        // Создание босс баров
        BossBarManager.CreateBossBar();

        Bukkit.getScheduler().runTaskTimer(this, scoreUpdater::updateCapturePoints, 0L, 20L);
    }

    @Override
    public void onDisable() {
        BossBarManager.ClearBossBars();
    }


    public static List<Player> JustGetAllPlayers(String worldName) {
        // Берем всех игроков из мира 'worldName'
        World world = Bukkit.getServer().getWorld(worldName);
        // Продожим, если мир существует
        assert world != null;
        List<Player> playersList = world.getPlayers();
        // Случайно перемешиваем список игроков
        Collections.shuffle(playersList);
        return playersList;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        // Находим все стойки для брони в мире
        List<ArmorStand> armorStands = (List<ArmorStand>) world.getEntitiesByClass(ArmorStand.class);

        // Ищем стойку для брони с тегом "spawnpoint"
        for (ArmorStand armorStand : armorStands) {
            if (armorStand.getScoreboardTags().contains("spawnpoint")) {
                // Телепортируем игрока к найденной стойке для брони
                player.teleport(armorStand.getLocation());
                break;
            }
        }
    }
}
