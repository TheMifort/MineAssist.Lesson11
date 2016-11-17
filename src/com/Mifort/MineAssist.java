package com.Mifort;

import com.Mifort.Providers.RegionProvider;
import com.Mifort.Utils.EnchantmentsHelper;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Mifort on 06.11.2016.
 */
public class MineAssist extends JavaPlugin implements Listener {

    List<AWaitBreak> aWaitBreaks = new ArrayList<>();
    RegionProvider regionProvider;


    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this,this);
        regionProvider = new RegionProvider(this);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        if(player==null)return;

        AWaitBreak aWaitBreak = AWaitBreak.get(player,aWaitBreaks);
        if(aWaitBreak == null)return;

        ItemStack itemInHand = player.getItemInHand();
        if(itemInHand == null)return;

        List<Block> blocks = getBlocks(aWaitBreak);

        int silkTouch = itemInHand.getEnchantmentLevel(Enchantment.SILK_TOUCH);
        int fortune = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        int durability = itemInHand.getEnchantmentLevel(Enchantment.DURABILITY);

        Random random = new Random();

        boolean isBroken = false;

        aWaitBreaks.remove(aWaitBreak);
        if(!regionProvider.canChangeRegion(player,event.getBlock().getLocation()))return;

        if(player.getFoodLevel() == 0)return;

        for(Block block : blocks)
        {
            if(!regionProvider.canChangeRegion(player,block.getLocation()))continue;

            if(random.nextInt(100) < (100/(1+durability)))
                itemInHand.setDurability((short)(itemInHand.getDurability() + 1));

            for(ItemStack is : EnchantmentsHelper.getDrops((List<ItemStack>)block.getDrops(itemInHand),block,silkTouch,fortune))
            {
                player.getWorld().dropItemNaturally(block.getLocation(),is);
            }

            if(itemInHand.getDurability() >= itemInHand.getType().getMaxDurability())
            {
                isBroken = true;
                player.getInventory().removeItem(itemInHand);
                break;
            }


            if(player.getGameMode() == GameMode.SURVIVAL) {
                player.setExhaustion(player.getExhaustion() + 0.025f);
                if(player.getExhaustion() >= 4)
                {
                    if(player.getSaturation() >= 1)
                    {
                        player.setSaturation(player.getSaturation() - 1);
                    }
                    else
                    {
                        player.setFoodLevel(player.getFoodLevel() - 1);
                    }
                    player.setExhaustion(0);
                }
            }

            block.setType(Material.AIR);
        }


        if(!isBroken)
        player.setItemInHand(itemInHand);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if(player==null)return;

        AWaitBreak aWaitBreak = AWaitBreak.get(player,aWaitBreaks);
        if(aWaitBreak != null)aWaitBreaks.remove(aWaitBreak);

        if(event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            aWaitBreaks.add(new AWaitBreak(player,event.getClickedBlock(),event.getBlockFace()));
        }
    }

    public List<Block> getBlocks(AWaitBreak aWaitBreak)
    {
        if(aWaitBreak.face == BlockFace.DOWN || aWaitBreak.face == BlockFace.UP)
            return getBlocks(aWaitBreak,1,0,1);
        else if(aWaitBreak.face == BlockFace.EAST || aWaitBreak.face == BlockFace.WEST)
            return getBlocks(aWaitBreak,0,1,1);
        else return getBlocks(aWaitBreak,1,1,0);
    }

    public List<Block> getBlocks(AWaitBreak aWaitBreak,int x,int y,int z)
    {
        List<Block> blocks = new ArrayList<>();

        World world = aWaitBreak.player.getWorld();

        int bX = aWaitBreak.block.getX();
        int bY = aWaitBreak.block.getY();
        int bZ = aWaitBreak.block.getZ();

        for(int cX = -x;cX <=x;cX++)
            for(int cY = -y;cY <=y;cY++)
                for(int cZ = -z;cZ <=z;cZ++)
                {
                    Block block = world.getBlockAt(bX + cX,bY + cY,bZ + cZ);
                    if(aWaitBreak.block == block)continue;
                    if(block.getType() == Material.AIR)continue;

                    blocks.add(block);
                }
        return blocks;
    }
}
