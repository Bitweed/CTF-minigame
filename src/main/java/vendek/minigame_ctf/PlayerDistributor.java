package vendek.minigame_ctf;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerDistributor implements CommandExecutor {

    String worldName = "world";
    String teamOneName = "RED";
    String teamTwoName = "BLUE";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<Player> players = JustGetAllPlayers();
        int playersCount = players.size();

        // Команды игроков (red, blue)
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getMainScoreboard();
        Team red = scoreboard.getTeam(teamOneName);
        Team blue = scoreboard.getTeam(teamTwoName);

        // Первая половина идет в красную команду, а вторая в синюю
        for (int i = 0; i < playersCount; i++){
            if (i + 1 <= (playersCount / 2)) {
                red.addEntry(players.get(i).getName());
            }
            else
            {
                blue.addEntry(players.get(i).getName());
            }
        }
        return true;
    }

    private List<Player> JustGetAllPlayers() {
        // Берем всех игроков из мира 'worldName'
        World world = Bukkit.getServer().getWorld(worldName);
        List<Player> playersList = world.getPlayers();
        // Случайно перемешиваем список игроков
        Collections.shuffle(playersList);
        return playersList;
    }
}
