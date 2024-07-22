package org.harbingers_of_chaos.mvm.mixin;

import com.mojang.brigadier.ParseResults;
import net.minecraft.command.argument.SignedArgumentList;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.message.MessageChain;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.harbingers_of_chaos.mvlib.AuthAccount;
import org.harbingers_of_chaos.mvlib.SQL;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;
    @Shadow public abstract void sendPacket(Packet<?> packet);

    @Shadow @Final private static Logger LOGGER;
    @Shadow protected abstract ParseResults<ServerCommandSource> parse(String command);

    @Shadow protected abstract Map<String, SignedMessage> collectArgumentMessages(CommandExecutionC2SPacket packet, SignedArgumentList<?> arguments, LastSeenMessageList lastSeenMessages) throws MessageChain.MessageChainException;

    @Inject(method = "onPlayerMove", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
            shift = At.Shift.AFTER), cancellable = true)
    public void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        if (AuthAccount.canPlay(this.player.getEntityName())) return;
//        LOGGER.info(player.getEntityName() + "  onPlayerMove");
        ci.cancel();
    }

    @Inject(method = "onPlayerAction", at = @At("HEAD"), cancellable = true)
    public void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (AuthAccount.canPlay(this.player.getEntityName())) return;
//        LOGGER.info("onPlayerAction");
        ci.cancel();

        if (packet.getAction() == PlayerActionC2SPacket.Action.DROP_ITEM || packet.getAction() == PlayerActionC2SPacket.Action.DROP_ALL_ITEMS) {
            /*
                Updates players main hand slot to prevent desync
                This action only gets triggered when dropping from the main hand
             */
            ItemStack stack = this.player.getStackInHand(Hand.MAIN_HAND);
            Packet<ClientPlayPacketListener> packet1 = new ScreenHandlerSlotUpdateS2CPacket(-2, 1, this.player.getInventory().getSlotWithStack(stack), stack);
            this.sendPacket(packet1);
        } else {
            /*
                Sends a block update packet to the client
                Prevents desync between client and server when breaking or placing blocks
            */
            BlockPos blockPos = packet.getPos();
            Packet<ClientPlayPacketListener> packet1 = new BlockUpdateS2CPacket(this.player.getWorld(), blockPos);
            this.sendPacket(packet1);
        }
    }

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    public void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        if (AuthAccount.canPlay(this.player.getEntityName())) return;
        AuthAccount.setAuth(this.player,packet.chatMessage());
//        LOGGER.info(packet.chatMessage() + " onChatMessage");
        ci.cancel();
    }

    @Inject(method = "onCommandExecution", at = @At("HEAD"), cancellable = true)
    public void onCommandExecution(CommandExecutionC2SPacket packet, CallbackInfo ci) {
        if (AuthAccount.canPlay(this.player.getEntityName())) return;
        String command = packet.command();

        int index = command.indexOf(" ");
        if(command.contains(" ")){
            if(!command.substring(0,index).equals("register")) ci.cancel();
        }else if(!command.equals("register")) ci.cancel();
    }


    @Inject(method = "onClickSlot", at = @At("HEAD"), cancellable = true)
    public void onClickSlot(ClickSlotC2SPacket packet, CallbackInfo ci) {
        if (AuthAccount.canPlay(this.player.getEntityName())) return;
//        LOGGER.info("onClickSlot");
        ci.cancel();

        int slot = packet.getSlot();
        if (slot < 0) return; // Clicked outside of the inventory

        ItemStack stack = this.player.getInventory().getStack(slot);
        // ^ packet.getStack() can cause desync

        // Updates clicked slot and the cursor to prevent desync

        Packet<ClientPlayPacketListener> packet1 = new ScreenHandlerSlotUpdateS2CPacket(-2, 1, slot, stack);
        Packet<ClientPlayPacketListener> packet2 = new ScreenHandlerSlotUpdateS2CPacket(-1, 1, -1, ItemStack.EMPTY);

        this.sendPacket(packet1); // Updates inventory slot
        this.sendPacket(packet2); // Updates cursor
    }
    
    @Inject(method = "onCreativeInventoryAction", at = @At("HEAD"), cancellable = true)
    public void onCreativeInventoryAction(CreativeInventoryActionC2SPacket packet, CallbackInfo ci) {
        if (AuthAccount.canPlay(this.player.getEntityName())) return;
        LOGGER.info("onCreativeInventoryAction");
        ci.cancel();

        int slot = packet.getSlot();
        if (slot < 0) return;

        ItemStack stack = this.player.getInventory().getStack(slot);
        // ^ packet.getStack() can cause desync

        // Updates clicked slot and the cursor to prevent desync

        Packet<ClientPlayPacketListener> packet1 = new ScreenHandlerSlotUpdateS2CPacket(-2, 1, slot, stack);
        Packet<ClientPlayPacketListener> packet2 = new ScreenHandlerSlotUpdateS2CPacket(-1, 1, -1, ItemStack.EMPTY);

        this.sendPacket(packet1); // Updates inventory slot
        this.sendPacket(packet2); // Updates cursor
    }
    @Inject(method = "onDisconnected",at = @At("HEAD"),cancellable = true)
    public void onDisconnected(Text player, CallbackInfo ci) {
        AuthAccount.removePlayer(this.player.getEntityName());
    }
}
