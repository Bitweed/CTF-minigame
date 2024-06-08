package vendek.minigame_ctf.commands;

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
import vendek.minigame_ctf.Minigame_CTF;

import java.util.List;

import static vendek.minigame_ctf.Minigame_CTF.JustGetAllPlayers;

public class StartCTF implements CommandExecutor {

    private void startCountdown(World world, List<Player> players, int countdownTime) {
        new BukkitRunnable() {
            int countdown = countdownTime;

            public void run() {
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
        }.runTaskTimer(Minigame_CTF.getInstance(), 0, 20L);
    }

    private Location findArmorStandLocation(World world, String tag) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ArmorStand && entity.getScoreboardTags().contains(tag)) {
                return entity.getLocation();
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("Вы не указали название мира!");
            return false;
        }
        String worldName = args[0];
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            commandSender.sendMessage("Мира '" + worldName + "' не существует!");
            return false;
        }
        // Список всех игроков в мире
        List<Player> players = JustGetAllPlayers(worldName);

        // Распределение по командам
        PlayerDistributor pd = new PlayerDistributor();
        pd.DistributePlayers(worldName);

        // Обратный отсчёт
        startCountdown(world, players, 3);

        return true;
    }
}