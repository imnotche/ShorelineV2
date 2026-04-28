package net.shoreline.client.util.math;

import lombok.experimental.UtilityClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.shoreline.client.impl.render.Interpolation;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TrajectoryUtil
{
    public List<Vec3d> getTrajectory(Entity entity, int ticks)
    {
        if (entity instanceof EnderPearlEntity)
        {
            return getPearlTrajectory(entity, ticks);
        }
        else if (entity instanceof ArrowEntity || entity instanceof SpectralArrowEntity)
        {
            return getArrowTrajectory(entity, ticks);
        }
        else if (entity instanceof TridentEntity)
        {
            return getTridentTrajectory(entity, ticks);
        }
        else if (entity instanceof SnowballEntity || entity instanceof EggEntity || entity instanceof ExperienceBottleEntity)
        {
            return getThrownItemTrajectory(entity, ticks);
        }
        else if (entity instanceof FireballEntity)
        {
            return getFireballTrajectory(entity, ticks);
        }
        else if (entity instanceof WitherSkullEntity)
        {
            return getWitherSkullTrajectory(entity, ticks);
        }
        else if (entity instanceof FishingBobberEntity)
        {
            return getFishingBobberTrajectory(entity, ticks);
        }
        else if (entity instanceof LlamaSpitEntity)
        {
            return getLlamaSpitTrajectory(entity, ticks);
        }

        return getPearlTrajectory(entity, ticks);
    }

    public List<Vec3d> getPearlTrajectory(Entity entity, int ticks)
    {
        List<Vec3d> result = new ArrayList<>();
        Vec3d interp = Interpolation.getRenderPosition(entity, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        result.add(interp);

        Vec3d last;
        double x = interp.getX();
        double y = interp.getY();
        double z = interp.getZ();

        Vec3d velocity = entity.getVelocity();
        double mX = velocity.getX();
        double mY = velocity.getY();
        double mZ = velocity.getZ();

        // https://minecraft.wiki/w/Ender_Pearl
        double gravity = 0.03;
        float drag     = 0.99f;
        while (ticks-- >= 0)
        {
            last = new Vec3d(x, y, z);

            x += mX;
            y += mY;
            z += mZ;

            mX *= drag;
            mY = (mY * drag) - gravity;
            mZ *= drag;

            Vec3d position = new Vec3d(x, y, z);
            RaycastContext context = new RaycastContext(last, position, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity);
            BlockHitResult ray = MinecraftClient.getInstance().world.raycast(context);
            if (ray != null && ray.getType() == HitResult.Type.BLOCK)
            {
                return result;
            }

            result.add(position);
        }

        return result;
    }

    // https://minecraft.wiki/w/Arrow
    public List<Vec3d> getArrowTrajectory(Entity entity, int ticks)
    {
        List<Vec3d> result = new ArrayList<>();
        Vec3d interp = Interpolation.getRenderPosition(entity, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        result.add(interp);

        double x = interp.getX();
        double y = interp.getY();
        double z = interp.getZ();

        Vec3d velocity = entity.getVelocity();
        double mX = velocity.getX();
        double mY = velocity.getY();
        double mZ = velocity.getZ();

        double gravity = 0.05;
        float drag     = 0.99f;
        while (ticks-- >= 0)
        {
            Vec3d last = new Vec3d(x, y, z);

            x += mX;
            y += mY;
            z += mZ;

            mX *= drag;
            mY = (mY * drag) - gravity;
            mZ *= drag;

            Vec3d position = new Vec3d(x, y, z);
            RaycastContext context = new RaycastContext(last, position, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity);
            BlockHitResult ray = MinecraftClient.getInstance().world.raycast(context);
            if (ray != null && ray.getType() == HitResult.Type.BLOCK)
            {
                return result;
            }

            result.add(position);
        }

        return result;
    }

    // https://minecraft.wiki/w/Trident
    public List<Vec3d> getTridentTrajectory(Entity entity, int ticks)
    {
        List<Vec3d> result = new ArrayList<>();
        Vec3d interp = Interpolation.getRenderPosition(entity, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        result.add(interp);

        double x = interp.getX();
        double y = interp.getY();
        double z = interp.getZ();

        Vec3d velocity = entity.getVelocity();
        double mX = velocity.getX();
        double mY = velocity.getY();
        double mZ = velocity.getZ();

        double gravity = 0.05;
        float drag     = 0.99f;
        while (ticks-- >= 0)
        {
            Vec3d last = new Vec3d(x, y, z);

            x += mX;
            y += mY;
            z += mZ;

            mX *= drag;
            mY = (mY * drag) - gravity;
            mZ *= drag;

            Vec3d position = new Vec3d(x, y, z);
            RaycastContext context = new RaycastContext(last, position, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity);
            BlockHitResult ray = MinecraftClient.getInstance().world.raycast(context);
            if (ray != null && ray.getType() == HitResult.Type.BLOCK)
            {
                return result;
            }

            result.add(position);
        }

        return result;
    }

    // https://minecraft.wiki/w/Snowball
    // Covers: snowball, egg, splash/lingering potion, experience bottle
    public List<Vec3d> getThrownItemTrajectory(Entity entity, int ticks)
    {
        List<Vec3d> result = new ArrayList<>();
        Vec3d interp = Interpolation.getRenderPosition(entity, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        result.add(interp);

        double x = interp.getX();
        double y = interp.getY();
        double z = interp.getZ();

        Vec3d velocity = entity.getVelocity();
        double mX = velocity.getX();
        double mY = velocity.getY();
        double mZ = velocity.getZ();

        double gravity = entity instanceof ExperienceBottleEntity ? 0.07 : 0.03;
        float drag     = 0.99f;
        while (ticks-- >= 0)
        {
            Vec3d last = new Vec3d(x, y, z);

            x += mX;
            y += mY;
            z += mZ;

            mX *= drag;
            mY = (mY * drag) - gravity;
            mZ *= drag;

            Vec3d position = new Vec3d(x, y, z);
            RaycastContext context = new RaycastContext(last, position, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity);
            BlockHitResult ray = MinecraftClient.getInstance().world.raycast(context);
            if (ray != null && ray.getType() == HitResult.Type.BLOCK)
            {
                return result;
            }

            result.add(position);
        }

        return result;
    }

    // https://minecraft.wiki/w/Fireball
    // Ghast/blaze fireballs: no gravity, constant acceleration from accel field
    public List<Vec3d> getFireballTrajectory(Entity entity, int ticks)
    {
        List<Vec3d> result = new ArrayList<>();
        Vec3d interp = Interpolation.getRenderPosition(entity, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        result.add(interp);

        double x = interp.getX();
        double y = interp.getY();
        double z = interp.getZ();

        Vec3d velocity = entity.getVelocity();
        double mX = velocity.getX();
        double mY = velocity.getY();
        double mZ = velocity.getZ();

        float drag = 0.95f;
        while (ticks-- >= 0)
        {
            Vec3d last = new Vec3d(x, y, z);

            x += mX;
            y += mY;
            z += mZ;

            mX *= drag;
            mY *= drag;
            mZ *= drag;

            Vec3d position = new Vec3d(x, y, z);
            RaycastContext context = new RaycastContext(last, position, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity);
            BlockHitResult ray = MinecraftClient.getInstance().world.raycast(context);
            if (ray != null && ray.getType() == HitResult.Type.BLOCK)
            {
                return result;
            }

            result.add(position);
        }

        return result;
    }

    // https://minecraft.wiki/w/Wither_Skull
    // Same flight model as fireball but slower drag
    public List<Vec3d> getWitherSkullTrajectory(Entity entity, int ticks)
    {
        List<Vec3d> result = new ArrayList<>();
        Vec3d interp = Interpolation.getRenderPosition(entity, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        result.add(interp);

        double x = interp.getX();
        double y = interp.getY();
        double z = interp.getZ();

        Vec3d velocity = entity.getVelocity();
        double mX = velocity.getX();
        double mY = velocity.getY();
        double mZ = velocity.getZ();

        float drag = 0.95f;
        while (ticks-- >= 0)
        {
            Vec3d last = new Vec3d(x, y, z);

            x += mX;
            y += mY;
            z += mZ;

            mX *= drag;
            mY *= drag;
            mZ *= drag;

            Vec3d position = new Vec3d(x, y, z);
            RaycastContext context = new RaycastContext(last, position, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity);
            BlockHitResult ray = MinecraftClient.getInstance().world.raycast(context);
            if (ray != null && ray.getType() == HitResult.Type.BLOCK)
            {
                return result;
            }

            result.add(position);
        }

        return result;
    }

    // https://minecraft.wiki/w/Fishing_Rod
    public List<Vec3d> getFishingBobberTrajectory(Entity entity, int ticks)
    {
        List<Vec3d> result = new ArrayList<>();
        Vec3d interp = Interpolation.getRenderPosition(entity, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        result.add(interp);

        double x = interp.getX();
        double y = interp.getY();
        double z = interp.getZ();

        Vec3d velocity = entity.getVelocity();
        double mX = velocity.getX();
        double mY = velocity.getY();
        double mZ = velocity.getZ();

        double gravity = 0.03;
        float drag     = 0.92f;
        while (ticks-- >= 0)
        {
            Vec3d last = new Vec3d(x, y, z);

            x += mX;
            y += mY;
            z += mZ;

            mX *= drag;
            mY = (mY * drag) - gravity;
            mZ *= drag;

            Vec3d position = new Vec3d(x, y, z);
            RaycastContext context = new RaycastContext(last, position, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity);
            BlockHitResult ray = MinecraftClient.getInstance().world.raycast(context);
            if (ray != null && ray.getType() == HitResult.Type.BLOCK)
            {
                return result;
            }

            result.add(position);
        }

        return result;
    }

    // https://minecraft.wiki/w/Llama#Spitting
    public List<Vec3d> getLlamaSpitTrajectory(Entity entity, int ticks)
    {
        List<Vec3d> result = new ArrayList<>();
        Vec3d interp = Interpolation.getRenderPosition(entity, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        result.add(interp);

        double x = interp.getX();
        double y = interp.getY();
        double z = interp.getZ();

        Vec3d velocity = entity.getVelocity();
        double mX = velocity.getX();
        double mY = velocity.getY();
        double mZ = velocity.getZ();

        double gravity = 0.06;
        float drag     = 0.99f;
        while (ticks-- >= 0)
        {
            Vec3d last = new Vec3d(x, y, z);

            x += mX;
            y += mY;
            z += mZ;

            mX *= drag;
            mY = (mY * drag) - gravity;
            mZ *= drag;

            Vec3d position = new Vec3d(x, y, z);
            RaycastContext context = new RaycastContext(last, position, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity);
            BlockHitResult ray = MinecraftClient.getInstance().world.raycast(context);
            if (ray != null && ray.getType() == HitResult.Type.BLOCK)
            {
                return result;
            }

            result.add(position);
        }

        return result;
    }
}