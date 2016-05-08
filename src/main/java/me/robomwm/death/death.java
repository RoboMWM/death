package me.robomwm.death;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by Robo on 5/7/2016.
 */
public class death extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        hackWorld(Bukkit.getWorld("world"));
    }
    public void hackWorld(World bukkitWorld){
        net.minecraft.server.v1_9_R1.World world =
                ((org.bukkit.craftbukkit.v1_9_R1.CraftWorld)bukkitWorld).getHandle();

        //world.isClientSide = true;
        try {
            Field f = net.minecraft.server.v1_9_R1.World.class.getDeclaredField("isClientSide");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);

            f.set(world, Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
