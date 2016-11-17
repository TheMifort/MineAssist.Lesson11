package com.Mifort.Providers;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by Mifort on 10.11.2016.
 */
public class RegionProvider {

    private Logger logger;
    private WorldGuardPlugin worldGuard;

    public RegionProvider(JavaPlugin plugin)
    {
        logger = plugin.getLogger();

        Plugin wg = plugin.getServer().getPluginManager().getPlugin("WorldGuard");

        if(wg != null && wg instanceof WorldGuardPlugin)
        {
            worldGuard = (WorldGuardPlugin)wg;
            logger.info("WorldGuard was found and integrated");
        }
        else
        {
            logger.info("WorldGuard was not found");
        }
    }

    public boolean canChangeRegion(Player player, Location location)
    {
        if(worldGuard != null && worldGuard.isEnabled())
        {
            return worldGuard.canBuild(player,location);
        }

        return true;
    }

}
