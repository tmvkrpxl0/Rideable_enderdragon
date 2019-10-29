package tmvkrpxl0;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;;

public class ReListener implements Listener{
	private final LinkedHashMap<String, Object[]>map = Rideable_enderdragon.getmap();
	private final LinkedHashMap<String, BukkitRunnable[]>Threads =  new LinkedHashMap<String, BukkitRunnable[]>();
	private final int EXPT = 0;
	private final int PARACUTET = 1;
	private void changev(Player player, int index, Object content) {
		Object [] temp = map.get(player.getUniqueId().toString());
		temp[index] = content;
		map.put(player.getUniqueId().toString(), temp);
	}
	private void paracute(Player player) {
		Vector vp = player.getVelocity().setY(-0.1);
		String uuid = player.getUniqueId().toString();
		BukkitRunnable [] tt = Threads.get(uuid);
		tt[PARACUTET] = new BukkitRunnable() {
			@Override
			public void run() {
				if(player.getVelocity().getY() >= 0 || player.isOnGround() ||
						(player.getVehicle() != null && 
						player.getVehicle().getType() == EntityType.ENDER_DRAGON) ||
						player.getLocation().getBlock().getType() == Material.STATIONARY_WATER
						|| player.getLocation().getBlock().getType() == Material.WATER || player.isSneaking()) {
					changev(player, Indexes.FLYLEFT.getIndex(), false);
					this.cancel();
				}
				player.setVelocity(vp.setX(player.getVelocity().getX()).setZ(player.getVelocity().getZ()));
				player.setFallDistance(0);
			}
		};
		Threads.put(uuid, tt);
		Threads.get(uuid)[PARACUTET].runTaskTimer(Rideable_enderdragon.getp(), 10L, 1L);
	}
	private void setle(Player player) {
		player.setExp((float)map.get(player.getUniqueId().toString())[Indexes.EXP.getIndex()]);
		player.setLevel((int)map.get(player.getUniqueId().toString())[Indexes.LEVEL.getIndex()]);
	}
	@EventHandler
	public void onEntityDismount(EntityDismountEvent event) {
		if(event.getDismounted() instanceof EnderDragon && event.getDismounted().getCustomName() == "Rideable Ender Dragon") {
			Player player = (Player)event.getEntity();
			String uuid = player.getUniqueId().toString();
			if(Threads.containsKey(uuid) && Threads.get(uuid)[EXPT] != null && !Threads.get(uuid)[EXPT].isCancelled())Threads.get(uuid)[EXPT].cancel();
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 3.0F, 0.5F);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("Press Shift to cut paracute").create());
			paracute(player);
			setle(player);
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(map.containsKey(event.getPlayer().getUniqueId().toString())) {
			Player player = event.getPlayer();
			Object[] temp = map.get(player.getUniqueId().toString());
			if(temp[Indexes.FLYLEFT.getIndex()] != null && (boolean)temp[Indexes.FLYLEFT.getIndex()]) {
				paracute(player);
			}
		}
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(event.getPlayer().getVehicle() != null && event.getPlayer().getVehicle().getType() == EntityType.ENDER_DRAGON) {
		Player player = event.getPlayer();
		setle(player);
		player.getVehicle().eject();
		Object [] temp = map.get(player.getUniqueId().toString());
		temp[Indexes.FLYLEFT.getIndex()] = true;
		map.put(player.getUniqueId().toString(), temp);
		}
	}
	
	@EventHandler
	public void onMount(EntityMountEvent event) {
		if(event.getMount() instanceof EnderDragon) {
			Player player = (Player) event.getEntity();
			String uuid = player.getUniqueId().toString();
			BukkitRunnable[] tt = new BukkitRunnable[2];
			Threads.put(uuid, tt);
			Object[] temp = map.get(uuid);
			if(temp == null)temp = new Object[7];
			temp[Indexes.EXP.getIndex()] = player.getExp();
			temp[Indexes.LEVEL.getIndex()] = player.getLevel();
			temp[Indexes.COOLDOWN.getIndex()] = 0.0;
			map.put(uuid, temp);
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(player.getVehicle() != null) {
				if(player.getVehicle().getType() == EntityType.ENDER_DRAGON 
						&& player.getVehicle().getCustomName() == "Rideable Ender Dragon") {
					String uuid = player.getUniqueId().toString();
					final Object[] temp = map.get(uuid);
					if((double)temp[Indexes.COOLDOWN.getIndex()] != 0)return;
					DragonFireball fireball = player.getWorld()
							.spawn(player.getLocation().add(player.getLocation().getDirection()
									.multiply(5)), DragonFireball.class);
					fireball.setVelocity(fireball.getDirection().multiply(5));
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_SHOOT, 1.0F, 1.0F);
					temp[Indexes.COOLDOWN.getIndex()] = 10.0;
					map.put(uuid, temp);
					
					BukkitRunnable[] tt = new BukkitRunnable[2];
					tt[EXPT] = new BukkitRunnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							temp[Indexes.COOLDOWN.getIndex()] = (double)temp[Indexes.COOLDOWN.getIndex()] - 0.1;
							player.setExp((float) ((double)temp[Indexes.COOLDOWN.getIndex()]/10));
							if((double)temp[Indexes.COOLDOWN.getIndex()] <= 0.1) {
								temp[Indexes.COOLDOWN.getIndex()] = 0.0;
								player.setExp(0.98F);
								player.getWorld().playSound(player.getLocation(), 
										Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.5F);
								this.cancel();
							}
						}
					};
					Threads.put(uuid, tt);
					Threads.get(uuid)[EXPT].runTaskTimer(Rideable_enderdragon.getp(), 0, 1L);
					
				}
			}
		}
	}
}
