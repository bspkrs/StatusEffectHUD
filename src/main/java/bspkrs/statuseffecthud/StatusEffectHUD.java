package bspkrs.statuseffecthud;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

import org.lwjgl.opengl.GL11;

import bspkrs.client.util.HUDUtils;
import bspkrs.statuseffecthud.fml.StatusEffectHUDMod;
import bspkrs.util.BSConfiguration;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;

public class StatusEffectHUD
{
    public static final String                VERSION_NUMBER       = "1.21(" + Const.MCVERSION + ")";
    
    protected static float                    zLevel               = -150.0F;
    private static ScaledResolution           scaledResolution;
    
    // Config fields
    public static String                      alignMode            = "middleright";
    // @BSProp(info="Valid list mode strings are horizontal and vertical")
    // public static String listMode = "vertical";
    public static boolean                     enableBackground     = false;
    public static boolean                     enableEffectName     = true;
    public static boolean                     enableIconBlink      = true;
    public static int                         durationBlinkSeconds = 10;
    public static String                      effectNameColor      = "f";
    public static String                      durationColor        = "f";
    public static int                         xOffset              = 2;
    public static int                         yOffset              = 2;
    public static int                         yOffsetBottomCenter  = 41;
    public static boolean                     applyXOffsetToCenter = false;
    public static boolean                     applyYOffsetToMiddle = false;
    public static boolean                     showInChat           = true;
    
    private static Map<PotionEffect, Integer> potionMaxDurationMap = new HashMap<PotionEffect, Integer>();
    private static BSConfiguration            config;
    
