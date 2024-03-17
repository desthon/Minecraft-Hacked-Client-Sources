/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  org.apache.commons.lang3.exception.ExceptionUtils
 */
package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandMessageRaw
extends CommandBase {
    private static final String __OBFID = "CL_00000667";

    @Override
    public String getCommandName() {
        return "tellraw";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.tellraw.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        }
        EntityPlayerMP var3 = CommandMessageRaw.getPlayer(sender, args[0]);
        String var4 = CommandMessageRaw.func_180529_a(args, 1);
        try {
            IChatComponent var5 = IChatComponent.Serializer.jsonToComponent(var4);
            var3.addChatMessage(ChatComponentProcessor.func_179985_a(sender, var5, var3));
        }
        catch (JsonParseException var7) {
            Throwable var6 = ExceptionUtils.getRootCause((Throwable)var7);
            throw new SyntaxErrorException("commands.tellraw.jsonException", var6 == null ? "" : var6.getMessage());
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandMessageRaw.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

