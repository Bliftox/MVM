package org.harbingers_of_chaos.mvm.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.harbingers_of_chaos.mvlib.AuthAccount;
import org.harbingers_of_chaos.mvlib.SQL;
import org.harbingers_of_chaos.mvlib.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

@Mixin(PlayerManager.class)
public class PlayerListMixin {

    @Inject(method = "checkCanJoin", at = @At("TAIL"), cancellable = true)
    public void canPlayerLogin(SocketAddress $$0, GameProfile $$1, CallbackInfoReturnable<Text> cir) {
        // Check if maintenance mode is enabled and kick the player
        if (Config.instance != null) {
            String ip = $$0.toString().substring(1, $$0.toString().indexOf(":"));
            String name = $$1.getName();
            if(Config.instance.game.questionnaire) {
                LOGGER.info("[MVM] Connect with ip : " + ip);
                LOGGER.info("[MVM] Connect with name : " + name);

                if (SQL.hasPlayer(name)) {
//                    LOGGER.info("[MVM] Создайте заявку в дискорд сервере!\n");
                    Text reason = Text.empty()
                            .append(Text.literal("Создайте заявку в дискорд сервере!\n"))
                            .append(Text.literal("И ожидайте одобрение администрацией сервера.\n"));
                    cir.setReturnValue(reason);
                } else if (!SQL.hasIP(name,ip)) {
//                    String id = SQL.getPlayerId2Nickname(name);
//                    ACCOUNT_LINKING.tryQueueForLinking(ip, id);
//                    String code = ACCOUNT_LINKING.getCode(ip);
////                    LOGGER.info("[MVM] Ваш код авторизации : "+code );
//
//                    Text reason = Text.empty()
//                            .append(Text.literal("Ваш код авторизации "))
//                            .append(Text.literal(code)
//                                    .formatted(Formatting.BLUE, Formatting.UNDERLINE))
//                            .append(Text.literal("\nОтправте его в лс боту Chorny"));
//                    cir.setReturnValue(reason);
                }
            }else{
                AuthAccount.addPlayer(name,ip);
            }
        }
    }
}