    public static void loadConfig(File file)
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }
        
        config = new BSConfiguration(file);
        
        config.load();
        
        alignMode = config.getString("alignMode", ctgyGen, alignMode,
                "Valid alignment strings are topleft, topcenter, topright, middleleft, middlecenter, middleright, bottomleft, bottomcenter, bottomright");
        enableBackground = config.getBoolean("enableBackground", ctgyGen, enableBackground,
                "Set to true to see the effect background box, false to disable.");
        enableEffectName = config.getBoolean("enableEffectName", ctgyGen, enableEffectName,
                "Set to true to show effect names, false to disable.");
        enableIconBlink = config.getBoolean("enableIconBlink", ctgyGen, enableIconBlink,
                "Set to true to enable blinking for the icon when a potion/effect is nearly gone, false to disable.");
        durationBlinkSeconds = config.getInt("durationBlinkSeconds", ctgyGen, durationBlinkSeconds, -1, 60,
                "When a potion/effect has this many seconds remaining the timer will begin to blink. Set to -1 to disable blinking.");
        effectNameColor = config.getString("effectNameColor", ctgyGen, effectNameColor,
                "Valid color values are 0-9, a-f (color values can be found here: http://www.minecraftwiki.net/wiki/File:Colors.png).");
        durationColor = config.getString("durationColor", ctgyGen, durationColor,
                "Valid color values are 0-9, a-f (color values can be found here: http://www.minecraftwiki.net/wiki/File:Colors.png).");
        xOffset = config.getInt("xOffset", ctgyGen, xOffset, Integer.MIN_VALUE, Integer.MAX_VALUE,
                "Horizontal offset from the edge of the screen (when using right alignments the x offset is relative to the right edge of the screen)");
        yOffset = config.getInt("yOffset", ctgyGen, yOffset, Integer.MIN_VALUE, Integer.MAX_VALUE,
                "Vertical offset from the edge of the screen (when using bottom alignments the y offset is relative to the bottom edge of the screen)");
        yOffsetBottomCenter = config.getInt("yOffsetBottomCenter", ctgyGen, yOffsetBottomCenter, 0, Integer.MAX_VALUE,
                "Vertical offset used only for the bottomcenter alignment to avoid the vanilla HUD");
        applyXOffsetToCenter = config.getBoolean("applyXOffsetToCenter", ctgyGen, applyXOffsetToCenter,
                "Set to true if you want the xOffset value to be applied when using a center alignment");
        applyYOffsetToMiddle = config.getBoolean("applyYOffsetToMiddle", ctgyGen, applyYOffsetToMiddle,
                "Set to true if you want the yOffset value to be applied when using a middle alignment");
        showInChat = config.getBoolean("showInChat", ctgyGen, showInChat,
                "Set to true to show info when chat is open, false to disable info when chat is open");
        
        config.save();
    }
    
    public static boolean onTickInGame(Minecraft mc)
    {
        if (StatusEffectHUDMod.instance.isEnabled() && (mc.inGameHasFocus || mc.currentScreen == null || (mc.currentScreen instanceof GuiChat && showInChat)) &&
                !mc.gameSettings.showDebugInfo && !mc.gameSettings.keyBindPlayerList.isPressed())
        {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            displayStatusEffects(mc);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        
        return true;
    }
    
    private static int getX(int width)
    {
        if (alignMode.equalsIgnoreCase("topcenter") || alignMode.equalsIgnoreCase("middlecenter") || alignMode.equalsIgnoreCase("bottomcenter"))
            return scaledResolution.getScaledWidth() / 2 - width / 2 + (applyXOffsetToCenter ? xOffset : 0);
        else if (alignMode.equalsIgnoreCase("topright") || alignMode.equalsIgnoreCase("middleright") || alignMode.equalsIgnoreCase("bottomright"))
            return scaledResolution.getScaledWidth() - width - xOffset;
        else
            return xOffset;
    }
    
    private static int getY(int rowCount, int height)
    {
        if (alignMode.equalsIgnoreCase("middleleft") || alignMode.equalsIgnoreCase("middlecenter") || alignMode.equalsIgnoreCase("middleright"))
            return (scaledResolution.getScaledHeight() / 2) - ((rowCount * height) / 2) + (applyYOffsetToMiddle ? yOffset : 0);
        else if (alignMode.equalsIgnoreCase("bottomleft") || alignMode.equalsIgnoreCase("bottomright"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffset;
        else if (alignMode.equalsIgnoreCase("bottomcenter"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffsetBottomCenter;
        else
            return yOffset;
    }
    
    private static boolean shouldRender(PotionEffect pe, int ticksLeft, int thresholdSeconds)
    {
        if (potionMaxDurationMap.get(pe).intValue() > 400)
            if (ticksLeft / 20 <= thresholdSeconds)
                return ticksLeft % 20 < 10;
        
        return true;
    }
    
    private static void displayStatusEffects(Minecraft mc)
    {
        Collection<?> activeEffects = mc.thePlayer.getActivePotionEffects();
        
        if (!activeEffects.isEmpty())
        {
            int yOffset = enableBackground ? 33 : enableEffectName ? 20 : 18;
            if (activeEffects.size() > 5 && enableBackground)
                yOffset = 132 / (activeEffects.size() - 1);
            
            int yBase = getY(activeEffects.size(), yOffset);
            
            for (Iterator<?> iteratorPotionEffect = activeEffects.iterator(); iteratorPotionEffect.hasNext(); yBase += yOffset)
            {
                PotionEffect potionEffect = (PotionEffect) iteratorPotionEffect.next();
                
                // If we find a newly added potionEffect, add it and the current duration to the map to keep track of the max duration
                if (!potionMaxDurationMap.containsKey(potionEffect) || potionMaxDurationMap.get(potionEffect).intValue() < potionEffect.getDuration())
                    potionMaxDurationMap.put(potionEffect, new Integer(potionEffect.getDuration()));
                
                Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                int xBase = getX(enableBackground ? 120 : 18 + 4 + mc.fontRenderer.getStringWidth("0:00"));
                String potionName = "";
                
                if (enableEffectName)
                {
                    potionName = StatCollector.translateToLocal(potion.getName());
                    
                    if (potionEffect.getAmplifier() == 1)
                    {
                        potionName = potionName + " II";
                    }
                    else if (potionEffect.getAmplifier() == 2)
                    {
                        potionName = potionName + " III";
                    }
                    else if (potionEffect.getAmplifier() == 3)
                    {
                        potionName = potionName + " IV";
                    }
                    
                    xBase = getX(enableBackground ? 120 : 18 + 4 + mc.fontRenderer.getStringWidth(potionName));
                }
                
                String effectDuration = Potion.getDurationString(potionEffect);
                
                if (enableBackground)
                    HUDUtils.drawTexturedModalRect(xBase, yBase, 0, 166, 140, 32, zLevel);
                
                if (alignMode.toLowerCase().contains("right"))
                {
                    xBase = getX(0);
                    if (potion.hasStatusIcon())
                    {
                        int potionStatusIcon = potion.getStatusIconIndex();
                        
                        if (!enableIconBlink || (enableIconBlink && shouldRender(potionEffect, potionEffect.getDuration(), durationBlinkSeconds)))
                            HUDUtils.drawTexturedModalRect(xBase + (enableBackground ? -24 : -18), yBase + (enableBackground ? 7 : 0), 0 + potionStatusIcon % 8 * 18, 166 + 32 + potionStatusIcon / 8 * 18, 18, 18, zLevel);
                    }
                    int stringWidth = mc.fontRenderer.getStringWidth(potionName);
                    mc.fontRenderer.drawStringWithShadow("\247" + effectNameColor + potionName + "\247r", xBase + (enableBackground ? -10 : -4) - 18 - stringWidth, yBase + (enableBackground ? 6 : 0), 0xffffff);
                    stringWidth = mc.fontRenderer.getStringWidth(effectDuration);
                    
                    if (shouldRender(potionEffect, potionEffect.getDuration(), durationBlinkSeconds))
                        mc.fontRenderer.drawStringWithShadow("\247" + durationColor + effectDuration + "\247r", xBase + (enableBackground ? -10 : -4) - 18 - stringWidth, yBase + (enableBackground ? 6 : 0) + (enableEffectName ? 10 : 5), 0xffffff);
                }
                else
                {
                    if (potion.hasStatusIcon())
                    {
                        int potionStatusIcon = potion.getStatusIconIndex();
                        HUDUtils.drawTexturedModalRect(xBase + (enableBackground ? 6 : 0), yBase + (enableBackground ? 7 : 0), 0 + potionStatusIcon % 8 * 18, 166 + 32 + potionStatusIcon / 8 * 18, 18, 18, zLevel);
                    }
                    mc.fontRenderer.drawStringWithShadow("\247" + effectNameColor + potionName + "\247r", xBase + (enableBackground ? 10 : 4) + 18, yBase + (enableBackground ? 6 : 0), 0xffffff);
                    
                    if (shouldRender(potionEffect, potionEffect.getDuration(), durationBlinkSeconds))
                        mc.fontRenderer.drawStringWithShadow("\247" + durationColor + effectDuration + "\247r", xBase + (enableBackground ? 10 : 4) + 18, yBase + (enableBackground ? 6 : 0) + (enableEffectName ? 10 : 5), 0xffffff);
                }
            }
            
            // See if any potions have expired... if they have, remove them from the map
            List<PotionEffect> toRemove = new LinkedList<PotionEffect>();
            
            for (PotionEffect pe : potionMaxDurationMap.keySet())
                if (!activeEffects.contains(pe))
                    toRemove.add(pe);
            
            for (PotionEffect pe : toRemove)
                potionMaxDurationMap.remove(pe);
        }
    }
}
