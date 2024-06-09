package vendek.minigame_ctf;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BossBarManager implements CommandExecutor {
    private static BossBar redBar;
    private static BossBar blueBar;
    private int sections = 100;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<Player> players = Minigame_CTF.JustGetAllPlayers("world");
        ShowBossBars(players);
        sender.sendMessage("Боссбары были включены");
        return true;
    }

    public static void CreateBossBar() {
        redBar = Bukkit.createBossBar("Красные", BarColor.RED, BarStyle.SOLID);
        redBar.setProgress(0.01);

        blueBar = Bukkit.createBossBar("Синие", BarColor.BLUE, BarStyle.SOLID);
        blueBar.setProgress(0.01);
    }

    public static void ShowBossBars (List<Player> players) {
        for (Player player : players) {
            redBar.addPlayer(player);
            blueBar.addPlayer(player);
        }
    }

    public static void ClearBossBars () {
        if (redBar != null) {
            redBar.removeAll();
            redBar = null;
        }
        if (blueBar != null) {
            blueBar.removeAll();
            blueBar = null;
        }
    }
}
