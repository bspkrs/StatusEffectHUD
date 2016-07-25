package bspkrs.statuseffecthud.fml;

import bspkrs.statuseffecthud.CommandStatusEffect;
import bspkrs.statuseffecthud.StatusEffectHUD;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        StatusEffectHUD.initConfig(event.getSuggestedConfigurationFile());
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new SEHRenderTicker());

        ClientCommandHandler.instance.registerCommand(new CommandStatusEffect());

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Reference.MODID))
        {
            Reference.config.save();
            StatusEffectHUD.syncConfig();
        }
    }
}
