package vendek.minigame_ctf;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

// Этот класс отвечает за обновление очков в игре
public class ScoreUpdater {

    // Максимальное количество очков, которое может набрать команда
    private static final int MAX_POINTS = 7;
    private final String worldName;
    private final World world;

    public ScoreUpdater(String worldName) {
        this.worldName = worldName;
        this.world = Bukkit.getWorld(worldName);
    }

    // Метод для обновления очков захвата
    public void updateCapturePoints() {
        // Получаем главное табло
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        // Получаем объекты, отображающие количество очков захвата для красной и синей команд
        Objective redObjective = scoreboard.getObjective("RED_CAPTURE_POINTS");
        Objective blueObjective = scoreboard.getObjective("BLUE_CAPTURE_POINTS");

        // Если одного из объектов нет, то выходим из метода
        if (redObjective == null || blueObjective == null) return;

        // Получаем список всех игроков в мире
        List<Player> players = Minigame_CTF.JustGetAllPlayers(worldName);

        // Проходимся по каждому игроку
        for (Player player : players) {
            // Получаем зону захвата, в которой находится игрок
            ArmorStand zone = getZone(player);
            // Если зона не найдена, то переходим к следующему игроку
            if (zone == null) continue;

            // Получаем количество очков захвата для красной и синей команд в данной зоне
            Score redScore = redObjective.getScore(zone.getUniqueId().toString());
            Score blueScore = blueObjective.getScore(zone.getUniqueId().toString());

            // Обновляем количество очков захвата для команды игрока
            updateTeamScore(player, redScore, blueScore, zone);
        }
    }

    // Метод для получения зоны захвата, в которой находится игрок
    private ArmorStand getZone(Player player) {
        return player.getWorld().getEntitiesByClass(ArmorStand.class).stream()
                .filter(armorStand -> armorStand.getScoreboardTags().contains("zone") && player.getLocation().distance(armorStand.getLocation()) <= 3)
                .findFirst().orElse(null);
    }

    // Метод для обновления количества очков захвата для команды игрока
    private void updateTeamScore(Player player, Score redScore, Score blueScore, ArmorStand zone) {
        // Получаем название команды игрока
        String teamName = player.getScoreboard().getEntryTeam(player.getName()).getName();
        // Получаем координаты зоны
        Location zoneLocation = zone.getLocation();

        // Если игрок в красной команде, то обновляем количество очков захвата для красной и синей команд
        if (teamName.equals("RED")) {
            updateScore(blueScore, redScore, player, zoneLocation);
        }
        // Если игрок в синей команде, то обновляем количество очков захвата для синей и красной команд
        else if (teamName.equals("BLUE")) {
            updateScore(redScore, blueScore, player, zoneLocation);
        }
    }

    // Метод для обновления количества очков захвата для команд
    private void updateScore(Score opponentScore, Score teamScore, Player player, Location zoneLocation) {
        // Если у противника есть очки, то уменьшаем их на 2
        if (opponentScore.getScore() > 0) {
            opponentScore.setScore(Math.max(0, opponentScore.getScore() - 2));
            world.playSound(zoneLocation, Sound.BLOCK_FIRE_EXTINGUISH, 1.0F, 1.0F);
        }
        // Если у команды игрока меньше максимального количества очков, то увеличиваем их на 1 и проигрываем звук
        else if (teamScore.getScore() < MAX_POINTS) {
            teamScore.setScore(teamScore.getScore() + 1);
            world.playSound(zoneLocation, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        }
    }
}
