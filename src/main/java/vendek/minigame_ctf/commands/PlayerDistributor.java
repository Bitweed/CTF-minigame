package vendek.minigame_ctf.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import vendek.minigame_ctf.Minigame_CTF;

import java.util.List;

public class PlayerDistributor implements CommandExecutor {
    String teamOneName = "RED";
    String teamTwoName = "BLUE";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DistributePlayers();
        sender.sendMessage("Игроки были успешно поделены на команды.");
        return true;
    }

    private void DistributePlayers() {
        List<Player> players = Minigame_CTF.JustGetAllPlayers("world");
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
