package com.sk89q.craftbook.sponge.mechanics.area;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.data.Sign;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.command.CommandSource;

import com.sk89q.craftbook.sponge.util.SignUtil;

public class Bridge extends SimpleArea {

    public BlockLoc getOtherEnd(BlockLoc block) {

        Direction back = SignUtil.getBack(block);

        for (int i = 0; i < 16; i++) {

            block = block.getRelative(back);

            if (SignUtil.isSign(block)) {
                Sign sign = block.getData(Sign.class).get();

                if (SignUtil.getTextRaw(sign, 1).equals("[Bridge]")) {

                return block; }
            }
        }

        return null;
    }

    @Override
    public boolean triggerMechanic(BlockLoc block, Sign sign, Human human, Boolean forceState) {

        if (SignUtil.getTextRaw(sign, 1).equals("[Bridge]")) {

            Direction back = SignUtil.getBack(block);

            BlockLoc baseBlock = block.getRelative(Direction.DOWN);

            BlockLoc left = baseBlock.getRelative(SignUtil.getLeft(block));
            BlockLoc right = baseBlock.getRelative(SignUtil.getRight(block));

            BlockLoc otherSide = getOtherEnd(block);
            if (otherSide == null) {
                if (human instanceof CommandSource) ((CommandSource) human).sendMessage("Missing other end!");
                return true;
            }

            baseBlock = baseBlock.getRelative(back);

            left = baseBlock.getRelative(SignUtil.getLeft(block));
            right = baseBlock.getRelative(SignUtil.getRight(block));

            BlockState type = block.getRelative(Direction.DOWN).getState();
            if (baseBlock.getState().equals(type) && (forceState == null || forceState == false)) type = BlockTypes.AIR.getDefaultState();

            while (baseBlock.getX() != otherSide.getX() || baseBlock.getZ() != otherSide.getZ()) {

                baseBlock.replaceWith(type);
                left.replaceWith(type);
                right.replaceWith(type);

                baseBlock = baseBlock.getRelative(back);

                left = baseBlock.getRelative(SignUtil.getLeft(block));
                right = baseBlock.getRelative(SignUtil.getRight(block));
            }
        } else return false;

        return true;
    }
}