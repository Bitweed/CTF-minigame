package vendek.minigame_ctf.pre_game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import vendek.minigame_ctf.GameManager;
import vendek.minigame_ctf.Minigame_CTF;
import vendek.minigame_ctf.pre_game.PlayerDistributor;

import java.util.List;

import static vendek.minigame_ctf.Minigame_CTF.JustGetAllPlayers;
import static vendek.minigame_ctf.Minigame_CTF.getGameManagers;

public class StartCTF implements CommandExecutor {

    private static final int COUNTDOWN_TIME = 3;
    private static final long RUN_TASK_INTERVAL = 20L;

    public static void startCountdown(World world, List<Player> players) {
        new BukkitRunnable() {
            int countdown = COUNTDOWN_TIME;
            public void run() {
                // Окончание отсчёта
                if (countdown <= 0) {
                    // Точки телепортации
                    Location blueSpawn = findArmorStandLocation(world, "blue_spawn");
                    Location redSpawn = findArmorStandLocation(world, "red_spawn");
                    // Телепортация игроков
                    for (Player player : players) {
                        String teamName = player.getScoreboard().getEntryTeam(player.getName()).getName();
                        if (teamName.contains("BLUE")) {
                            player.teleport(blueSpawn);
                        } else if (teamName.contains("RED")) {
                            player.teleport(redSpawn);
                        }
                    }
                    for (Player player : players) {
                        player.sendTitle(ChatColor.GREEN + "Вперёд!", "", 10, 20, 10);
                    }
                    this.cancel();
                } else {
                    for (Player player : players) {
                        player.sendTitle(ChatColor.YELLOW + String.valueOf(countdown), "", 10, 20, 10);
                    }
                    countdown--;
                }
            }
        }.runTaskTimer(Minigame_CTF.getInstance(), 0, RUN_TASK_INTERVAL);
    }

    private static Location findArmorStandLocation(World world, String tag) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ArmorStand && entity.getScoreboardTags().contains(tag)) {
                return entity.getLocation();
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        int gameObjectIndex = 0;
        GameManager gm = getGameManagers().get(gameObjectIndex);
        if (gm.getPlayerCount() < 1) {
            commandSender.sendMessage("Недостаточно игроков для старта" + " (" + gm.getPlayerCount() + ")");
            return false;
        }
        gm.startGame();
        return true;
    }
}