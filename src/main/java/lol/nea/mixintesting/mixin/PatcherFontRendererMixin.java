package lol.nea.mixintesting.mixin;

import club.sk1er.patcher.mixins.accessors.FontRendererAccessor;
import lol.nea.mixintesting.MixinTesting;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import club.sk1er.patcher.util.enhancement.text.CachedString;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Pseudo
@Mixin(targets = "club.sk1er.patcher.hooks.FontRendererHook", remap = false)
public abstract class PatcherFontRendererMixin {

    @Shadow @Final
    private FontRendererAccessor fontRendererAccessor;

    private CachedString cachedString = null;
    private Character charCaptured = null;
    private int indexCaptured = 0;

    private boolean increaseIndex = false;

    @Dynamic("Patcher")
    @ModifyVariable(method = "renderStringAtPos", at = @At("STORE"), index = 15)
    private CachedString captureCachedString(CachedString value) {
        cachedString = value;
        return cachedString;
    }

    @Dynamic("Patcher")
    @Redirect(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 1))
    private char captureChar(String instance, int index) {
        char c = instance.charAt(index);
        charCaptured = c;
        indexCaptured = index;
        return c;
    }

    @Dynamic("Patcher")
    @Inject(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Lclub/sk1er/patcher/util/enhancement/text/CachedString;setLastRed(F)V", ordinal = 2, shift = At.Shift.BY, by = 2))
    private void onStringRendered_setColor(String text, boolean shadow, CallbackInfoReturnable<Boolean> cir) {
        if (charCaptured != null && cachedString != null) {
            if (charCaptured == 'x') {
                try {
                    String hexCode = text.substring(indexCaptured + 1, indexCaptured + 7);
                    System.out.println("Hex code: " + hexCode);
                    Color color = Color.decode("#" + hexCode);
                    float r = shadow ? color.getRed() / 4f : color.getRed();
                    float g = shadow ? color.getGreen() / 4f : color.getGreen();
                    float b = shadow ? color.getBlue() / 4f : color.getBlue();
                    setColors(r, g, b);
                    increaseIndex = true;
                } catch(Exception ignored) {

                }

            } else if (charCaptured == 'w') {
                Color color = MixinTesting.Companion.getRainbowColor(indexCaptured);
                float r = shadow ? color.getRed() / 4f : color.getRed();
                float g = shadow ? color.getGreen() / 4f : color.getGreen();
                float b = shadow ? color.getBlue() / 4f : color.getBlue();
                setColors(r, g, b);
            }
        }
        charCaptured = null;
    }

    private void setColors(float r, float g, float b) {
        float a = fontRendererAccessor.getAlpha();
        this.fontRendererAccessor.setTextColor(MixinTesting.Companion.getRGBA((int) r, (int) g, (int) b, (int) (a * 255)));
        GlStateManager.color(r / 255f, g / 255f, b / 255f, a);

        cachedString.setLastAlpha(a);
        cachedString.setLastGreen(g / 255f);
        cachedString.setLastBlue(b / 255f);
        cachedString.setLastRed(r / 255f);
    }

    @Dynamic("Patcher")
    @ModifyVariable(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Lclub/sk1er/patcher/util/enhancement/text/CachedString;setLastRed(F)V", ordinal = 2, shift = At.Shift.BY, by = 3, remap = false), name = "messageChar")
    private int onStringRendered_indexIncrease(int index) {
        if (increaseIndex) {
            increaseIndex = false;
            return index + 6;
        }
        return index;
    }
}
