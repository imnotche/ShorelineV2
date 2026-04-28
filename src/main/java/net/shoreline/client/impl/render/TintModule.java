package net.shoreline.client.impl.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Identifier;
import net.shoreline.client.ShorelineMod;
import net.shoreline.client.api.config.BooleanConfig;
import net.shoreline.client.api.config.ColorConfig;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.module.GuiCategory;
import net.shoreline.client.api.module.Toggleable;
import net.shoreline.client.impl.Managers;
import net.shoreline.client.impl.event.LoadingEvent;
import net.shoreline.client.impl.event.particle.TotemParticleEvent;
import net.shoreline.client.impl.event.render.*;
import net.shoreline.eventbus.annotation.EventListener;

import java.awt.*;

public class TintModule extends Toggleable
{
    private final Identifier shineTexture;

    Config<Boolean> lightConfig = new BooleanConfig.Builder("Light")
            .setDescription("Change the color of world light")
            .setDefaultValue(false).build();
    Config<Color> lightColor = new ColorConfig.Builder("LightColor")
            .setDescription("The color of world light")
            .setDefaultValue(Color.RED).setVisible(lightConfig::getValue).build();

    Config<Boolean> foliageConfig = new BooleanConfig.Builder("Foliage")
            .setDescription("Change the color of foliage")
            .setDefaultValue(false).build();
    Config<Color> foliageColor = new ColorConfig.Builder("FoliageColor")
            .setDescription("The color of foliage")
            .setDefaultValue(Color.RED).setVisible(foliageConfig::getValue).build();

    Config<Boolean> waterConfig = new BooleanConfig.Builder("Water")
            .setDescription("Change the color of water")
            .setDefaultValue(false).build();
    Config<Color> waterColor = new ColorConfig.Builder("WaterColor")
            .setDescription("The color of water")
            .setDefaultValue(Color.RED).setVisible(waterConfig::getValue).build();

    Config<Boolean> lavaConfig = new BooleanConfig.Builder("Lava")
            .setDescription("Change the color of lava")
            .setDefaultValue(false).build();
    Config<Color> lavaColor = new ColorConfig.Builder("LavaColor")
            .setDescription("The color of lava")
            .setDefaultValue(Color.RED).setVisible(lavaConfig::getValue).build();

    Config<Boolean> glintConfig = new BooleanConfig.Builder("Glint")
            .setDescription("Change the color of enchantment glint")
            .setDefaultValue(false).build();
    Config<Color> glintColor = new ColorConfig.Builder("GlintColor")
            .setDescription("The color of enchantment glint")
            .setDefaultValue(Color.RED).setVisible(glintConfig::getValue).build();

    Config<Boolean> totemsConfig = new BooleanConfig.Builder("TotemEffects")
            .setDescription("Change the color of totem particles")
            .setDefaultValue(false).build();
    Config<Color> totemsColor = new ColorConfig.Builder("TotemColor")
            .setDescription("The color of totem particles")
            .setDefaultValue(Color.RED).setVisible(totemsConfig::getValue).build();

    public TintModule()
    {
        super("Tint", new String[] {"Ambience"}, "Change the world color tint", GuiCategory.RENDER);

        this.shineTexture = Identifier.of(ShorelineMod.MOD_ID, "textures/shine.png");
    }

    @Override
    public void onToggle()
    {
        reloadWorld();
    }

    @EventListener
    public void onFinishedLoading(LoadingEvent.Finished event)
    {
        for (Config<?> config : getConfigs())
        {
            config.addObserver(v -> reloadWorld());
        }

        lavaConfig.addObserver(v -> Managers.RESOURCE_PACK.toggleResourcePack("lava", v));
    }

    @EventListener
    public void onLightTint(WorldTintEvent.Light event)
    {
        if (lightConfig.getValue())
        {
            event.cancel();
            event.setColor(lightColor.getValue());
        }
    }

    @EventListener
    public void onFoliageTint(WorldTintEvent.Foliage event)
    {
        if (foliageConfig.getValue())
        {
            event.cancel();
            event.setColor(foliageColor.getValue());
        }
    }

    @EventListener
    public void onWaterTint(WorldTintEvent.Water event)
    {
        if (waterConfig.getValue())
        {
            event.cancel();
            event.setColor(waterColor.getValue());
        }
    }

    @EventListener
    public void onLavaTint(WorldTintEvent.Lava event)
    {
        if (lavaConfig.getValue())
        {
            event.cancel();
            event.setColor(lavaColor.getValue());
        }
    }

    @EventListener
    public void onBlockLight(BlockLightEvent event)
    {
        if (lightConfig.getValue())
        {
            event.cancel();
            event.setBlockLight(0);
        }
    }

    @EventListener
    public void onEntityLightBlock(EntityLightEvent.Block event)
    {
        if (lightConfig.getValue())
        {
            event.cancel();
            event.setLight(0);
        }
    }

    @EventListener
    public void onEntityLightSky(EntityLightEvent.Skylight event)
    {
        if (lightConfig.getValue())
        {
            event.cancel();
            event.setLight(15);
        }
    }

    @EventListener
    public void onLightData(LightDataEvent event)
    {
        if (lightConfig.getValue())
        {
            event.cancel();
            event.setSl(15);
        }
    }

    @EventListener
    public void onLuminance(LuminanceEvent event)
    {
        if (lightConfig.getValue())
        {
            event.cancel();
            event.setLuminance(0);
        }
    }

    @EventListener
    public void onTotemParticle(TotemParticleEvent event)
    {
        if (totemsConfig.getValue())
        {
            event.cancel();
            event.setColor(totemsColor.getValue());
        }
    }

    @EventListener
    public void onGlintTexturePre(GlintTextureEvent.Pre event)
    {
        if (glintConfig.getValue())
        {
            mc.getTextureManager().getTexture(shineTexture).setFilter(true, false);
            RenderSystem.setShaderTexture(0, shineTexture);
            Color color = glintColor.getValue();
            RenderSystem.setShaderColor(color.getRed() / 255.0f,
                    color.getGreen() / 255.0f,
                    color.getBlue() / 255.0f, 1.0f);
        }
    }

    @EventListener
    public void onGlintTexturePost(GlintTextureEvent.Post event)
    {
        if (glintConfig.getValue())
        {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private void reloadWorld()
    {
        if (mc.worldRenderer != null && mc.player != null)
        {
            int x = (int) mc.player.getX();
            int y = (int) mc.player.getY();
            int z = (int) mc.player.getZ();
            int d = mc.options.getViewDistance().getValue() * 16;
            mc.worldRenderer.scheduleBlockRenders(x - d, y - d, z - d, x + d, y + d, z + d);
        }
    }
}