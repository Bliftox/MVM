package org.harbingers_of_chaos.mvm.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.dimension.DimensionType;
import org.harbingers_of_chaos.mystiversemodserver.MystiVerseModServer;

public class Start {
    private static MinecraftServer minecraftServer = MystiVerseModServer.getMinecraftServer();

    private static final String permProtected = "mvs.protected";
    private static final String permModify = "mvs.can_modify";
    private static final String msgProtected = "Requested inventory is protected";
    public static int start(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();

        ServerPlayerEntity requestedPlayer = getRequestedPlayer(context,"");
        ServerPlayerEntity requestedPlayer1 = getRequestedPlayer(context,"1");
        ServerPlayerEntity requestedPlayer2 = getRequestedPlayer(context,"2");
        ServerPlayerEntity requestedPlayer3 = getRequestedPlayer(context,"3");

        ItemStack a = Items.STICK.getDefaultStack();
        ItemStack b = Items.STICK.getDefaultStack();
        ItemStack c = Items.STICK.getDefaultStack();
        ItemStack d = Items.STICK.getDefaultStack();


        NbtList list = new NbtList();
        NbtCompound tag1 = new NbtCompound();
        tag1.putBoolean("italic", false);
        tag1.putString("text", "aaaa");
        list.add(tag1);
        final char dm = (char) 34;
        //a.setCustomName(Text.of("ded"));
        NbtCompound nbtCompound = a.getOrCreateSubNbt("display");
//            nbtCompound.put("Name", "[{\"text\":\"Яд\",\"italic\":false}]");
        NbtCompound aNBT = a.getNbt();
//                a.getOrCreateNbt().putString("Name", "DED");
                a.getOrCreateNbt().putInt("CustomModelDate", Math.max(0, 100000000));


        boolean canModify = Permissions.check(context.getSource(), permModify, true);

        Permissions.check(requestedPlayer.getUuid(), permProtected, false).thenAcceptAsync(isProtected -> {
            if (isProtected) {
                context.getSource().sendError(Text.literal(msgProtected));
            } else {

                requestedPlayer.giveItemStack(a);
//                requestedPlayer.sendMessage(Text.of(String.valueOf(aNBT.getInt("custom_model_data"))));
                requestedPlayer.sendMessage(Text.of(aNBT.toString()));
                requestedPlayer.sendMessage(Text.of("3"));
                /*
                SimpleGui gui = new SavingPlayerDataGui(ScreenHandlerType.GENERIC_9X5, player, requestedPlayer);
                gui.setTitle(requestedPlayer.getName());
                for (int i = 0; i < requestedPlayer.getInventory().size(); i++) {
                    gui.setSlotRedirect(i, canModify ? new Slot(requestedPlayer.getInventory(), i, 0, 0) : new UnmodifiableSlot(requestedPlayer.getInventory(), i));
                }

                gui.open();*/
            }
        });

        return 1;
    }
    public static int send(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        CommandManager commandManager = player.getServer().getCommandManager();
        commandManager.executeWithPrefix(context.getSource(),"give " + player.getGameProfile().getName() + " stick{CustomModelData:1000,display:{Name:'[{\"text\":\"Яд\"}]',Lore:['[{\"text\":\"sa\",\"italic\":false}]']}} "); //(player, "command to execute WITHOUT /");
        /*String text = null;
        try {
            text = MessageArgumentType.getMessage(context,"a").getString();
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }


        if (player == null || text.trim().isEmpty()) {
            return 0;
        }
        if (text.startsWith("/")) {
            player.sendCommand(text.substring(1).trim());

        } else {
            player.sendMessage(Text.of(text.trim()));
        }*/
        return 1;
    }
    public static int inv(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerPlayerEntity requestedPlayer = getRequestedPlayer(context,"");
        assert player != null;

        boolean canModify = Permissions.check(context.getSource(), permModify, true);

        Permissions.check(requestedPlayer.getUuid(), permProtected, false).thenAcceptAsync(isProtected -> {
            if (isProtected) {
                context.getSource().sendError(Text.literal(msgProtected));
            } else {

                requestedPlayer.sendMessage(Text.of("gh"));/*
                SimpleGui gui = new SavingPlayerDataGui(ScreenHandlerType.GENERIC_9X5, player, requestedPlayer);
                gui.setTitle(requestedPlayer.getName());
                for (int i = 0; i < requestedPlayer.getInventory().size(); i++) {
                    gui.setSlotRedirect(i, canModify ? new Slot(requestedPlayer.getInventory(), i, 0, 0) : new UnmodifiableSlot(requestedPlayer.getInventory(), i));
                }

                gui.open();*/
            }
        });

        return 1;

    }

    public static int eChest(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerPlayerEntity requestedPlayer = getRequestedPlayer(context,"");
        EnderChestInventory requestedEchest = requestedPlayer.getEnderChestInventory();

        boolean canModify = Permissions.check(context.getSource(), permModify, true);

        Permissions.check(requestedPlayer.getUuid(), permProtected, false).thenAcceptAsync(isProtected -> {
            if (isProtected) {
                context.getSource().sendError(Text.literal(msgProtected));
            } else {
                player.sendMessage(Text.of("gh"));/*
                SimpleGui gui = new SavingPlayerDataGui(ScreenHandlerType.GENERIC_9X3, player, requestedPlayer);
                gui.setTitle(requestedPlayer.getName());
                for (int i = 0; i < requestedEchest.size(); i++) {
                    gui.setSlotRedirect(i, canModify ? new Slot(requestedEchest, i, 0, 0) : new UnmodifiableSlot(requestedEchest, i));
                }

                gui.open();*/
            }
        });

        return 1;
    }





    private static ServerPlayerEntity getRequestedPlayer(CommandContext<ServerCommandSource> context, String number)
            throws CommandSyntaxException {
        GameProfile requestedProfile = GameProfileArgumentType.getProfileArgument(context, "target" + number).iterator().next();
//        publicKey = ClientPlayNetworkHandler.getPlayerListEntry(requestedProfile.getName()).getPublicKeyData();
        ServerPlayerEntity requestedPlayer = minecraftServer.getPlayerManager().getPlayer(requestedProfile.getName());

        if (requestedPlayer == null) {
            requestedPlayer = minecraftServer.getPlayerManager().createPlayer(requestedProfile,null);
            NbtCompound compound = minecraftServer.getPlayerManager().loadPlayerData(requestedPlayer);
            if (compound != null) {
                ServerWorld world = minecraftServer.getWorld(
                        DimensionType.worldFromDimensionNbt(new Dynamic<>(NbtOps.INSTANCE, compound.get("Dimension")))
                                .result().get());

                if (world != null) {
                    requestedPlayer.setWorld(world);
                }
            }
        }

        return requestedPlayer;
    }
}