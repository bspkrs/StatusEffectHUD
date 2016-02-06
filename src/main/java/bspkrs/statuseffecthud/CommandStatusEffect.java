package bspkrs.statuseffecthud;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
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
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 1;
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2)
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
