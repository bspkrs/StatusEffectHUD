package bspkrs.statuseffecthud.fml;

import net.minecraft.client.Minecraft;
import bspkrs.statuseffecthud.StatusEffectHUD;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SEHRenderTicker
{
    private Minecraft      mc;
    private static boolean isRegistered = false;
    
    public SEHRenderTicker()
    {
        mc = Minecraft.getMinecraft();
        isRegistered = true;
    }
    
    @SubscribeEvent
    public void onTick(RenderTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;
        
        if (!StatusEffectHUD.onTickInGame(mc))
        {
            FMLCommonHandler.instance().bus().unregister(this);
            isRegistered = false;
        }
    }
    
    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
