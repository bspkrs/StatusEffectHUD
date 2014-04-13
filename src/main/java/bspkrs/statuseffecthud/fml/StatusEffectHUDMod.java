package bspkrs.statuseffecthud.fml;

import net.minecraftforge.client.ClientCommandHandler;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.statuseffecthud.CommandStatusEffect;
import bspkrs.statuseffecthud.StatusEffectHUD;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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
        FMLCommonHandler.instance().bus().register(new SEHGameTicker());
        FMLCommonHandler.instance().bus().register(new SEHRenderTicker());
        
        if (event.getSide().isClient())
        {
            ClientCommandHandler.instance.registerCommand(new CommandStatusEffect());
        }
    }
}
