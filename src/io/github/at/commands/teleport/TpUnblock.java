package io.github.at.commands.teleport;

import io.github.at.config.Config;
import io.github.at.config.CustomMessages;
import io.github.at.config.TpBlock;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class TpUnblock implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (Config.isFeatureEnabled("teleport")) {
            if (sender.hasPermission("at.member.unblock")) {
                if (sender instanceof Player){
                    Player player = (Player)sender;
                    if (args.length>0){
                        if (args[0].equalsIgnoreCase(player.getName())){
                            sender.sendMessage(CustomMessages.getString("Error.blockSelf"));
                            return true;
                        }
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                        if (target == null){
                            sender.sendMessage(CustomMessages.getString("Error.noSuchPlayer"));
                        } else {
                            if (TpBlock.getBlockedPlayers(player).contains(target.getPlayer().getUniqueId())){
                                try {
                                    TpBlock.remBlockedPlayer(player, target.getPlayer());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (NullPointerException e) {
                                    sender.sendMessage(CustomMessages.getString("Error.noSuchPlayer"));
                                    return true;
                                }
                                sender.sendMessage(CustomMessages.getString("Info.unblockPlayer").replaceAll("\\{player}", args[0]));
                            }
                        }
                    } else {
                        sender.sendMessage(CustomMessages.getString("Error.noPlayerInput"));
                    }
                } else {
                    sender.sendMessage(CustomMessages.getString("Error.notAPlayer"));
                }
            }
        } else {
            sender.sendMessage(CustomMessages.getString("Error.featureDisabled"));
        }
        return true;
    }
}
