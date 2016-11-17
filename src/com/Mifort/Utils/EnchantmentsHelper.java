package com.Mifort.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mifort on 08.11.2016.
 */
public class EnchantmentsHelper {

    public static ItemStack getDrops(ItemStack naturally, Block block, int silk, int fortune)
    {
        if(silk == 1)
        {
            naturally.setAmount(1);

            if(block.getType() == Material.GLOWING_REDSTONE_ORE)
            {
                naturally.setType(Material.REDSTONE_ORE);
            }
            else naturally.setType(block.getType());
        }
        else
        {
            int chanceToExtra = 100/(2+fortune);
            Random random = new Random();

            if(block.getType() == Material.GLOWSTONE && fortune > 0)
            {
                naturally.setAmount(4);
            }
            else if(block.getType() == Material.GRAVEL)
            {
                if(fortune == 0)chanceToExtra = 10;
                else if(fortune == 1)chanceToExtra = 14;
                else if(fortune == 2)chanceToExtra = 25;
                else chanceToExtra = 100;

                if(random.nextInt(100) < chanceToExtra)naturally.setType(Material.FLINT);
                else naturally.setType(Material.GRAVEL);

                naturally.setAmount(1);
            }
            else
            {
                int multiplier = 1;
                for(int i = 1; i<= fortune+1;i++)
                {
                    if(random.nextInt(100) < chanceToExtra)multiplier=i;
                }
                naturally.setAmount(naturally.getAmount() * multiplier);
            }
        }
        return naturally;
    }
    public static List<ItemStack> getDrops(List<ItemStack> naturally, Block block, int silk, int fortune)
    {
        List<ItemStack> list = new ArrayList<>();
        if(silk == 1)
            list.add(getDrops(naturally.get(0),block,silk,fortune));
        else
        {
            for(ItemStack is : naturally)
            {
                list.add(getDrops(is,block,silk,fortune));
            }
        }
        return list;
    }
}
