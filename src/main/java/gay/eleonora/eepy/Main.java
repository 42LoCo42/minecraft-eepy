package gay.eleonora.eepy;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerNegotiationEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod(Main.MODID)
public class Main {
	public static final long MS = 1000;

	public static final String MODID = "eepy";
	private static final Logger LOGGER = LogUtils.getLogger();

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private long lastLogin = 0;

	public Main() {
		MinecraftForge.EVENT_BUS.register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	public Runnable mkTask(ServerStartedEvent event, long deadline) {
		return () -> {
			var now = System.currentTimeMillis();
			if(now < lastLogin + Config.afterLoginSeconds * MS) {
				LOGGER.debug("eepy check too early since last login, skipping");
			} else if(now > deadline + Config.afterDeadlineSeconds * MS) {
				LOGGER.debug("eepy check more than 1s too late, skipping");
			} else {
				LOGGER.debug("running eepy check");

				var players = event.getServer().getPlayerCount();
				LOGGER.debug("players online: {}", players);

				if(players <= 0) {
					LOGGER.info("the server is very eepy (＿ ＿*) Z z z");

					try {
						Runtime.getRuntime().exec(new String[]{
							"kill", "-SIGSTOP",
							String.valueOf(ProcessHandle.current().pid()),
						});
					} catch(IOException e) {
						LOGGER.error("was rudely interrupted while trying to eep: {}", e);
					}
				}
			}

			scheduler.schedule(
				mkTask(event, now + Config.delaySeconds * MS),
				Config.delaySeconds, TimeUnit.SECONDS);
		};
	}

	@SubscribeEvent
	public void onServerStarted(final ServerStartedEvent event) {
		LOGGER.info("eepy has started :3, delay is {} seconds", Config.delaySeconds);
		scheduler.schedule(mkTask(event, System.currentTimeMillis()), 0, TimeUnit.SECONDS);
	}

	@SubscribeEvent
	public void onPlayerNegotiation(final PlayerNegotiationEvent event) {
		lastLogin = System.currentTimeMillis();
	}
}
