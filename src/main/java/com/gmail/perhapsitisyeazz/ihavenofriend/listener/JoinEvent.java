package com.gmail.perhapsitisyeazz.ihavenofriend.listener;

import com.gmail.perhapsitisyeazz.ihavenofriend.util.Data;
import com.gmail.perhapsitisyeazz.ihavenofriend.manager.JoinFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.moderocky.mask.internal.utility.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.*;

public class JoinEvent implements Listener {

    private final JoinFile joinFile = new JoinFile();
    private final Data data = new Data();

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File playerData = data.getData(player);
        if (!playerData.exists()) joinFile.createPlayerFile(player);
        JsonObject object = data.getJsonObject(player);
        String name = object.get("Username").getAsString();
        if (!name.equals(player.getName())) {
            object.addProperty("Username", player.getName());
            String jsonObjectToString = object.toString();
            FileManager.write(playerData, jsonObjectToString);
        }
        JsonArray jsonArray = object.get("FriendList").getAsJsonArray();
        if (jsonArray.size() == 0) return;
        for (JsonElement array : jsonArray) {
            UUID uuid = UUID.fromString(array.getAsString());
            Player friend = Bukkit.getPlayer(uuid);
            assert (friend != null);
            if (!friend.isOnline()) return;
            friend.sendMessage(new ComponentBuilder()
                    .append("[").color(ChatColor.DARK_GRAY)
                    .append("Friend").color(ChatColor.DARK_AQUA)
                    .append("] ").color(ChatColor.DARK_GRAY)
                    .append(player.getName()).color(ChatColor.AQUA)
                    .append(" has joined the server.").color(ChatColor.DARK_GREEN)
                    .create());
        }
    }
}
