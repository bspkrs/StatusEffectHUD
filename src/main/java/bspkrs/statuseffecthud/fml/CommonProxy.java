package bspkrs.statuseffecthud.fml;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {}

    public void init(FMLInitializationEvent event)
    {
        FMLLog.log("StatusEffectHUD", Level.ERROR, "************************************************************************************");
        FMLLog.log("StatusEffectHUD", Level.ERROR, "* StatusEffectHUD is a CLIENT-ONLY mod. Installing it on your server is pointless. *");
        FMLLog.log("StatusEffectHUD", Level.ERROR, "************************************************************************************");
    }
}
