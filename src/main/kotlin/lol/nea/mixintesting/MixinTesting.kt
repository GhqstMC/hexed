package lol.nea.mixintesting

import lol.nea.mixintesting.commands.ExampleCommand
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ModMetadata
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.awt.Color

@Mod(
    modid = "mixintesting",
    name = "MixinTesting",
    version = "1.0",
    useMetadata = true,
    clientSideOnly = true
)
class MixinTesting {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        metadata = event.modMetadata
        System.setProperty("devauth.enabled", "true")
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand(ExampleCommand())

        listOf(
            this
        ).forEach(MinecraftForge.EVENT_BUS::register)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || currentGui == null) return
        mc.displayGuiScreen(currentGui)
        currentGui = null
    }

    companion object {
        val mc: Minecraft = Minecraft.getMinecraft()
        var currentGui: GuiScreen? = null

        lateinit var metadata: ModMetadata

        fun getRGBA(r: Int, g: Int, b: Int, a: Int): Int {
            return a and 0xFF shl 24 or
                    (r and 0xFF shl 16) or
                    (g and 0xFF shl 8) or
                    (b and 0xFF)
        }

        fun getRainbowColor(index: Int): Color {
            return Color.getHSBColor(((System.currentTimeMillis() - index * 100L) % 5000).toFloat() / 5000, 0.5f, 1f)
        }
    }
}