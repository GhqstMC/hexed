package lol.nea.mixintesting.mixin;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import lol.nea.mixintesting.MixinTesting;

import java.awt.*;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @Shadow private int textColor;
    @Shadow private float alpha;

    @Shadow(remap = false) protected abstract void setColor(float r, float g, float b, float a);

    private Character charCaptured = null;
    private int indexCaptured = 0;

    private boolean increaseIndex = false;

    @Redirect(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 1))
    private char captureChar(String instance, int index) {
        char c = instance.charAt(index);
        charCaptured = c;
        indexCaptured = index;
        return c;
    }

    @Inject(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;setColor(FFFF)V", ordinal = 1, shift = At.Shift.BY, by = 2, remap = false), remap = true)
    private void onStringRendered_setColor(String text, boolean shadow, CallbackInfo ci) {
        if (charCaptured != null) {
            if (charCaptured == 'x') {
                try {
                    String hexCode = text.substring(indexCaptured + 1, indexCaptured + 7);
                    Color color = Color.decode("#" + hexCode);
                    float r = shadow ? color.getRed() / 4f : color.getRed();
                    float g = shadow ? color.getGreen() / 4f : color.getGreen();
                    float b = shadow ? color.getBlue() / 4f : color.getBlue();
                    textColor = MixinTesting.Companion.getRGBA((int) r, (int) g, (int) b, (int) (alpha * 255));
                    setColor(r / 255f, g / 255f, b / 255f, alpha);
                    increaseIndex = true;
                } catch(Exception ignored) {

                }

            } else if (charCaptured == 'w') {
                Color color = MixinTesting.Companion.getRainbowColor(indexCaptured);
                float r = shadow ? color.getRed() / 4f : color.getRed();
                float g = shadow ? color.getGreen() / 4f : color.getGreen();
                float b = shadow ? color.getBlue() / 4f : color.getBlue();
                textColor = MixinTesting.Companion.getRGBA((int) r, (int) g, (int) b, (int) (alpha * 255));
                setColor(r / 255f, g / 255f, b / 255f, alpha);
            }
        }
        charCaptured = null;
    }

    @ModifyVariable(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;setColor(FFFF)V", ordinal = 1, shift = At.Shift.BY, by = 3, remap = false), name = "i")
    private int onStringRendered_indexIncrease(int index) {
        if (increaseIndex) {
            increaseIndex = false;
            return index + 6;
        }
        return index;
    }

}
