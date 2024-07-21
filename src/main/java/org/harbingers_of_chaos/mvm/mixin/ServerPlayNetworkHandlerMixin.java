package org.harbingers_of_chaos.mvm.mixin;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;
    @Shadow public abstract void sendPacket(Packet<?> packet);

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "onPlayerMove", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
            shift = At.Shift.AFTER), cancellable = true)
    public void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {

        LOGGER.info(player.getEntityName() + "  onPlayerMove");
        ci.cancel();
    }

    @Inject(method = "onPlayerAction", at = @At("HEAD"), cancellable = true)
    public void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        LOGGER.info("onPlayerAction");
        ci.cancel();
    }

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    public void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        LOGGER.info("onChatMessage");
        ci.cancel();
    }

    @Inject(method = "onCommandExecution", at = @At("HEAD"), cancellable = true)
    public void onCommandExecution(CommandExecutionC2SPacket packet, CallbackInfo ci) {
        LOGGER.info("onCommandExecution");
        ci.cancel();
    }

    @Inject(method = "onClickSlot", at = @At("HEAD"), cancellable = true)
    public void onClickSlot(ClickSlotC2SPacket packet, CallbackInfo ci) {
        LOGGER.info("onClickSlot");
    }
    
    @Inject(method = "onCreativeInventoryAction", at = @At("HEAD"), cancellable = true)
    public void onCreativeInventoryAction(CreativeInventoryActionC2SPacket packet, CallbackInfo ci) {
        LOGGER.info("onCreativeInventoryAction");
        ci.cancel();
    }
}
