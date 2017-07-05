package fr.syst3ms.quarsk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.PotionEffectUtils;
import com.sun.istack.internal.Nullable;
import fr.syst3ms.quarsk.classes.EnumType;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.Bukkit;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

@SuppressWarnings({"unchecked"})
public class QuarSk extends JavaPlugin {
	private static SkriptAddon addonInstance;
	private static JavaPlugin plugin;

	public static SkriptAddon getAddon() {
		return addonInstance;
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}

	public static void debug(Object... l) {
		Arrays.asList(l).forEach(s -> Bukkit.getLogger().log(Level.INFO, s.toString()));
	}

	public void onEnable() {
		if (getServer().getBukkitVersion().startsWith("1.7")) {
			getLogger().log(Level.INFO, "");
			getServer().getPluginManager().disablePlugin(this);
		}
		getLogger().log(Level.INFO,  "Starting up QuarSk v" + getPlugin().getDescription().getVersion() + " !");
		plugin = this;
		normalRegister();
		if (Registration.generateFolder()) {
			getLogger().log(Level.INFO, "Created QuarSk's folder !");
		}
		if (Registration.generateSyntaxFile()) {
			getLogger().log(Level.INFO, "Generated QuarSk's syntax file !");
		}
		getLogger().log(Level.INFO,
			"Registered " + Registration.getEvents().size() + " events, " + Registration.getConditions().size()
			+ " conditions, " + Registration.getEffects().size() + " effects and " + Registration
				.getExpressions()
				.size() + " expressions ! Good game !"
		);
	}

	private void normalRegister() {
		addonInstance = Skript.registerAddon(this).setLanguageFileDirectory("lang");
		/*
		 * TYPES
         */
		//Potions
		Classes.registerClass(new ClassInfo<>(PotionEffect.class, "potioneffect")
			.name("potioneffect")
			.description("A getter for potion effects")
			.user("potion ?effect")
			.parser(new Parser<PotionEffect>() {
				@Override
				@Nullable
				public PotionEffect parse(String obj, ParseContext context) {
					return null;
				}

				@NotNull
				@Override
				public String toString(@NotNull PotionEffect potionEffect, int i) {
					return PotionEffectUtils.toString(potionEffect.getType()) + " of tier "
						   + potionEffect.getAmplifier() + " lasting " + potionEffect.getDuration() + " with particles "
						   + (potionEffect.hasParticles() ? "enabled" : "disabled") + ", ambient effect " + (
							   potionEffect.isAmbient()
								   ? "enabled"
								   : "disabled");
				}

				@NotNull
				@Override
				public String toVariableNameString(@NotNull PotionEffect potionEffect) {
					return potionEffect.getType().toString().toLowerCase() + "," + potionEffect.getAmplifier() + ","
						   + potionEffect.getDuration() + "," + potionEffect.hasParticles() + ","
						   + potionEffect.isAmbient() + "," + potionEffect.getColor().toString().toLowerCase();
				}

				@NotNull
				public String getVariableNamePattern() {
					return "[a-z]+,\\d+,\\d+,(true|false),(true|false),[a-z]+";
				}
			}));
		//Banners
		Classes.registerClass(new ClassInfo<>(Pattern.class, "bannerlayer")
			.name("banner layer")
			.description("A getter for banner layers")
			.user("banner ?(layer|pattern)")
			.parser(new Parser<Pattern>() {
				@org.jetbrains.annotations.Nullable
				@Override
				public Pattern parse(String s, ParseContext parseContext) {
					return null;
				}

				@NotNull
				@Override
				public String toString(@NotNull Pattern pattern, int i) {
					return "layer with pattern " + EnumType.toString(pattern.getPattern().name()) + " and color "
						   + pattern.getColor().toString().toLowerCase();
				}

				@NotNull
				@Override
				public String toVariableNameString(@NotNull Pattern pattern) {
					return "bannerlayer," + pattern.getPattern().toString().toLowerCase() + "," + pattern
						.getColor()
						.toString()
						.toLowerCase();
				}

				@NotNull
				@Override
				public String getVariableNamePattern() {
					return "bannerlayer,[a-z]+,[a-z]+";
				}
			}));
		EnumType.newType(PatternType.class, "bannerpattern", "banner ?pattern(?: ?type)?");
		// Event values
		EventValues.registerEventValue(PotionSplashEvent.class,
			Projectile.class,
			new Getter<Projectile, PotionSplashEvent>() {
				@Override
				public Projectile get(PotionSplashEvent potionSplashEvent) {
					return potionSplashEvent.getEntity();
				}
			},
			0
		);
		EventValues.registerEventValue(LingeringPotionSplashEvent.class,
			Projectile.class,
			new Getter<Projectile, LingeringPotionSplashEvent>() {
				@Override
				public Projectile get(LingeringPotionSplashEvent lingeringPotionSplashEvent) {
					return lingeringPotionSplashEvent.getEntity();
				}
			},
			0
		);
		EventValues.registerEventValue(LingeringPotionSplashEvent.class,
			Entity.class,
			new Getter<Entity, LingeringPotionSplashEvent>() {
				@Override
				public Entity get(LingeringPotionSplashEvent lingeringPotionSplashEvent) {
					return lingeringPotionSplashEvent.getEntity();
				}
			},
			0
		);
		//Syntax registration
		try {
			getAddon().loadClasses("fr.syst3ms.quarsk", "conditions", "effects", "events", "expressions");
			getAddon().loadClasses("fr.syst3ms.quarsk.effects", "banner", "potion");
			getAddon().loadClasses("fr.syst3ms.quarsk.expressions", "banner", "beacon", "eventvalues", "potion");
		} catch (IOException e) {
			Skript.exception(e,
				"An error has occured while registering Quarsk's syntax",
				"Report this error to me, Syst3ms, and I will (hopefully) fix it"
			);
		}
	}

	public void onDisable() {

	}
}

