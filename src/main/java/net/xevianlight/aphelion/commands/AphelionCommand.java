package net.xevianlight.aphelion.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.block.Blocks;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.core.space.SpacePartitionSavedData;
import net.xevianlight.aphelion.entites.vehicles.RocketEntity;
import net.xevianlight.aphelion.util.RocketStructure;
import net.xevianlight.aphelion.util.SpacePartitionHelper;

import java.util.EnumSet;

public class AphelionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("aphelion")
                .requires(source -> source.hasPermission(3))
                .then(Commands.literal("station")
                        .then(Commands.literal("orbit")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("pos", ColumnPosArgument.columnPos())
                                                .then(Commands.argument("orbit", ResourceLocationArgument.id())
                                                        .executes(context -> {
                                                            int x = SpacePartitionHelper.get(ColumnPosArgument.getColumnPos(context, "pos").x());
                                                            int z = SpacePartitionHelper.get(ColumnPosArgument.getColumnPos(context, "pos").z());
                                                            ResourceLocation orbit = ResourceLocationArgument.getId(context, "orbit");

                                                            ServerLevel level = context.getSource().getLevel();
                                                            SpacePartitionSavedData.get(level).setOrbitForPartition(x, z, orbit);

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable("command.aphelion.station.orbit.set", x, z, orbit.toString()),
                                                                    true
                                                            );

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("all")
                                                .then(Commands.argument("orbit", ResourceLocationArgument.id())
                                                        .executes(context -> {
                                                            ResourceLocation orbit = ResourceLocationArgument.getId(context, "orbit");

                                                            ServerLevel level = context.getSource().getLevel();

                                                            SpacePartitionSavedData.get(level).overwriteAllExistingOrbits(orbit);

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable("command.aphelion.station.orbit.overwrite_all", orbit.toString()),
                                                                    true
                                                            );

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                )
                                .then(Commands.literal("get")
                                        .then(Commands.argument("pos", ColumnPosArgument.columnPos())
                                                .executes(context -> {
                                                    int x = SpacePartitionHelper.get(ColumnPosArgument.getColumnPos(context, "pos").x());
                                                    int z = SpacePartitionHelper.get(ColumnPosArgument.getColumnPos(context, "pos").z());

                                                    ServerLevel level = context.getSource().getLevel();
                                                    ResourceLocation orbit = SpacePartitionSavedData.get(level).getOrbitForPartition(x, z);

                                                    if (orbit != null) {
                                                        context.getSource().sendSuccess(
                                                                () -> Component.translatable("command.aphelion.station.orbit.get", x, z, orbit.toString()),
                                                                true
                                                        );
                                                    } else {
                                                        context.getSource().sendSuccess(
                                                                () -> Component.translatable("command.aphelion.station.orbit.get.unassigned", x, z),
                                                                true
                                                        );
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                                .then(Commands.literal("clear")
                                        .then(Commands.argument("pos", ColumnPosArgument.columnPos())
                                                .executes(context -> {
                                                    int x = SpacePartitionHelper.get(ColumnPosArgument.getColumnPos(context, "pos").x());
                                                    int z = SpacePartitionHelper.get(ColumnPosArgument.getColumnPos(context, "pos").z());

                                                    ServerLevel level = context.getSource().getLevel();

                                                    boolean success = SpacePartitionSavedData.get(level).clearOrbitForPartition(x, z);

                                                    if (success) {
                                                        context.getSource().sendSuccess(
                                                                () -> Component.translatable("command.aphelion.station.orbit.cleared", x, z),
                                                                true
                                                        );
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                        .then(Commands.literal("all")
                                                .executes(context -> {
                                                    ServerLevel level = context.getSource().getLevel();

                                                    SpacePartitionSavedData.get(level).clearAllOrbits();

                                                    context.getSource().sendSuccess(
                                                            () -> Component.translatable("command.aphelion.station.orbit.clear_all"),
                                                            true
                                                    );

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                                .then(Commands.literal("debug")
                                        .then(Commands.literal("posToKey")
                                                .then(Commands.argument("pos", ColumnPosArgument.columnPos())
                                                        .executes(context -> {
                                                            ServerLevel level = context.getSource().getLevel();

                                                            int x = SpacePartitionHelper.get(ColumnPosArgument.getColumnPos(context,"pos").x());
                                                            int z = SpacePartitionHelper.get(ColumnPosArgument.getColumnPos(context,"pos").z());

                                                            long key = SpacePartitionSavedData.pack(x,z);

                                                            Component clickableOutput = Component.literal(String.valueOf(key))
                                                                    .withStyle(style -> style
                                                                            .withClickEvent(new ClickEvent(
                                                                                    ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                                                    String.valueOf(key)
                                                                            ))
                                                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click")))
                                                                            .withColor(ChatFormatting.AQUA)
                                                                    );

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable("command.aphelion.station.orbit.debug.posToKey", x, z, clickableOutput),
                                                                    true
                                                            );

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("keyToPos")
                                                .then(Commands.argument("key", LongArgumentType.longArg(0, Long.MAX_VALUE))
                                                        .executes(context -> {
                                                            ServerLevel level = context.getSource().getLevel();


                                                            long key = LongArgumentType.getLong(context,"key");

                                                            int x = SpacePartitionSavedData.unpackX(key);
                                                            int z = SpacePartitionSavedData.unpackZ(key);

                                                            String stationCoord = x + " " + z;

                                                            Component clickableOutput = ComponentUtils.wrapInSquareBrackets(Component.literal(stationCoord))
                                                                    .withStyle(style -> style
                                                                            .withClickEvent(new ClickEvent(
                                                                                    ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                                                    stationCoord
                                                                            ))
                                                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click")))
                                                                            .withColor(ChatFormatting.GREEN)
                                                                    );

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable("command.aphelion.station.orbit.debug.keyToPos", key, clickableOutput),
                                                                    true
                                                            );

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("getPartition")
                                                .then(Commands.argument("pos", ColumnPosArgument.columnPos())
                                                        .executes(context -> {

                                                            int x = ColumnPosArgument.getColumnPos(context, "pos").x();
                                                            int z = ColumnPosArgument.getColumnPos(context, "pos").z();

                                                            String stationCoord = SpacePartitionHelper.get(x) + " " + SpacePartitionHelper.get(z);

                                                            Component clickableOutput = ComponentUtils.wrapInSquareBrackets(Component.literal(stationCoord))
                                                                    .withStyle(style -> style
                                                                            .withClickEvent(new ClickEvent(
                                                                                    ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                                                    stationCoord
                                                                            ))
                                                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click")))
                                                                            .withColor(ChatFormatting.GREEN)
                                                                    );

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable("command.aphelion.station.orbit.debug.getPartition", x, z, clickableOutput),
                                                                    true
                                                            );

                                                            return Command.SINGLE_SUCCESS;
                                                        })))
                                )
                        )
                        .then(Commands.literal("tp")
                                .then(Commands.argument("x", IntegerArgumentType.integer())
                                        .then(Commands.argument("z", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    var player = context.getSource().getEntity();

                                                    double x = (double) IntegerArgumentType.getInteger(context, "x");
                                                    double z = (double) IntegerArgumentType.getInteger(context, "z");

                                                    int destX = (int) Math.floor(x * SpacePartitionHelper.SIZE) + (SpacePartitionHelper.SIZE / 2);
                                                    int destZ = (int) Math.floor(z * SpacePartitionHelper.SIZE) + (SpacePartitionHelper.SIZE / 2);

                                                    String stationCoord = x + ", " + z;

                                                    long key = SpacePartitionSavedData.pack((int) x, (int) z);

                                                    Component clickablePos = ComponentUtils.wrapInSquareBrackets(Component.literal(stationCoord))
                                                            .withStyle(style -> style
                                                                    .withClickEvent(new ClickEvent(
                                                                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                                            stationCoord
                                                                    ))
                                                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click")))
                                                                    .withColor(ChatFormatting.GREEN)
                                                            );

                                                    Component clickableId = Component.literal(String.valueOf(key))
                                                            .withStyle(style -> style
                                                                    .withClickEvent(new ClickEvent(
                                                                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                                            String.valueOf(key)
                                                                    ))
                                                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click")))
                                                                    .withColor(ChatFormatting.AQUA)
                                                            );

                                                    ServerLevel space = player.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Aphelion.MOD_ID, "space")));

                                                    if (player != null) {
                                                        player.teleportTo(space, destX, player.position().y, destZ, EnumSet.noneOf(RelativeMovement.class), player.getYRot(), player.getXRot());

                                                        context.getSource().sendSuccess(
                                                                () -> Component.translatable("command.aphelion.station.teleport.success", player.getDisplayName(), clickablePos, clickableId),
                                                                true
                                                        );

                                                        return Command.SINGLE_SUCCESS;
                                                    }

                                                    context.getSource().sendFailure(
                                                            Component.translatable("command.aphelion.station.teleport.failure")
                                                    );

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )
                )
                .then(Commands.literal("planet")
                        .then(Commands.literal("tp")
                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                        .executes(context -> {

                                            var player = context.getSource().getEntity();
                                            if (player == null || player.getServer() == null) {
                                                context.getSource().sendFailure(Component.translatable("command.aphelion.station.teleport.failure"));
                                                return Command.SINGLE_SUCCESS;
                                            }

                                            var targetDim = DimensionArgument.getDimension(context, "dimension");

                                            ServerLevel targetLevel = player.getServer().getLevel(targetDim.dimension());

                                            if (targetLevel == null) {
                                                context.getSource().sendFailure(Component.translatable("command.aphelion.station.teleport.failure.invalid_level"));
                                                return Command.SINGLE_SUCCESS;
                                            }

                                            player.teleportTo(targetLevel, player.position().x, player.position().y, player.position().z, EnumSet.noneOf(RelativeMovement.class), player.getYRot(), player.getXRot());

                                            return Command.SINGLE_SUCCESS;
                                        }))
                        )
                )
                .then(Commands.literal("rocket")
                        .then(Commands.literal("summon")
                                .executes(context -> {
                                    RocketStructure structure = new RocketStructure(s -> {
                                        s.add(0,0,0, Blocks.IRON_BLOCK.defaultBlockState());
                                    });
                                    context.getSource().sendSuccess(() -> Component.translatable("command.aphelion.rocket.spawn.success"), true);
                                    RocketEntity rocket = RocketEntity.spawnRocket(context.getSource().getLevel(), context.getSource().getEntity().blockPosition(), structure);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.argument("entity", EntityArgument.entity())
                                .then(Commands.literal("structure")
                                        .then(Commands.literal("set")
                                                .then(Commands.argument("nbt", CompoundTagArgument.compoundTag())
                                                        .executes(context -> {
                                                            Entity entity = EntityArgument.getEntity(context, "entity");
                                                            if (entity instanceof RocketEntity rocket) {
                                                                RocketStructure structure = new RocketStructure(RocketStructure::clear);
                                                                structure.load(CompoundTagArgument.getCompoundTag(context, "nbt"));
                                                                rocket.setStructure(structure);
                                                            } else {
                                                                context.getSource().sendFailure(Component.translatable("command.aphelion.rocket.entity_invalid"));
                                                            }
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("get")
                                                .executes(context -> {
                                                    Entity entity = EntityArgument.getEntity(context, "entity");

                                                    if (entity instanceof RocketEntity rocket) {
                                                        RocketStructure structure = rocket.getStructure();
                                                        CompoundTag tag = structure.save();

                                                        Component clickableId = Component.literal(tag.toString())
                                                                .withStyle(style -> style
                                                                        .withClickEvent(new ClickEvent(
                                                                                ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                                                tag.toString()
                                                                        ))
                                                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click")))
                                                                        .withColor(ChatFormatting.AQUA)
                                                                );

                                                        context.getSource().sendSuccess(
                                                                () -> clickableId,
                                                                true
                                                        );
                                                    } else {
                                                        context.getSource().sendFailure(Component.translatable("command.aphelion.rocket.entity_invalid"));
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )
                )

        );
    }
}
