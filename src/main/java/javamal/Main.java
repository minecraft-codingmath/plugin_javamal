package javamal;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin implements Listener{

    public Map<Player, Location> pos1 = new HashMap<Player, Location>();
    public Map<Player, Location> pos2 = new HashMap<Player, Location>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName();
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "이 명령어는 인게임에서 실행해야 합니다.");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.equals("jm")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.WHITE + "1. " + ChatColor.AQUA + " /jm copy" + ChatColor.WHITE + "- 블럭을 자바말 코드로 변환합니다.");
                return true;
            }
            if (args[0].equals("copy") || args[0].equals("복사")) {
                if (!pos1.containsKey(player)) {
                    player.sendMessage(ChatColor.RED + "pos 1을 지정하세요.");
                    return true;
                }

                if (!pos2.containsKey(player)) {
                    player.sendMessage(ChatColor.RED + "pos 2를 지정하세요.");
                    return true;
                }

                Location p1 = pos1.get(player);
                Location p2 = pos2.get(player);
                Vector rp1 = new Vector();
                Vector rp2 = new Vector();
                if (p1.getX() > p2.getX()) {
                    rp1.setX(p2.getX());
                    rp2.setX(p1.getX());
                } else {
                    rp1.setX(p1.getX());
                    rp2.setX(p2.getX());
                }

                if (p1.getY() > p2.getY()) {
                    rp1.setY(p2.getY());
                    rp2.setY(p1.getY());
                } else {
                    rp1.setY(p1.getY());
                    rp2.setY(p2.getY());
                }

                if (p1.getZ() > p2.getZ()) {
                    rp1.setZ(p2.getZ());
                    rp2.setZ(p1.getZ());
                } else {
                    rp1.setZ(p1.getZ());
                    rp2.setZ(p2.getZ());
                }

                String result = "hs";

                for (int y = (int) rp1.getY() ; y <= (int) rp2.getY() ; y++) {
                    result += "[";
                    for (int z = (int) rp1.getZ() ; z <= (int) rp2.getZ() ; z++) {
                        result += "[";
                        for (int x = (int) rp1.getX() ; x <= (int) rp2.getX() ; x++) {
                            Block block = player.getWorld().getBlockAt(x, y, z);
                            if (block.getType().equals(Material.AIR)) {
                                result += "h";
                            } else {
                                result += "c";
                            }
                            result += "s";
                        }
                        result += "]hr";
                    }
                    result += "]hu";
                }

                this.getLogger().info(result);
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType().equals(Material.WOOD_AXE)) {
            Block block = event.getClickedBlock();
            pos2.put(event.getPlayer(), block.getLocation());
            event.getPlayer().sendMessage("지점 2 설정: " + block.getLocation().getBlockX() + ", " + block.getLocation().getBlockY() + ", " + block.getLocation().getBlockZ());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WOOD_AXE)) {
            Block block = event.getBlock();
            pos1.put(event.getPlayer(), block.getLocation());
            event.getPlayer().sendMessage("지점 1 설정: " + block.getLocation().getBlockX() + ", " + block.getLocation().getBlockY() + ", " + block.getLocation().getBlockZ());
            event.setCancelled(true);
        }
    }
}
