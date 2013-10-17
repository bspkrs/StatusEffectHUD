package bspkrs.statuseffecthud.fml;

import java.util.EnumSet;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.statuseffecthud.StatusEffectHUD;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "StatusEffectHUD", name = "StatusEffectHUD", version = StatusEffectHUD.VERSION_NUMBER, dependencies = "required-after:bspkrsCore", useMetadata = true)
public class StatusEffectHUDMod
{
    protected ModVersionChecker      versionChecker;
    private final String             versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/statusEffectHUD.version";
    private final String             mcfTopic   = "http://www.minecraftforum.net/topic/1114612-";
    
    @Metadata(value = "StatusEffectHUD")
    public static ModMetadata        metadata;
    
    @Instance(value = "StatusEffectHUD")
    public static StatusEffectHUDMod instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        StatusEffectHUD.loadConfig(event.getSuggestedConfigurationFile());
        
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        TickRegistry.registerTickHandler(new SEHGameTicker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
        TickRegistry.registerTickHandler(new SEHRenderTicker(EnumSet.of(TickType.RENDER)), Side.CLIENT);
    }
}
