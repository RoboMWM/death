package me.robomwm.death;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import sun.misc.Unsafe;

public final class death extends JavaPlugin
{
    private static final Unsafe unsafe;

    static {
        try
        {
            final Class<Unsafe> clazz = Unsafe.class;
            final Field unsafeField = clazz.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        }
        catch (final Throwable t)
        {
            throw new InternalError(t);
        }
    }

    @Override
    public void onEnable()
    {
        getConfig().addDefault("hak", Collections.singletonList("world"));
        getConfig().options().copyDefaults(true);
        saveConfig();

        for (final String world : getConfig().getStringList("hak"))
        {
            final World w = getServer().getWorld(world);
            if (w == null)
                continue;
            try
            {
                hackWorld(w);
            }
            catch (final Throwable e)
            {
                unsafe.throwException(e);
            }
        }
    }

    private void hackWorld(final World bukkitWorld) throws Throwable
    {
        final Class<? extends World> bukkitWorldClass = bukkitWorld.getClass();
        final Method getHandle = bukkitWorldClass.getMethod("getHandle");
        getHandle.setAccessible(true); // just in case
        final Object handle = getHandle.invoke(bukkitWorld);

        final Field f = findField("isClientSide", handle.getClass());
        unsafe.putBoolean(handle, unsafe.objectFieldOffset(f), true);
    }

    private Field findField(final String field, final Class<?> handle)
    {
        if (handle == null)
            return null;
        try
        {
            return handle.getDeclaredField(field);
        }
        catch (final NoSuchFieldException e)
        {
            return findField(field, handle.getSuperclass());
        }
    }
}
