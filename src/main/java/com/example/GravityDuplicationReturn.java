package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GravityDuplicationReturn implements ModInitializer {
	public static final String MOD_ID = "gravity-duplication-return";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static GameRule<Boolean> ENABLE_PORTAL_DUPLICATION;
	public static GameRule<Boolean> ENABLE_DRAGON_EGG_DUPLICATION;
	public static final TagKey<EntityType<?>> DUPLICATABLE_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "duplicatable_entities"));

	@Override
	public void onInitialize() {
		ENABLE_PORTAL_DUPLICATION = GameRuleBuilder.forBoolean(true)
				.category(GameRuleCategory.MISC)
				.buildAndRegister(Identifier.withDefaultNamespace("enable_portal_duplication"));

		ENABLE_DRAGON_EGG_DUPLICATION = GameRuleBuilder.forBoolean(true)
				.category(GameRuleCategory.MISC)
				.buildAndRegister(Identifier.withDefaultNamespace("enable_dragon_egg_duplication"));

		LOGGER.info("Gravity Duplication Return initialized.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
