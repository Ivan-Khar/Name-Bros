package com.aqupd.namebros;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Main implements ModInitializer {

  public static Logger LOGGER = LoggerFactory.getLogger("NameBros");

  @Override
  public void onInitialize() {
    Config conf = Config.INSTANCE;
    conf.load();

    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher
        .register(literal("namebros").requires(source -> Permissions.check(source, "namebros.command.use", 3))
        .then(literal("addblacklist").then(argument("Entity name", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes(ctx -> {
          String eName = RegistryEntryArgumentType.getSummonableEntityType(ctx, "Entity name").getKey().get().getValue().toString();
          if(conf.addBlacklist(eName)) ctx.getSource().sendMessage(Text.literal("Added entity \"" + eName + "\" to the blacklist"));
          else ctx.getSource().sendMessage(Text.literal("Entity \"" + eName + "\" already blacklisted"));
          return 1;
        })))
        .then(literal("removeblacklist").then(argument("Entity name", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes(ctx -> {
          String eName = RegistryEntryArgumentType.getSummonableEntityType(ctx, "Entity name").getKey().get().getValue().toString();
          if(conf.removeBlackList(eName)) ctx.getSource().sendMessage(Text.literal("Removed entity \"" + eName + "\" from the blacklist"));
          else ctx.getSource().sendMessage(Text.literal("Entity \"" + eName + "\" not blacklisted"));
          return 1;
        })))
        .then(literal("debug").executes(ctx -> {
          conf.toggleDebug();
          ctx.getSource().sendMessage(Text.literal("Debug mode is: " + (conf.isDebug()?"ON":"OFF")));
          return 1;
        }))
        .then(literal("enable").executes(ctx -> {
          conf.toggleEnable();
          ctx.getSource().sendMessage(Text.literal("Mod is " + (conf.enabled()?"enabled":"disabled")));
          return 1;
        }))
        .then(literal("reload").executes(ctx -> {
          conf.load();
          ctx.getSource().sendMessage(Text.literal("Reloaded config files"));
          return 1;
        }))
    )));
    LOGGER.info("Mod initialized");
  }
}
