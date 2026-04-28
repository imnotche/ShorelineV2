package net.shoreline.client.impl.module.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.util.math.Vec3d;
import net.shoreline.client.api.config.ColorConfig;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.module.GuiCategory;
import net.shoreline.client.impl.Managers;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.impl.module.impl.RenderModule;
import net.shoreline.client.util.math.TrajectoryUtil;
import net.shoreline.eventbus.annotation.EventListener;

import java.awt.*;
import java.util.List;

public class TrajectoriesModule extends RenderModule
{
    Config<Color> pearlColor = new ColorConfig.Builder("PearlColor")
            .setTransparency(true).setDescription("Ender pearl trajectory color")
            .setDefaultValue(new Color(0, 220, 255, 200)).build();

    Config<Color> arrowColor = new ColorConfig.Builder("ArrowColor")
            .setTransparency(true).setDescription("Arrow trajectory color")
            .setDefaultValue(new Color(255, 220, 0, 200)).build();

    Config<Color> tridentColor = new ColorConfig.Builder("TridentColor")
            .setTransparency(true).setDescription("Trident trajectory color")
            .setDefaultValue(new Color(0, 180, 255, 200)).build();

    Config<Color> thrownColor = new ColorConfig.Builder("ThrownColor")
            .setTransparency(true).setDescription("Thrown item trajectory color")
            .setDefaultValue(new Color(255, 255, 255, 200)).build();

    Config<Color> fireballColor = new ColorConfig.Builder("FireballColor")
            .setTransparency(true).setDescription("Fireball trajectory color")
            .setDefaultValue(new Color(255, 80, 0, 200)).build();

    Config<Color> witherSkullColor = new ColorConfig.Builder("WitherSkullColor")
            .setTransparency(true).setDescription("Wither skull trajectory color")
            .setDefaultValue(new Color(50, 50, 50, 200)).build();

    Config<Color> bobberColor = new ColorConfig.Builder("BobberColor")
            .setTransparency(true).setDescription("Fishing bobber trajectory color")
            .setDefaultValue(new Color(255, 0, 100, 200)).build();

    Config<Color> llamaSpitColor = new ColorConfig.Builder("LlamaSpitColor")
            .setTransparency(true).setDescription("Llama spit trajectory color")
            .setDefaultValue(new Color(200, 200, 50, 200)).build();

    public TrajectoriesModule()
    {
        super("Trajectories", "Shows trajectories of projectiles", GuiCategory.RENDER);
    }

    @EventListener
    public void onRender(RenderWorldEvent.Post event)
    {
        if (checkNull()) return;

        for (Entity entity : mc.world.getEntities())
        {
            if (!mc.world.getWorldBorder().contains(entity.getBlockPos()))
            {
                continue;
            }

            List<Vec3d> trajectory;
            int rgb;

            if (entity instanceof EnderPearlEntity)
            {
                trajectory = TrajectoryUtil.getPearlTrajectory(entity, 300);
                rgb = pearlColor.getValue().getRGB();
            }
            else if (entity instanceof ArrowEntity || entity instanceof SpectralArrowEntity)
            {
                trajectory = TrajectoryUtil.getArrowTrajectory(entity, 300);
                rgb = arrowColor.getValue().getRGB();
            }
            else if (entity instanceof TridentEntity)
            {
                trajectory = TrajectoryUtil.getTridentTrajectory(entity, 300);
                rgb = tridentColor.getValue().getRGB();
            }
            else if (entity instanceof SnowballEntity
                    || entity instanceof EggEntity
                    || entity instanceof ExperienceBottleEntity)
            {
                trajectory = TrajectoryUtil.getThrownItemTrajectory(entity, 300);
                rgb = thrownColor.getValue().getRGB();
            }
            else if (entity instanceof FireballEntity)
            {
                trajectory = TrajectoryUtil.getFireballTrajectory(entity, 300);
                rgb = fireballColor.getValue().getRGB();
            }
            else if (entity instanceof WitherSkullEntity)
            {
                trajectory = TrajectoryUtil.getWitherSkullTrajectory(entity, 300);
                rgb = witherSkullColor.getValue().getRGB();
            }
            else if (entity instanceof FishingBobberEntity)
            {
                trajectory = TrajectoryUtil.getFishingBobberTrajectory(entity, 300);
                rgb = bobberColor.getValue().getRGB();
            }
            else if (entity instanceof LlamaSpitEntity)
            {
                trajectory = TrajectoryUtil.getLlamaSpitTrajectory(entity, 300);
                rgb = llamaSpitColor.getValue().getRGB();
            }
            else
            {
                continue;
            }

            if (trajectory.isEmpty()) continue;

            renderTrajectory(event, trajectory, rgb);
        }
    }

    private void renderTrajectory(RenderWorldEvent.Post event, List<Vec3d> trajectory, int rgb)
    {
        Vec3d last = trajectory.getFirst();
        for (Vec3d pos : trajectory)
        {
            Managers.RENDER.renderLine(event.getMatrixStack(), last, pos, rgb);
            last = pos;
        }
    }
}