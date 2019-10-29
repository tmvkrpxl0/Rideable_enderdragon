package tmvkrpxl0;

import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.WorldServer;

public class ReCommand implements CommandExecutor{
	private final LinkedHashMap<String, Object[]> map = Rideable_enderdragon.getmap();
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player == false) {
			sender.sendMessage("You must use this command in-game!");
			return true;
		}
		Player player = (Player) sender;
		String uuid = player.getUniqueId().toString();
		if(!player.isOp()) {
			player.sendMessage(ChatColor.RED + "You don't have Op!");
			return true;
		}
			Object [] temp;
		if(map == null)Bukkit.getConsoleSender().sendMessage("map is null");
		if(uuid == null)Bukkit.getConsoleSender().sendMessage("uuid is null");
		if(!map.containsKey(uuid)) {
			temp = new Object[7];
			temp[Indexes.DESTROY_BLOCK.getIndex()] = false;
			temp[Indexes.DAMAGE_ENTITY.getIndex()] = false;
			temp[Indexes.COOLDOWN.getIndex()] = 0;
			map.put(uuid, temp);
		}
		if(args.length > 0) {
			temp = map.get(uuid);
			String ar0 = args[0].toLowerCase();
			switch(ar0) {
			case "destroy_block":
				if(args.length > 1) {
					String ar1 = args[1];
					switch(ar1) {
					case "true":
						player.sendMessage("You will be able to destroy block with Ender Dragon");
						player.sendMessage(ChatColor.GRAY + "Note that enabling this is dangerous. I recommend you to disable this");
						temp[Indexes.DESTROY_BLOCK.getIndex()] = true;
						map.put(uuid, temp);
						break;
					case "false":
						player.sendMessage("You will no longer be able to destroy block with Ender Dragon");
						temp[Indexes.DESTROY_BLOCK.getIndex()] = false;
						map.put(uuid, temp);
						break;
					}
				}else {
						temp[Indexes.DESTROY_BLOCK.getIndex()] = !(boolean)temp[Indexes.DESTROY_BLOCK.getIndex()];
						map.put(uuid, temp);
						if((boolean) map.get(uuid)[Indexes.DESTROY_BLOCK.getIndex()]) {
							player.sendMessage("You will be able to destroy block with Ender Dragon");
							player.sendMessage(ChatColor.GRAY + "Note that enabling this is dangerous. I recommend you to disable this");
						}
						else player.sendMessage("You will no longer be able to destroy block with Ender Dragon");
					}
				break;
			case "damage_entity":
				if(args.length > 1) {
					String ar1 = args[1];
					switch(ar1) {
					case "true":
						player.sendMessage("You will be able to damage entity with Ender Dragon");
						temp[Indexes.DAMAGE_ENTITY.getIndex()] = true;
						map.put(uuid, temp);
						break;
					case "false":
						player.sendMessage("You will no longer be able to damage entity with Ender Dragon");
						temp[Indexes.DAMAGE_ENTITY.getIndex()] = false;
						map.put(uuid, temp);
						break;
					}
				}else {
						temp[Indexes.DAMAGE_ENTITY.getIndex()] = !(boolean)temp[Indexes.DAMAGE_ENTITY.getIndex()];
						map.put(uuid, temp);
						if((boolean)map.get(uuid)[Indexes.DAMAGE_ENTITY.getIndex()])player.sendMessage("You will be able to damage entity with Ender Dragon");
						else player.sendMessage("You will no longer be able to damage entity with Ender Dragon");
					}
				break;
			case "spawn":
				Location loc = player.getLocation();
				WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
				Rideable_enderdragon_entity dragon = new Rideable_enderdragon_entity(world);
				dragon.getBukkitEntity().setPassenger(player);
				dragon.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
				dragon.yaw =  loc.getYaw()+ 180;
				dragon.pitch = loc.getPitch();
				world.addEntity(dragon);
				break;
			default:
				return false;
			}
	}else return false;
		return true;
    }
}
