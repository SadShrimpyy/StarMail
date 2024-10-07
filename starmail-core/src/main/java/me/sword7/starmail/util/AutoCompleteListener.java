package me.sword7.starmail.util;

import com.google.common.collect.ImmutableList;
import me.sword7.starmail.StarMail;
import me.sword7.starmail.box.Box;
import me.sword7.starmail.letter.Letter;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.warehouse.WarehouseCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteListener implements Listener {

    public AutoCompleteListener() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onComplete(TabCompleteEvent e) {
        if (e.getSender() instanceof Player) {
            String buffer = e.getBuffer().toLowerCase();
            if (buffer.contains("/sendto ")) {
                String root = "/sendto";
                e.setCompletions(getRefinedCompletions(root, buffer, getOnlinePlayers()));
            } else if (buffer.contains("/letter ")) {
                int args = numberOfFullArgs(buffer);
                if (args == 1) {
                    String root = "/letter " + getArg(buffer, 0);
                    e.setCompletions(getRefinedCompletions(root, buffer, getOnlinePlayers()));
                } else if (args == 0) {
                    String root = "/letter";
                    e.setCompletions(getRefinedCompletions(root, buffer, letterCompletions));
                }
            } else if (buffer.contains("/pack ")) {
                int args = numberOfFullArgs(buffer);
                if (args == 1) {
                    String root = "/pack " + getArg(buffer, 0);
                    e.setCompletions(getRefinedCompletions(root, buffer, getOnlinePlayers()));
                } else if (args == 0) {
                    String root = "/pack";
                    e.setCompletions(getRefinedCompletions(root, buffer, packCompletions));
                }
            } else if (buffer.contains("/box ")) {
                int args = numberOfFullArgs(buffer);
                if (args == 1) {
                    String root = "/box " + getArg(buffer, 0);
                    e.setCompletions(getRefinedCompletions(root, buffer, getOnlinePlayers()));
                } else if (args == 0) {
                    String root = "/box";
                    e.setCompletions(getRefinedCompletions(root, buffer, boxCompletions));
                }
            } else if (buffer.contains("/globalbox ")) {
                int args = numberOfFullArgs(buffer);
                if (args == 1) {
                    String root = "/globalbox " + getArg(buffer, 0);
                    e.setCompletions(getRefinedCompletions(root, buffer, getOnlinePlayers()));
                } else if (args == 0) {
                    String root = "/globalbox";
                    e.setCompletions(getRefinedCompletions(root, buffer, boxCompletions));
                }
            } else if (buffer.contains("/postbox ")) {
                int args = numberOfFullArgs(buffer);
                if (args == 1) {
                    String root = "/postbox " + getArg(buffer, 0);
                    e.setCompletions(getRefinedCompletions(root, buffer, getOnlinePlayers()));
                } else if (args == 0) {
                    String root = "/postbox";
                    e.setCompletions(getRefinedCompletions(root, buffer, postboxCompletions));
                }
            } else if (buffer.contains("/warehouse send ")) {
                int args = numberOfFullArgs(buffer);
                if (args == 2) {
                    String root = "/warehouse send " + getArg(buffer, 1);
                    e.setCompletions(getRefinedCompletions(root, buffer, WarehouseCache.getEntryTypes()));
                } else if (args == 1) {
                    String root = "/warehouse send";
                    e.setCompletions(getRefinedCompletions(root, buffer, getOnlinePlayers()));
                }
            } else if (buffer.contains("/warehouse rename ") && numberOfFullArgs(buffer) == 1) {
                String root = "/warehouse rename";
                e.setCompletions(getRefinedCompletions(root, buffer, WarehouseCache.getEntryTypes()));
            } else if (buffer.contains("/warehouse edit ") && numberOfFullArgs(buffer) == 1) {
                String root = "/warehouse edit";
                e.setCompletions(getRefinedCompletions(root, buffer, WarehouseCache.getEntryTypes()));
            } else if (buffer.contains("/warehouse delete ") && numberOfFullArgs(buffer) == 1) {
                String root = "/warehouse delete";
                e.setCompletions(getRefinedCompletions(root, buffer, WarehouseCache.getEntryTypes()));
            } else if (buffer.contains("/warehouse ") && numberOfFullArgs(buffer) == 0) {
                String root = "/warehouse";
                e.setCompletions(getRefinedCompletions(root, buffer, warehouseCompletions));
            } else if (buffer.contains("/blacklist ") && numberOfFullArgs(buffer) == 0) {
                String root = "/blacklist";
                e.setCompletions(getRefinedCompletions(root, buffer, blacklistCompletions));
            }
        }
    }

    private List<String> getRefinedCompletions(String root, String buffer, List<String> completions) {
        if (buffer.equalsIgnoreCase(root + " ")) {
            return completions;
        } else {
            List<String> refinedCompletions = new ArrayList<>();
            String bufferFromRoot = buffer.split(root + " ")[1];
            for (String completion : completions) {
                if (bufferFromRoot.length() < completion.length()) {
                    if (completion.substring(0, bufferFromRoot.length()).equalsIgnoreCase(bufferFromRoot)) {
                        refinedCompletions.add(completion);
                    }
                }
            }
            return refinedCompletions;
        }
    }

    private int numberOfFullArgs(String buffer) {
        int lastNotCompletedPenalty = endsInSpace(buffer) ? 0 : -1;
        return buffer.split(" ").length - 1 + lastNotCompletedPenalty;
    }

    private boolean endsInSpace(String buffer) {
        return ' ' == buffer.charAt(buffer.length() - 1);
    }

    private String getArg(String buffer, int arg) {
        return buffer.split(" ")[arg + 1];
    }

    private List<String> warehouseCompletions = new ImmutableList.Builder<String>()
            .add("send")
            .add("save")
            .add("rename")
            .add("edit")
            .add("list")
            .add("delete")
            .add("help")
            .build();

    private List<String> blacklistCompletions = new ImmutableList.Builder<String>()
            .add("help")
            .add("add")
            .add("list")
            .add("remove")
            .add("reload")
            .build();

    private List<String> letterCompletions = makeLetterCompletions();

    private List<String> makeLetterCompletions() {
        List<String> completions = new ArrayList<>();
        for (Letter letter : Letter.getAllLetters()) {
            completions.add(letter.getName());
        }
        return completions;
    }


    private List<String> packCompletions = makePackCompletions();

    private List<String> makePackCompletions() {
        List<String> completions = new ArrayList<>();
        for (Pack pack : Pack.getAllPacks()) {
            completions.add(pack.getName());
        }
        return completions;
    }


    private List<String> boxCompletions = makeBoxCompletions();

    private List<String> makeBoxCompletions() {
        List<String> completions = new ArrayList<>();
        for (Box box : Box.getAllBoxes()) {
            completions.add(box.getName());
        }
        return completions;
    }

    private List<String> postboxCompletions = makePostboxCompletions();

    private List<String> makePostboxCompletions() {
        List<String> completions = new ArrayList<>();
        for (Postbox postbox : Postbox.getAllPostboxes()) {
            completions.add(postbox.getName());
        }
        return completions;
    }

    private List<String> getOnlinePlayers() {
        List<String> completions = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            completions.add(player.getName());
        }
        return completions;
    }


}
