package bspkrs.statuseffecthud;

import java.io.File;
import java.util.ArrayList;
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
import bspkrs.statuseffecthud.fml.Reference;
import bspkrs.util.CommonUtils;

public class StatusEffectHUD
{
    protected static float                    zLevel                            = -150.0F;
    private static ScaledResolution           scaledResolution;
    
    // Config fields
    private final static boolean              enabledDefault                    = true;
    public static boolean                     enabled                           = enabledDefault;
    private final static String               alignModeDefault                  = "middleright";
    public static String                      alignMode                         = alignModeDefault;
    // @BSProp(info="Valid list mode strings are horizontal and vertical")
    // public static String listMode = "vertical";
    private final static boolean              disableInventoryEffectListDefault = true;
    public static boolean                     disableInventoryEffectList        = disableInventoryEffectListDefault;
    private final static boolean              enableBackgroundDefault           = false;
    public static boolean                     enableBackground                  = enableBackgroundDefault;
    private final static boolean              enableEffectNameDefault           = true;
    public static boolean                     enableEffectName                  = enableEffectNameDefault;
    private final static boolean              enableIconBlinkDefault            = true;
    public static boolean                     enableIconBlink                   = enableIconBlinkDefault;
    private final static int                  durationBlinkSecondsDefault       = 10;
    public static int                         durationBlinkSeconds              = durationBlinkSecondsDefault;
    private final static String               effectNameColorDefault            = "f";
    public static String                      effectNameColor                   = effectNameColorDefault;
    private final static String               durationColorDefault              = "f";
    public static String                      durationColor                     = durationColorDefault;
    private final static int                  xOffsetDefault                    = 2;
    public static int                         xOffset                           = xOffsetDefault;
    private final static int                  yOffsetDefault                    = 2;
    public static int                         yOffset                           = yOffsetDefault;
    private final static int                  yOffsetBottomCenterDefault        = 41;
    public static int                         yOffsetBottomCenter               = yOffsetBottomCenterDefault;
    private final static boolean              applyXOffsetToCenterDefault       = false;
    public static boolean                     applyXOffsetToCenter              = applyXOffsetToCenterDefault;
    private final static boolean              applyYOffsetToMiddleDefault       = false;
    public static boolean                     applyYOffsetToMiddle              = applyYOffsetToMiddleDefault;
    private final static boolean              showInChatDefault                 = true;
    public static boolean                     showInChat                        = showInChatDefault;
    
    private static Map<PotionEffect, Integer> potionMaxDurationMap              = new HashMap<PotionEffect, Integer>();
    
    public static void initConfig(File file)
    {
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }
        
        Reference.config = new Configuration(file);
        
