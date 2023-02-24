package lol.nea.mixintesting.mixin;

import net.minecraft.util.ChatAllowedCharacters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatAllowedCharacters.class)
public class MixinChatAllowedCharacters {
    @Inject(method = "filterAllowedCharacters", at = @At("HEAD"), cancellable = true)
    private static void filterAllowedCharacters(String input, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(input);
        cir.cancel();
    }
}
