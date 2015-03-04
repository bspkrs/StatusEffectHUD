package bspkrs.statuseffecthud.fml;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SEHGameTicker
{
    private Minecraft      mc;
    private static boolean isRegistered = false;

    public SEHGameTicker()
    {
        isRegistered = true;
        mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;

        boolean keepTicking = !(mc != null && mc.thePlayer != null && mc.theWorld != null);

        if (!keepTicking && isRegistered)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && StatusEffectHUDMod.instance.versionChecker != null)
                if (!StatusEffectHUDMod.instance.versionChecker.isCurrentVersion())
                    for (String msg : StatusEffectHUDMod.instance.versionChecker.getInGameMessage())
                        mc.thePlayer.addChatMessage(new ChatComponentText(msg));

            FMLCommonHandler.instance().bus().unregister(this);
            isRegistered = false;
        }
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
