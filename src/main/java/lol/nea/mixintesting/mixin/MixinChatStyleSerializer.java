package lol.nea.mixintesting.mixin;

import com.google.gson.*;
import lol.nea.mixintesting.interfaces.ChatStyleExtended;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.lang.reflect.Type;

@Mixin(ChatStyle.Serializer.class)
public class MixinChatStyleSerializer {

    public ChatStyle deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject jsonobject2;
            JsonObject jsonobject1;
            ChatStyle chatstyle = new ChatStyle();
            JsonObject obj = json.getAsJsonObject();
            if (obj == null) {
                return null;
            }
            if (obj.has("bold")) {
                chatstyle.setBold(obj.get("bold").getAsBoolean());
            }
            if (obj.has("italic")) {
                chatstyle.setItalic(obj.get("italic").getAsBoolean());
            }
            if (obj.has("underlined")) {
                chatstyle.setUnderlined(obj.get("underlined").getAsBoolean());
            }
            if (obj.has("strikethrough")) {
                chatstyle.setStrikethrough(obj.get("strikethrough").getAsBoolean());
            }
            if (obj.has("obfuscated")) {
                chatstyle.setObfuscated(obj.get("obfuscated").getAsBoolean());
            }
            if (obj.has("color")) {
                chatstyle.setColor((EnumChatFormatting) ((Object) context.deserialize(obj.get("color"), (Type) ((Object) EnumChatFormatting.class))));
            }
            if (obj.has("insertion")) {
                chatstyle.setInsertion(obj.get("insertion").getAsString());
            }
            if (obj.has("hex")) {
                ((ChatStyleExtended) chatstyle).setHexColor(obj.get("hex").getAsString());
            }
            if (obj.has("chroma")) {
                ((ChatStyleExtended) chatstyle).setChromaStyle(obj.get("chroma").getAsInt());
            }
            if (obj.has("clickEvent") && (jsonobject1 = obj.getAsJsonObject("clickEvent")) != null) {
                String s;
                JsonPrimitive jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
                ClickEvent.Action clickevent$action = jsonprimitive == null ? null : ClickEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
                JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
                String string = s = jsonprimitive1 == null ? null : jsonprimitive1.getAsString();
                if (clickevent$action != null && s != null && clickevent$action.shouldAllowInChat()) {
                    chatstyle.setChatClickEvent(new ClickEvent(clickevent$action, s));
                }
            }
            if (obj.has("hoverEvent") && (jsonobject2 = obj.getAsJsonObject("hoverEvent")) != null) {
                JsonPrimitive jsonprimitive2 = jsonobject2.getAsJsonPrimitive("action");
                HoverEvent.Action hoverevent$action = jsonprimitive2 == null ? null : HoverEvent.Action.getValueByCanonicalName(jsonprimitive2.getAsString());
                IChatComponent ichatcomponent = (IChatComponent)context.deserialize(jsonobject2.get("value"), (Type)((Object)IChatComponent.class));
                if (hoverevent$action != null && ichatcomponent != null && hoverevent$action.shouldAllowInChat()) {
                    chatstyle.setChatHoverEvent(new HoverEvent(hoverevent$action, ichatcomponent));
                }
            }
            return chatstyle;
        }
        return null;
    }

    /**
     * @author nea
     * @reason more stuff
     */
    @Overwrite
    public JsonElement serialize(ChatStyle style, Type type, JsonSerializationContext context) {
        if (style.isEmpty()) {
            return null;
        }
        JsonObject jsonobject = new JsonObject();
        if (((ChatStyleExtended) style).getRawBold() != null) {
            jsonobject.addProperty("bold", ((ChatStyleExtended) style).getRawBold());
        }
        if (((ChatStyleExtended) style).getRawItalic() != null) {
            jsonobject.addProperty("italic", ((ChatStyleExtended) style).getRawItalic());
        }
        if (((ChatStyleExtended) style).getRawUnderlined() != null) {
            jsonobject.addProperty("underlined", ((ChatStyleExtended) style).getRawUnderlined());
        }
        if (((ChatStyleExtended) style).getRawStrikethrough() != null) {
            jsonobject.addProperty("strikethrough", ((ChatStyleExtended) style).getRawStrikethrough());
        }
        if (((ChatStyleExtended) style).getRawObfuscated() != null) {
            jsonobject.addProperty("obfuscated", ((ChatStyleExtended) style).getRawObfuscated());
        }
        if (((ChatStyleExtended) style).getRawColor() != null) {
            jsonobject.add("color", context.serialize((Object)((ChatStyleExtended) style).getRawColor()));
        }
        if (((ChatStyleExtended) style).getRawInsertion() != null) {
            jsonobject.add("insertion", context.serialize(((ChatStyleExtended) style).getRawInsertion()));
        }
        if (!((ChatStyleExtended) style).getRawHexColor().equals("")) {
            jsonobject.addProperty("hex", ((ChatStyleExtended) style).getRawHexColor());
        }
        if (((ChatStyleExtended) style).getRawChromaStyle() != 0) {
            jsonobject.addProperty("chroma", ((ChatStyleExtended) style).getRawChromaStyle());
        }
        if (((ChatStyleExtended) style).getRawChatClickEvent() != null) {
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("action", ((ChatStyleExtended) style).getRawChatClickEvent().getAction().getCanonicalName());
            jsonobject1.addProperty("value", ((ChatStyleExtended) style).getRawChatClickEvent().getValue());
            jsonobject.add("clickEvent", jsonobject1);
        }
        if (((ChatStyleExtended) style).getRawChatHoverEvent() != null) {
            JsonObject jsonobject2 = new JsonObject();
            jsonobject2.addProperty("action", ((ChatStyleExtended) style).getRawChatHoverEvent().getAction().getCanonicalName());
            jsonobject2.add("value", context.serialize(((ChatStyleExtended) style).getRawChatHoverEvent().getValue()));
            jsonobject.add("hoverEvent", jsonobject2);
        }
        return jsonobject;
    }
}
