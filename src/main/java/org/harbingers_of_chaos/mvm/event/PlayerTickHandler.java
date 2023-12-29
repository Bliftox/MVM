package org.harbingers_of_chaos.mvm.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.Random;

public class PlayerTickHandler implements ServerTickEvents.StartTick {
    Item item;
    ItemStack itemStack;
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if(((this.item = player.getStackInHand(Hand.OFF_HAND).getItem()) == Items.COMPASS && (this.itemStack = this.item.getDefaultStack()).hasNbt())
                    || ((this.item = player.getStackInHand(Hand.MAIN_HAND).getItem()) == Items.COMPASS && (this.itemStack = this.item.getDefaultStack()).hasNbt())
                    && itemStack.getNbt().getInt("CustomModelData") == 1000){

            }

        }
    }
}
