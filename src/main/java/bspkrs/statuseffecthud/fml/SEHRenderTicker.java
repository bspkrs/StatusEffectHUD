package bspkrs.statuseffecthud.fml;

import bspkrs.statuseffecthud.StatusEffectHUD;
import bspkrs.util.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

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

    @SuppressWarnings("rawtypes")
    @SubscribeEvent
    public void onTick(RenderTickEvent event)
    {
        if (event.phase.equals(Phase.START))
        {
            if (StatusEffectHUD.disableInventoryEffectList)
                if (mc.currentScreen != null && mc.currentScreen instanceof InventoryEffectRenderer)
                {
                    try
                    {
                        InventoryEffectRenderer ier = ((InventoryEffectRenderer) mc.currentScreen);
                        if (ReflectionHelper.getBooleanValue(InventoryEffectRenderer.class, "field_147045_u", "hasActivePotionEffects", ier, false))
                        {
                            ReflectionHelper.setBooleanValue(InventoryEffectRenderer.class, "field_147045_u", "hasActivePotionEffects", ier, false);
                            ReflectionHelper.setIntValue(GuiContainer.class, "field_147003_i", "guiLeft", ier,
                                    (ier.width - ReflectionHelper.getIntValue(GuiContainer.class, "field_146999_f", "xSize", ier, 176)) / 2);
                        }

                        List buttonList = ReflectionHelper.getListObject(GuiScreen.class, "field_146292_n", "buttonList", ier);
                        for (Object o : buttonList)
                            if (o instanceof GuiButton && ((GuiButton) o).id == 101)
                                ((GuiButton) o).xPosition = ReflectionHelper.getIntValue(GuiContainer.class, "field_147003_i", "guiLeft", ier,
                                        (ier.width - ReflectionHelper.getIntValue(GuiContainer.class, "field_146999_f", "xSize", ier, 176)) / 2);
                            else if (o instanceof GuiButton && ((GuiButton) o).id == 102)
                                ((GuiButton) o).xPosition = ReflectionHelper.getIntValue(GuiContainer.class, "field_147003_i", "guiLeft", ier,
                                        (ier.width - ReflectionHelper.getIntValue(GuiContainer.class, "field_146999_f", "xSize", ier, 176)) / 2) +
                                        ReflectionHelper.getIntValue(GuiContainer.class, "field_146999_f", "xSize", ier, 176) - 20;
                    }
                    catch (Throwable e)
                    {}
                }

            return;
        }

        if (!StatusEffectHUD.onTickInGame(mc))
        {
            MinecraftForge.EVENT_BUS.unregister(this);
            isRegistered = false;
        }
    }

    @SubscribeEvent
    public void onPotionDisplay(RenderGameOverlayEvent.Pre event) {
    	if (event.getType() == ElementType.POTION_ICONS) event.setCanceled(true);
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
