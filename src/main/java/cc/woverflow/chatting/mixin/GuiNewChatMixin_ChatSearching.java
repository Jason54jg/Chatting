package cc.woverflow.chatting.mixin;

import cc.woverflow.chatting.chat.ChatSearchingManager;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiNewChat.class)
public class GuiNewChatMixin_ChatSearching {
    @Shadow @Final private List<ChatLine> drawnChatLines;

    @Inject(method = "setChatLine", at = @At("HEAD"))
    private void handleSetChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly, CallbackInfo ci) {
        ChatSearchingManager.getCache().invalidateAll();
    }

    @Redirect(method = "drawChat", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiNewChat;drawnChatLines:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatLine> injected(GuiNewChat instance) {
        return ChatSearchingManager.filterMessages(ChatSearchingManager.INSTANCE.getLastSearch(), drawnChatLines);
    }
}
