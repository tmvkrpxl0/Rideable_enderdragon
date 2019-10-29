package tmvkrpxl0;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle;
import tmvkrpxl0.NMSUtils.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;;

public class Rideable_enderdragon extends JavaPlugin {
	
	ConsoleCommandSender sender;
	protected static Plugin plugin;
	protected ProtocolManager protocolManager;
	private static LinkedHashMap<String, Object[]> map;
	protected static LinkedHashMap<String, Object[]> getmap(){
		return map;
	}
	protected static Plugin getp() {
		return plugin;
	}
	@Override
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		sender = Bukkit.getConsoleSender();
		sender.sendMessage("---------------------------------------");
		sender.sendMessage("l                                      l");
		sender.sendMessage("l " + ChatColor.DARK_PURPLE + "Rideable_Enderdragon Plugin " 
		+ ChatColor.GREEN + "Enabled  " + ChatColor.WHITE + "l");
		sender.sendMessage("l                                      l");
		sender.sendMessage("---------------------------------------" + ChatColor.GREEN + getDescription().getVersion() + "v");
		NMSUtils.registerEntity("rideable_enderdragon", Type.ENDER_DRAGON, Rideable_enderdragon_entity.class);
		plugin = this;
		File ConfigFile = new File(getp().getDataFolder() + File.separator + "playerdata.dat");
		if(!ConfigFile.getParentFile().exists())ConfigFile.getParentFile().mkdir();
		if(!ConfigFile.exists()){
			map = new LinkedHashMap<String, Object[]>();
				try {
					ConfigFile.createNewFile();
				} catch (IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Failed to create new Config File");
				}
			}else {
				try {
					FileInputStream Fis = new FileInputStream(ConfigFile);
					ObjectInputStream Ois = new ObjectInputStream(Fis);
					map = (LinkedHashMap<String, Object[]>)Ois.readObject();
					Ois.close();
					Fis.close();
				} catch (Exception e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Failed to load Config File");
					sender.sendMessage(ChatColor.RED + "Please read StackTrace and see what's went wrong");
				}
			}
		protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				PacketPlayInSteerVehicle packet = (PacketPlayInSteerVehicle)event.getPacket().getHandle();
				Player player = event.getPlayer();
				String uuid = player.getUniqueId().toString();
				final Object[] temp = map.get(uuid);
				if(player.getVehicle() instanceof EnderDragon) {
					if(3>=(double)map.get(uuid)[Indexes.MULTIPLY.getIndex()] && packet.b()>0) {
						temp[Indexes.MULTIPLY.getIndex()] = (double)temp[Indexes.MULTIPLY.getIndex()] + 0.1;
					}
					if(0.4<=(double)map.get(uuid)[Indexes.MULTIPLY.getIndex()] && packet.b()<0) {
						temp[Indexes.MULTIPLY.getIndex()] = (double)temp[Indexes.MULTIPLY.getIndex()] - 0.1;
					}
					player.setLevel((int)((double)map.get(uuid)[Indexes.MULTIPLY.getIndex()] * 100 / 3));
				}
			}
		});
		getServer().getPluginManager().registerEvents(new ReListener(), plugin);
		this.getCommand("rideable_enderdragon").setExecutor(new ReCommand());
		this.getCommand("rideable_enderdragon").setTabCompleter(new ReCommandCompleter());
	}
	@Override
	public void onDisable() {
		HashSet<World> list = new HashSet<World>();
		for(Player player : Bukkit.getOnlinePlayers()) {
			list.add(player.getWorld());
		}
		HashSet<List<LivingEntity>> entities = new HashSet<List<LivingEntity>>();
		for(World world : list) {
			entities.add(world.getLivingEntities());
		}
		for(List<LivingEntity> llist : entities) {
			for(LivingEntity entity : llist) {
				if(entity instanceof Player) {
					Player player = (Player) entity;
					if(map.containsKey(player.getUniqueId().toString())) {
						Object[] v = map.get(player.getUniqueId().toString());
						player.setExp((float) v[Indexes.EXP.getIndex()]);
						player.setLevel((int) v[Indexes.LEVEL.getIndex()]);
						v[Indexes.FLYLEFT.getIndex()] = true;
					}
				}
				if(entity.getType() == EntityType.ENDER_DRAGON && entity.getName() == "Rideable Ender Dragon")
					entity.eject();

			}
		}
		sender.sendMessage("---------------------------------------");
		sender.sendMessage("l                                      l");
		sender.sendMessage("l " + ChatColor.DARK_PURPLE + "Rideable Enderdragon Plugin "
		+ ChatColor.RED + "Disabled  " + ChatColor.WHITE + "l");
		sender.sendMessage("l                                      l");
		sender.sendMessage("---------------------------------------" + ChatColor.GREEN + getDescription().getVersion() + "v");
		File ConfigFile = new File(getp().getDataFolder() + File.separator + "playerdata.dat");
		try {
			FileOutputStream Fos = new FileOutputStream(ConfigFile);
			ObjectOutputStream Oos = new ObjectOutputStream(Fos);
			Oos.writeObject(map);
			Oos.flush();
			Oos.close();
			Fos.flush();
			Fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.sender.sendMessage(ChatColor.RED + "ERROR DURING SAVING PLAYER CONFIG");
			e.printStackTrace();
		}
	}
}
