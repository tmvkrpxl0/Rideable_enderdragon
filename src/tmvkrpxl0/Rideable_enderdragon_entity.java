package tmvkrpxl0;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.EntityEnderDragon;
import net.minecraft.server.v1_12_R1.World;

public class Rideable_enderdragon_entity extends EntityEnderDragon{
	public Rideable_enderdragon_entity(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void n(){
		try {
		Player player = (Player) passengers.get(0).getBukkitEntity();
		LinkedHashMap<String, Object[]> map = Rideable_enderdragon.getmap();
		setYawPitch(player.getLocation().getYaw()+180, player.getLocation().getPitch());
		String uuid = player.getUniqueId().toString();
		if(map.get(uuid)[Indexes.MULTIPLY.getIndex()] == null) {
			final Object [] temp = map.get(uuid);
			temp[Indexes.MULTIPLY.getIndex()] = 1.0;
			map.put(uuid, temp);
		}
		Location loc = getBukkitEntity().getLocation().add(player.getLocation().getDirection().multiply((double)map.get(uuid)[Indexes.MULTIPLY.getIndex()]));
		Location loc1 = new Location(loc.getWorld(), loc.getBlockX()+4, loc.getBlockY()+10, loc.getBlockZ()+4);
		Location loc2 = new Location(loc.getWorld(), loc.getBlockX()-4, loc.getBlockY(), loc.getBlockZ()-4);
		Location l = null;
		setCustomName("Rideable Ender Dragon");
		if((boolean)map.get(uuid)[Indexes.DESTROY_BLOCK.getIndex()]) {
			for(int x = loc2.getBlockX();x<loc1.getBlockX();x++) {
				for(int y = loc2.getBlockY();y<loc1.getBlockY();y++) {
					for(int z = loc2.getBlockZ();z<loc1.getBlockZ();z++) {
						l = new Location(getBukkitEntity().getWorld(), (double)x,(double)y,(double)z);
						if(l.getBlock().getType() != Material.BEDROCK)l.getBlock().setType(Material.AIR);
					}
				}
			}
		}
		this.setPosition(loc.getX(), loc.getY(), loc.getZ());
		if((boolean)map.get(uuid)[Indexes.DAMAGE_ENTITY.getIndex()]) {
			for(Entity e : getBukkitEntity().getNearbyEntities(loc.getX(), loc.getY(), loc.getZ())) {
				if(e.equals(player) == false && getBukkitEntity().getLocation().distance(e.getLocation()) <= 5 && e instanceof LivingEntity){
					((LivingEntity) e).damage(5);
				}
			}
		}
		}catch(IndexOutOfBoundsException e) {
				setHealth(0);
				getBukkitEntity().remove();
				return;
			}
		}
	}
