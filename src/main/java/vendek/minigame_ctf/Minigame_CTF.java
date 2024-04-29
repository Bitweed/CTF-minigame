package vendek.minigame_ctf;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;

public final class Minigame_CTF extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getCommand("distributePlayers").setExecutor(new PlayerDistributor());
        this.getCommand("showBossBars").setExecutor(new BossBarManager());

        getServer().getPluginManager().registerEvents(this, this);

        // Создание босс баров
        BossBarManager.CreateBossBar();

        Bukkit.getScheduler().runTaskTimer(this, this::updateCapturePoints, 0L, 20L);
    }

    @Override
    public void onDisable() {
        BossBarManager.ClearBossBars();
    }

    private final Integer MAX_POINTS = 10;
    private void updateCapturePoints() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective redObjective = scoreboard.getObjective("RED_CAPTURE_POINTS");
        Objective blueObjective = scoreboard.getObjective("BLUE_CAPTURE_POINTS");

        // Если таких скорбордов не существует, то выходим из функции
        if (redObjective == null || blueObjective == null) return;

        // Список всех игроков на карте
        List<Player> players = JustGetAllPlayers("world");

        for (Player player : players) {
            // Получаем ArmorStand с тегом 'zone' в радиусе 3 блоков
            ArmorStand zone = player.getWorld().getEntitiesByClass(ArmorStand.class).stream()
                    .filter(armorStand -> armorStand.getScoreboardTags().contains("zone") && player.getLocation().distance(armorStand.getLocation()) <= 3)
                    .findFirst().orElse(null);
            if (zone == null) continue;

            // Получаем очки захвата команд у зоны
            Score redScore = redObjective.getScore(zone.getUniqueId().toString());
            Score blueScore = blueObjective.getScore(zone.getUniqueId().toString());

            if (player.getScoreboard().getEntryTeam(player.getName()).getName().equals("RED")) {
                if (blueScore.getScore() > 0) {
                    blueScore.setScore(Math.max(0, blueScore.getScore() - 2));
                } else if (redScore.getScore() < MAX_POINTS) {
                    redScore.setScore(redScore.getScore() + 1);
                }
            }
            else if (player.getScoreboard().getEntryTeam(player.getName()).getName().equals("BLUE")) {
                if (redScore.getScore() > 0) {
                    redScore.setScore(Math.max(0, redScore.getScore() - 2));
                } else if (blueScore.getScore() < MAX_POINTS) {
                    blueScore.setScore(blueScore.getScore() + 1);
                }
            }
        }
    }

    public static List<Player> JustGetAllPlayers(String worldName) {
        // Берем всех игроков из мира 'worldName'
        World world = Bukkit.getServer().getWorld(worldName);
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
