package io.github.sriyaankvad.conjoinedplayers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandKit implements CommandExecutor {

    public Player player1, player2, player3;
    public ArrayList<Player> players = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2 || args.length > 5) {
            printUsage(sender);
            return true;
        }

        for (String player : args) {
            if (Bukkit.getPlayer(player) == null) {
                sender.sendMessage("Player(s) not found\n\n");
                printUsage(sender);
                players.clear();
                return true;
            }

            players.add(Bukkit.getPlayer(player));
            (Bukkit.getPlayer(player)).setInvisible(true);
        }
        players.get(0).setInvisible(false);

        return true;
    }

    private static void printUsage(CommandSender sender) {
        sender.sendMessage("Usage:\n" +
                "/conjoinplayers <Player 1> <Player 2> " +
                " - Conjoins two players \n\t Player 1 handles the movement and places blocks"
                + "\n\tPlayer 2 damages blocks and entities as well as manages inventory\n" +
                "/conjoinplayers <Player 1> <Player 2> <Player 3>" +
                " - Conjoins three players \n\t Player 1 handles the movement"
                + "\n\tPlayer 2 places blocks and manages inventory\n\t" +
                "Player 3 damages entities and blocks" +
                "/conjoinplayers <Player 1> <Player 2> <Player 3> <Player 4>" +
                " - Conjoins four players \n\t Player 1 handles the movement"
                + "\n\tPlayer 2 manages inventory\n\t" +
                "Player 3 damages entities\n\tPlayer 4 Places and breaks blocks" +
                " - Conjoins four players \n\t Player 1 handles the movement"
                + "\n\tPlayer 2 manages inventory\n\t" +
                "Player 3 damages entities\n\tPlayer 4 Places blocks\n\tPlayer 5 breaks blocks");
    }
}
