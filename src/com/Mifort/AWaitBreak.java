package com.Mifort;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Mifort on 06.11.2016.
 */
public class AWaitBreak {

    public Player player;
    public Block block;
    public BlockFace face;

    public AWaitBreak(Player player, Block block,BlockFace face)
    {
        this.player = player;
        this.block = block;
        this.face = face;
    }

    public static AWaitBreak get(Player player, List<AWaitBreak> list)
    {
        for(AWaitBreak aWaitBreak : list)
        {
            if(aWaitBreak.player.getName().equals(player.getName()))return aWaitBreak;
        }
        return null;
    }
}