        syncConfig();
    }
    
    public static void syncConfig()
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
        Reference.config.load();
        
        Reference.config.setCategoryComment(ctgyGen, "ATTENTION: Editing this file manually is no longer necessary. \n" +
                "Type the command '/statuseffect config' without the quotes in-game to modify these settings.");
        
        List<String> orderedKeys = new ArrayList<String>(ConfigElement.values().length);
        
        enabled = Reference.config.getBoolean(ConfigElement.ENABLED.key(), ctgyGen, enabledDefault,
                ConfigElement.ENABLED.desc(), ConfigElement.ENABLED.languageKey());
        orderedKeys.add(ConfigElement.ENABLED.key());
        alignMode = Reference.config.getString(ConfigElement.ALIGN_MODE.key(), ctgyGen, alignModeDefault,
                ConfigElement.ALIGN_MODE.desc(), ConfigElement.ALIGN_MODE.validStrings(), ConfigElement.ALIGN_MODE.languageKey());
        orderedKeys.add(ConfigElement.ALIGN_MODE.key());
        disableInventoryEffectList = Reference.config.getBoolean(ConfigElement.DISABLE_INV_EFFECT_LIST.key(), ctgyGen, disableInventoryEffectListDefault,
                ConfigElement.DISABLE_INV_EFFECT_LIST.desc(), ConfigElement.DISABLE_INV_EFFECT_LIST.languageKey());
        orderedKeys.add(ConfigElement.DISABLE_INV_EFFECT_LIST.key());
        showInChat = Reference.config.getBoolean(ConfigElement.SHOW_IN_CHAT.key(), ctgyGen, showInChatDefault,
                ConfigElement.SHOW_IN_CHAT.desc(), ConfigElement.SHOW_IN_CHAT.languageKey());
        orderedKeys.add(ConfigElement.SHOW_IN_CHAT.key());
        enableBackground = Reference.config.getBoolean(ConfigElement.ENABLE_BACKGROUND.key(), ctgyGen, enableBackgroundDefault,
                ConfigElement.ENABLE_BACKGROUND.desc(), ConfigElement.ENABLE_BACKGROUND.languageKey());
        orderedKeys.add(ConfigElement.ENABLE_BACKGROUND.key());
        enableEffectName = Reference.config.getBoolean(ConfigElement.ENABLE_EFFECT_NAME.key(), ctgyGen, enableEffectNameDefault,
                ConfigElement.ENABLE_EFFECT_NAME.desc(), ConfigElement.ENABLE_EFFECT_NAME.languageKey());
        orderedKeys.add(ConfigElement.ENABLE_EFFECT_NAME.key());
        effectNameColor = Reference.config.getString(ConfigElement.EFFECT_NAME_COLOR.key(), ctgyGen, effectNameColorDefault,
                ConfigElement.EFFECT_NAME_COLOR.desc(), ConfigElement.EFFECT_NAME_COLOR.validStrings(), ConfigElement.EFFECT_NAME_COLOR.languageKey());
        orderedKeys.add(ConfigElement.EFFECT_NAME_COLOR.key());
        durationColor = Reference.config.getString(ConfigElement.DURATION_COLOR.key(), ctgyGen, durationColorDefault,
                ConfigElement.DURATION_COLOR.desc(), ConfigElement.DURATION_COLOR.validStrings(), ConfigElement.DURATION_COLOR.languageKey());
        orderedKeys.add(ConfigElement.DURATION_COLOR.key());
        enableIconBlink = Reference.config.getBoolean(ConfigElement.ENABLE_ICON_BLINK.key(), ctgyGen, enableIconBlinkDefault,
                ConfigElement.ENABLE_ICON_BLINK.desc(), ConfigElement.ENABLE_ICON_BLINK.languageKey());
        orderedKeys.add(ConfigElement.ENABLE_ICON_BLINK.key());
        durationBlinkSeconds = Reference.config.getInt(ConfigElement.DURATION_BLINK_SECONDS.key(), ctgyGen, durationBlinkSecondsDefault, -1, 60,
                ConfigElement.DURATION_BLINK_SECONDS.desc(), ConfigElement.DURATION_BLINK_SECONDS.languageKey());
        orderedKeys.add(ConfigElement.DURATION_BLINK_SECONDS.key());
        xOffset = Reference.config.getInt(ConfigElement.X_OFFSET.key(), ctgyGen, xOffsetDefault, Integer.MIN_VALUE, Integer.MAX_VALUE,
                ConfigElement.X_OFFSET.desc(), ConfigElement.X_OFFSET.languageKey());
        orderedKeys.add(ConfigElement.X_OFFSET.key());
        applyXOffsetToCenter = Reference.config.getBoolean(ConfigElement.APPLY_X_OFFSET_TO_CENTER.key(), ctgyGen, applyXOffsetToCenterDefault,
                ConfigElement.APPLY_X_OFFSET_TO_CENTER.desc(), ConfigElement.APPLY_X_OFFSET_TO_CENTER.languageKey());
        orderedKeys.add(ConfigElement.APPLY_X_OFFSET_TO_CENTER.key());
        yOffset = Reference.config.getInt(ConfigElement.Y_OFFSET.key(), ctgyGen, yOffsetDefault, Integer.MIN_VALUE, Integer.MAX_VALUE,
                ConfigElement.Y_OFFSET.desc(), ConfigElement.Y_OFFSET.languageKey());
        orderedKeys.add(ConfigElement.Y_OFFSET.key());
        applyYOffsetToMiddle = Reference.config.getBoolean(ConfigElement.APPLY_Y_OFFSET_TO_MIDDLE.key(), ctgyGen, applyYOffsetToMiddleDefault,
                ConfigElement.APPLY_Y_OFFSET_TO_MIDDLE.desc(), ConfigElement.APPLY_Y_OFFSET_TO_MIDDLE.languageKey());
        orderedKeys.add(ConfigElement.APPLY_Y_OFFSET_TO_MIDDLE.key());
        yOffsetBottomCenter = Reference.config.getInt(ConfigElement.Y_OFFSET_BOTTOM_CENTER.key(), ctgyGen, yOffsetBottomCenterDefault,
                Integer.MIN_VALUE, Integer.MAX_VALUE, ConfigElement.Y_OFFSET_BOTTOM_CENTER.desc(), ConfigElement.Y_OFFSET_BOTTOM_CENTER.languageKey());
        orderedKeys.add(ConfigElement.Y_OFFSET_BOTTOM_CENTER.key());
        
        Reference.config.setCategoryPropertyOrder(ctgyGen, orderedKeys);
        
        Reference.config.save();
        
    }
    
    public static boolean onTickInGame(Minecraft mc)
    {
        if (enabled && (mc.inGameHasFocus || mc.currentScreen == null || (mc.currentScreen instanceof GuiChat && showInChat)) &&
                !mc.gameSettings.showDebugInfo)
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
