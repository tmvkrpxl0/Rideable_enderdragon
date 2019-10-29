package tmvkrpxl0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import org.bukkit.util.StringUtil;

public class ReCommandCompleter implements TabCompleter{
	private final String [] commands = {"destroy_block", "damage_entity", "spawn"};
	private final String [] tf = {"true", "false"};
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		final List<String> ret = new ArrayList<>();
		if(arg3.length == 1) {
			StringUtil.copyPartialMatches(arg3[0], Arrays.asList(commands), ret);
		}
		else if(arg3.length == 2 && (arg3[0].startsWith(commands[0])|| arg3[0].startsWith(commands[1]))) {
				StringUtil.copyPartialMatches(arg3[1], Arrays.asList(tf), ret);
			}
		Collections.sort(ret);
		return ret;
	}
	
}
