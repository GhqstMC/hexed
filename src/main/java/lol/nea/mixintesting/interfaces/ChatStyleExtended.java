package lol.nea.mixintesting.interfaces;


import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public interface ChatStyleExtended {
    int chromaStyle = 0;
    String hexColor = "";
    int getChromaStyle();
    ChatStyleExtended setChromaStyle(int chromaStyle);
    String getHexColor();
    ChatStyleExtended setHexColor(String hexColor);

    ChatStyle getRawParentStyle();
    EnumChatFormatting getRawColor();
    Boolean getRawBold();
    Boolean getRawItalic();
    Boolean getRawUnderlined();
    Boolean getRawStrikethrough();
    Boolean getRawObfuscated();
    ClickEvent getRawChatClickEvent();
    HoverEvent getRawChatHoverEvent();
    String getRawInsertion();
    String getRawHexColor();
    int getRawChromaStyle();
}
