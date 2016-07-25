package bspkrs.statuseffecthud;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import bspkrs.fml.util.DelayedGuiDisplayTicker;
import bspkrs.statuseffecthud.fml.gui.GuiSEHConfig;

public class CommandStatusEffect extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "statuseffect";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "commands.statuseffect.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        try
        {
            new DelayedGuiDisplayTicker(10, new GuiSEHConfig(null));
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
}
