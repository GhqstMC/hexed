package lol.nea.mixintesting.mixin;

import lol.nea.mixintesting.interfaces.ChatStyleExtended;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ChatStyle.class)
public abstract class MixinChatStyle implements ChatStyleExtended {

    private int chromaStyle = 0;
    private String hexColor = "";

    @Shadow private ChatStyle parentStyle;
    @Shadow private EnumChatFormatting color;
    @Shadow private Boolean bold;
    @Shadow private Boolean italic;
    @Shadow private Boolean underlined;
    @Shadow private Boolean strikethrough;
    @Shadow private Boolean obfuscated;
    @Shadow private ClickEvent chatClickEvent;
    @Shadow private HoverEvent chatHoverEvent;
    @Shadow private String insertion;

    @Override
    public ChatStyle getRawParentStyle() {
        return this.parentStyle;
    }

    @Override
    public EnumChatFormatting getRawColor() {
        return this.color;
    }

    @Override
    public Boolean getRawBold() {
        return this.bold;
    }

    @Override
    public Boolean getRawItalic() {
        return this.italic;
    }

    @Override
    public Boolean getRawUnderlined() {
        return this.underlined;
    }

    @Override
    public Boolean getRawStrikethrough() {
        return this.strikethrough;
    }

    @Override
    public Boolean getRawObfuscated() {
        return this.obfuscated;
    }

    @Override
    public ClickEvent getRawChatClickEvent() {
        return this.chatClickEvent;
    }

    @Override
    public HoverEvent getRawChatHoverEvent() {
        return this.chatHoverEvent;
    }

    @Override
    public String getRawInsertion() {
        return this.insertion;
    }

    @Override
    public String getRawHexColor() {
        return ((ChatStyleExtended) this).hexColor;
    }

    @Override
    public int getRawChromaStyle() {
        return ((ChatStyleExtended) this).chromaStyle;
    }

    @Shadow
    protected abstract ChatStyle getParent();

    public int getChromaStyle() {
        return this.chromaStyle == 0 ? ((MixinChatStyle)(Object)this.getParent()).getChromaStyle() : this.chromaStyle;
    }

    public ChatStyleExtended setChromaStyle(int chromaStyle) {
        this.chromaStyle = chromaStyle;
        return this;
    }

    public String getHexColor() {
        return Objects.equals(this.hexColor, "") ? ((MixinChatStyle)(Object)this.getParent()).getHexColor() : this.hexColor;
    }

    public ChatStyleExtended setHexColor(String hexColor) {
        this.hexColor = hexColor;
        return this;
    }

    /**
     * @author nea
     * @reason more stuff
     */
    @Overwrite
    public ChatStyle createShallowCopy() {
        return (ChatStyle) ((ChatStyleExtended) new ChatStyle()
                .setBold(this.bold)
                .setItalic(this.italic)
                .setStrikethrough(this.strikethrough)
                .setUnderlined(this.underlined)
                .setObfuscated(this.obfuscated)
                .setColor(this.color)
                .setChatClickEvent(this.chatClickEvent)
                .setChatHoverEvent(this.chatHoverEvent)
                .setParentStyle(this.parentStyle)
                .setInsertion(this.insertion))
                .setChromaStyle(this.chromaStyle)
                .setHexColor(this.hexColor);
    }

    /**
     * @author nea
     * @reason more stuff
     */
    @Overwrite
    public ChatStyle createDeepCopy() {
        return (ChatStyle) ((ChatStyleExtended) new ChatStyle()
                .setBold(this.bold)
                .setItalic(this.italic)
                .setStrikethrough(this.strikethrough)
                .setUnderlined(this.underlined)
                .setObfuscated(this.obfuscated)
                .setColor(this.color)
                .setChatClickEvent(this.chatClickEvent)
                .setChatHoverEvent(this.chatHoverEvent)
                .setInsertion(this.insertion))
                .setChromaStyle(this.chromaStyle)
                .setHexColor(this.hexColor);
    }

    @Inject(method="getFormattingCode", at = @At("RETURN"), cancellable = true)
    public void getFormattingCode(CallbackInfoReturnable<String> cir) {
        String str = cir.getReturnValue();
        if (this.chromaStyle == 1) {
            str = str + "§w";
        }
        if (!Objects.equals(this.hexColor, "")) {
            str = str + "§x" + this.hexColor;
        }
        cir.setReturnValue(str);
    }

}
