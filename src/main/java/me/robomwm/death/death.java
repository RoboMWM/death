package me.robomwm.death;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class death extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        try {
            hackWorld(Bukkit.getWorld("world"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void hackWorld(World bukkitWorld) throws Exception {
        final Class<? extends World> bukkitWorldClass = bukkitWorld.getClass();
        final Method getHandle = bukkitWorldClass.getMethod("getHandle");
        final Object handle = getHandle.invoke(bukkitWorld);

        Field f = handle.getClass().getDeclaredField("isClientSide");

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);

        f.set(handle, Boolean.TRUE);
    }

}
