package gay.eleonora.eepy;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec.LongValue DELAY_SECONDS = BUILDER
		.comment("Number of seconds to delay between checks")
		.defineInRange("delaySeconds", 60, 1, Long.MAX_VALUE);

	public static final ForgeConfigSpec.LongValue AFTER_LOGIN_SECONDS = BUILDER
		.comment("If a check happens less than X seconds after a player login attempt, it's skipped")
		.defineInRange("afterLoginSeconds", 60, 1, Long.MAX_VALUE);

	public static final ForgeConfigSpec.LongValue AFTER_DEADLINE_SECONDS = BUILDER
		.comment("If a check happens more than X seconds after its scheduled time, it's skipped")
		.defineInRange("afterDeadlineSeconds", 1, 1, Long.MAX_VALUE);

	static final ForgeConfigSpec SPEC = BUILDER.build();

	public static long delaySeconds;
	public static long afterLoginSeconds;
	public static long afterDeadlineSeconds;

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		delaySeconds = DELAY_SECONDS.get();
		afterLoginSeconds = AFTER_LOGIN_SECONDS.get();
		afterDeadlineSeconds = AFTER_DEADLINE_SECONDS.get();
	}
}
