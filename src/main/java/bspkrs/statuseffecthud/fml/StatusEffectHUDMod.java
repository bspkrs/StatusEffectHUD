package bspkrs.statuseffecthud.fml;

import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)",
        useMetadata = true, guiFactory = Reference.GUI_FACTORY)
public class StatusEffectHUDMod
{
    protected ModVersionChecker      versionChecker;
    protected final String           versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/statusEffectHUD.version";
    protected final String           mcfTopic   = "http://www.minecraftforum.net/topic/1114612-";

    @Metadata(value = Reference.MODID)
    public static ModMetadata        metadata;

    @Instance(value = Reference.MODID)
    public static StatusEffectHUDMod instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy        proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }
}
