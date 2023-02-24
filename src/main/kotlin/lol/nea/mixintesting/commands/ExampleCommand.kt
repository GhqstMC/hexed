package lol.nea.mixintesting.commands

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText

class ExampleCommand : CommandBase() {
    override fun getCommandName() = "mixintesting"

    override fun getCommandAliases() = listOf("example")

    override fun getCommandUsage(sender: ICommandSender?) = "/$commandName"

    override fun getRequiredPermissionLevel() = 0

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        sender?.addChatMessage(ChatComponentText("Example command run!"))
    }
}