package com.aqupd.namebros;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.*;

public class Main implements ModInitializer {

  public static Logger LOGGER = LoggerFactory.getLogger("NameBros");

  @Override
  public void onInitialize() {
    Config conf = Config.INSTANCE;
    conf.load();

    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
      dispatcher.register(literal("namebros").requires(source -> Permissions.check(source, "namebros.command.use", 3))
              .then(literal("add").then(argument("Entity name", EntityArgumentType.entity())).executes(ctx -> {

                return 1;
              }))
              .then(literal("debug").executes(ctx -> {
                conf.toggleDebug();
                ctx.getSource().sendMessage(Text.literal("Debug mode is: " + (conf.isDebug()?"ON":"OFF")));
                return 1;
              })).then(literal("enable").executes(ctx -> {
                conf.toggleEnable();
                ctx.getSource().sendMessage(Text.literal("Mod is " + (conf.enabled()?"enabled":"disabled")));
                return 1;
              }))
      );
    }));
    LOGGER.info("Mod initialized");
  }
}
