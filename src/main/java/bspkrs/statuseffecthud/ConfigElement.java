package bspkrs.statuseffecthud;

import static bspkrs.util.config.Property.Type.BOOLEAN;
import static bspkrs.util.config.Property.Type.COLOR;
import static bspkrs.util.config.Property.Type.INTEGER;
import static bspkrs.util.config.Property.Type.STRING;
import bspkrs.util.config.Property.Type;

public enum ConfigElement
{
    ENABLED("enabled", "bspkrs.seh.configgui.enabled", "Enables or disables the Status Effect HUD display.", BOOLEAN),
    ALIGN_MODE("alignMode", "bspkrs.seh.configgui.alignMode",
            "Sets the position of the HUD on the screen. Valid alignment strings are topleft, topcenter, topright, middleleft, middlecenter, middleright, bottomleft, bottomcenter, bottomright.",
            STRING, new String[] { "topleft", "topcenter", "topright", "middleleft", "middlecenter", "middleright", "bottomleft", "bottomcenter", "bottomright" }),
    //    LIST_MODE("listMode", "bspkrs.seh.configgui.listMode",
    //            "Sets the direction to display status items. Valid list mode strings are horizontal and vertical.", STRING, new String[] { "vertical", "horizontal" }),
    DISABLE_INV_EFFECT_LIST("disableInventoryEffectList", "bspkrs.seh.configgui.disableInventoryEffectList",
            "Set to true to disable the vanilla status effect list shown when your inventory is open, false to allow vanilla inventory behavior.", BOOLEAN),
    ENABLE_BACKGROUND("enableBackground", "bspkrs.seh.configgui.enableBackground",
            "Set to true to see the effect background box, false to disable.", BOOLEAN),
    ENABLE_EFFECT_NAME("enableEffectName", "bspkrs.seh.configgui.enableEffectName",
            "Set to true to show effect names, false to disable.", BOOLEAN),
    DURATION_BLINK_SECONDS("durationBlinkSeconds", "bspkrs.seh.configgui.durationBlinkSeconds",
            "When a potion/effect has this many seconds remaining the timer will begin to blink. Set to -1 to disable blinking.", INTEGER),
    ENABLE_ICON_BLINK("enableIconBlink", "bspkrs.seh.configgui.enableIconBlink",
            "Set to true to enable blinking for the icon when a potion/effect is nearly gone, false to disable.", BOOLEAN),
    EFFECT_NAME_COLOR("effectNameColor", "bspkrs.seh.configgui.effectNameColor",
            "Valid color values are 0-9, a-f (color values can be found here: http://www.minecraftwiki.net/wiki/File:Colors.png).", COLOR,
            new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" }),
    DURATION_COLOR("durationColor", "bspkrs.seh.configgui.durationColor",
            "Valid color values are 0-9, a-f (color values can be found here: http://www.minecraftwiki.net/wiki/File:Colors.png).", COLOR,
            new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" }),
    X_OFFSET("xOffset", "bspkrs.seh.configgui.xOffset",
            "Horizontal offset from the edge of the screen (when using right alignments the x offset is relative to the right edge of the screen).", INTEGER),
    Y_OFFSET("yOffset", "bspkrs.seh.configgui.yOffset",
            "Vertical offset from the edge of the screen (when using bottom alignments the y offset is relative to the bottom edge of the screen).", INTEGER),
    Y_OFFSET_BOTTOM_CENTER("yOffsetBottomCenter", "bspkrs.seh.configgui.yOffsetBottomCenter",
            "Vertical offset used only for the bottomcenter alignment to avoid the vanilla HUD.", INTEGER),
    APPLY_X_OFFSET_TO_CENTER("applyXOffsetToCenter", "bspkrs.seh.configgui.applyXOffsetToCenter",
            "Set to true if you want the xOffset value to be applied when using a center alignment.", BOOLEAN),
    APPLY_Y_OFFSET_TO_MIDDLE("applyYOffsetToMiddle", "bspkrs.seh.configgui.applyYOffsetToMiddle",
            "Set to true if you want the yOffset value to be applied when using a middle alignment.", BOOLEAN),
    SHOW_IN_CHAT("showInChat", "bspkrs.seh.configgui.showInChat",
            "Set to true to show info when chat is open, false to disable info when chat is open.", BOOLEAN);
    
    private String   key;
    private String   langKey;
    private String   desc;
    private Type     propertyType;
    private String[] validStrings;
    
    private ConfigElement(String key, String langKey, String desc, Type propertyType, String[] validStrings)
    {
        this.key = key;
        this.langKey = langKey;
        this.desc = desc;
        this.propertyType = propertyType;
        this.validStrings = validStrings;
    }
    
    private ConfigElement(String key, String langKey, String desc, Type propertyType)
    {
        this(key, langKey, desc, propertyType, new String[0]);
    }
    
    public String key()
    {
        return key;
    }
    
    public String languageKey()
    {
        return langKey;
    }
    
    public String desc()
    {
        return desc;
    }
    
    public Type propertyType()
    {
        return propertyType;
    }
    
    public String[] validStrings()
    {
        return validStrings;
    }
}