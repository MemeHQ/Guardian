package me.valentin.guardian;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import me.valentin.guardian.listener.PlayerListener;
import me.valentin.guardian.alerts.AlertsManager;
import me.valentin.guardian.commands.AlertsCommand;
import me.valentin.guardian.player.PlayerManager;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Guardian extends JavaPlugin {

	private static Optional<Guardian> instance;

	private PlayerManager playerManager;
	private AlertsManager alertsManager;

	@Override
	public void onEnable() {
		Guardian.instance = Optional.of(this);

		this.playerManager = new PlayerManager();
		this.alertsManager = new AlertsManager();

		Arrays.asList(
				new PlayerListener()
		).forEach(l -> this.getServer().getPluginManager().registerEvents(l, this));

		Arrays.asList(
				new AlertsCommand()
		).forEach(this::registerCommand);
	}

	public static Guardian getInstance() {
		return Guardian.instance.orElseThrow(() -> new IllegalArgumentException("Guardian instance is null"));
	}

	private void registerCommand(Command cmd) {
		this.registerCommand(cmd, this.getName());
	}

	public void registerCommand(Command cmd, String fallbackPrefix) {
		MinecraftServer.getServer().server.getCommandMap().register(cmd.getName(), fallbackPrefix, cmd);
	}
}
