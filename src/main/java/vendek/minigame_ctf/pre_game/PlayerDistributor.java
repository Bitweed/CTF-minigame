package vendek.minigame_ctf.pre_game;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import vendek.minigame_ctf.Minigame_CTF;

import java.util.List;

public class PlayerDistributor {
    static String teamOneName = "RED";
    static String teamTwoName = "BLUE";


    public static void DistributePlayers(World world) {
        String worldName = world.getName();
        List<Player> players = Minigame_CTF.JustGetAllPlayers(worldName);
        int playersCount = players.size();

        // Команды игроков (red, blue)
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getMainScoreboard();
        Team red = scoreboard.getTeam(teamOneName);
        Team blue = scoreboard.getTeam(teamTwoName);

        // Первая половина идет в красную команду, а вторая в синюю
        for (int i = 0; i < playersCount; i++){
            if (i + 1 <= (playersCount / 2))
                red.addEntry(players.get(i).getName());
            else
                blue.addEntry(players.get(i).getName());
        }
    }


}
