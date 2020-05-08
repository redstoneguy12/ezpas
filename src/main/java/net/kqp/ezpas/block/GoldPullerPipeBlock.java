package net.kqp.ezpas.block;

import net.kqp.ezpas.block.entity.GoldPullerPipeBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class GoldPullerPipeBlock extends PullerPipeBlock {
    public GoldPullerPipeBlock() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new GoldPullerPipeBlockEntity();
    }
}
