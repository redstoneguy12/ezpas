package com.kqp.ezpas.item;

import com.kqp.ezpas.block.entity.pullerpipe.PullerPipeBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

public class PipeProbeItem extends Item {
    public PipeProbeItem() {
        super(new Item.Settings().group(ItemGroup.REDSTONE).maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if (!world.isClient) {
            BlockPos pos = context.getBlockPos();
            BlockEntity be = world.getBlockEntity(pos);

            if (be instanceof PullerPipeBlockEntity) {
                PullerPipeBlockEntity ppBe = ((PullerPipeBlockEntity) be);
                List<PullerPipeBlockEntity.ValidInventory> invList = ppBe.getValidInventories();

                Consumer<Text> send = (text) -> {
                    context.getPlayer().sendMessage(text, false);
                };

                Consumer<String> sendText = (string) -> {
                    send.accept(new LiteralText(string));
                };

                sendText.accept("Insertion points:");
                if (invList.isEmpty()) {
                    sendText.accept("None");
                } else {
                    for (PullerPipeBlockEntity.ValidInventory inventory : invList) {
                        send.accept(new TranslatableText(world.getBlockState(inventory.blockPos).getBlock().getTranslationKey())
                                .append(String.format("@(%d, %d, %d), into %s side",
                                        inventory.blockPos.getX(),
                                        inventory.blockPos.getY(),
                                        inventory.blockPos.getZ(),
                                        inventory.direction
                                ))
                        );

                        MutableText whitelistText = new LiteralText("Whitelist: ");
                        if (inventory.filters.whitelist.isEmpty()) {
                            whitelistText.append("None");
                        } else {
                            int i = 0;

                            for (PullerPipeBlockEntity.ComparableItemStack itemStack : inventory.filters.whitelist) {
                                whitelistText.append(new TranslatableText(itemStack.itemStack.getItem().getTranslationKey()));

                                if (i != inventory.filters.whitelist.size() - 1) {
                                    whitelistText.append(", ");
                                }

                                i++;
                            }
                        }
                        send.accept(whitelistText);

                        MutableText blacklistText = new LiteralText("Blacklist: ");
                        if (inventory.filters.blacklist.isEmpty()) {
                            blacklistText.append("None");
                        } else {
                            int i = 0;

                            for (PullerPipeBlockEntity.ComparableItemStack itemStack : inventory.filters.blacklist) {
                                blacklistText.append(new TranslatableText(itemStack.itemStack.getItem().getTranslationKey()));

                                if (i != inventory.filters.blacklist.size() - 1) {
                                    blacklistText.append(", ");
                                }

                                i++;
                            }
                        }
                        send.accept(blacklistText);

                        sendText.accept("");
                    }
                }
            }
        }

        return ActionResult.SUCCESS;
    }
}
