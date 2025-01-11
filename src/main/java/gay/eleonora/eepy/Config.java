package gay.eleonora.eepy;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec.LongValue DELAY_SECONDS = BUILDER
		.comment("Number of seconds to delay between checks")
		.defineInRange("delaySeconds", 60, 1, Long.MAX_VALUE);

	static final ForgeConfigSpec SPEC = BUILDER.build();

	public static long delaySeconds;

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		delaySeconds = DELAY_SECONDS.get();
	}
}
