package vendek.minigame_ctf;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import vendek.minigame_ctf.pre_game.StartCTF;
import vendek.minigame_ctf.pre_game.PlayerDistributor;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements Listener {
    private List<Player> players;
    private World world;
    private boolean isGameInProgress;

    private boolean isGameEnded;

    public GameManager(World world) {
        this.players = new ArrayList<>();
        this.world = world;
        this.isGameInProgress = false;
        this.isGameEnded = false;
    }

    public void startGame() {
        if (!isGameInProgress) {
            isGameInProgress = true;
            isGameEnded = false;

            // Распределение игроков по командам.
            PlayerDistributor.DistributePlayers(world);
            // Обратный отсчёт
            StartCTF.startCountdown(world, players);
        }
    }

    public void endGame() {
        if (isGameInProgress) {
            isGameInProgress = false;
            isGameEnded = true;
            // Здесь могут быть дополнительные действия для окончания игры
        }
    }

    public int getPlayerCount() {
        return players.size();
    }

    // Геттеры и сеттеры для приватных полей (если нужны)
    public List<Player> getPlayers() {
        return players;
    }

    public World getWorld() {
        return world;
    }

    public boolean isGameInProgress() {
        return isGameInProgress;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World toWorld = player.getWorld();
        World fromWorld = event.getFrom();

        Bukkit.getLogger().info("onChangeWorld вызван");

        if (toWorld.equals(this.world)) {
            players.add(player);
            Bukkit.getLogger().info(String.format("Вошел =) %s", players));
        }

        if (fromWorld.equals(this.world)) {
            players.remove(player);
            Bukkit.getLogger().info(String.format("Ушел =( %s", players));
        }
    }

}
