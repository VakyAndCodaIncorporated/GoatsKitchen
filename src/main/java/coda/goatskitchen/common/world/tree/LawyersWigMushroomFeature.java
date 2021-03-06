package coda.goatskitchen.common.world.tree;

import coda.goatskitchen.common.init.GKBlocks;
import coda.goatskitchen.util.Entry;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.ArrayList;
import java.util.Random;

public class LawyersWigMushroomFeature extends Feature<NoFeatureConfig> {
    private static final BlockState trunk = Blocks.MUSHROOM_STEM.defaultBlockState();
    private static final BlockState cap = GKBlocks.LAWYERS_WIG_MUSHROOM_BLOCK.get().defaultBlockState();

    //NOTE all random values below have 1 added to them when randomizing, the values determine the maximum possible output, not number of outputs

    //trunk placement
    public static int minimumTrunkHeight = 4;
    public static int trunkHeightExtra = 1;

    //branches on the trunk placement
    public static int minimumBranchHeight = 2;
    public static int branchHeightExtra = 0;

    //thinner, 'top' trunk placement
    public static int minimumTrunkTopHeight = 0;
    public static int trunkTopHeightExtra = 0;

    //branches on the top trunk placement
    public static int minimumTopBranchHeight = 1;
    public static int topBranchHeightExtra = 0;

    public LawyersWigMushroomFeature()
    {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public boolean place(ISeedReader iSeedReader, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, NoFeatureConfig noFeatureConfig) {
        ArrayList<Entry> filler = new ArrayList<>();
        ArrayList<Entry> leavesFiller = new ArrayList<>();
        int trunkHeight = minimumTrunkHeight + random.nextInt(trunkHeightExtra + 1);
        for (int i = 0; i < trunkHeight; i++) {
            int xOffset = 0;
            int zOffset = 0;
            BlockPos trunkPos = blockPos.offset(xOffset, i, zOffset);
            if (i == 0 && !canGrowTree(iSeedReader, trunkPos)) {
                return false;
            }
            boolean success = makeSlice(filler, iSeedReader, trunkPos, 1);
            if (!success) {
                return false;
            }
        }

        for (int i = 0; i < trunkHeight - 1; i++) {
            for (int j = 0; j < 1; j++) {
                int xOffset = 0;
                int zOffset = 0;
                BlockPos trunkPos = blockPos.offset(xOffset, i, zOffset);
                if (i == 0 && !canGrowTree(iSeedReader, trunkPos)) {
                    return false;
                }
                if (i == 5) {
                    boolean success = makeSlice(filler, iSeedReader, trunkPos, 1);
                    if (!success) {
                        return false;
                    }
                }
            }
        }

        int height = 4 + random.nextInt(1);
        int width = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <= height; j++) {
                double remove = 2 + Math.cos(j * -(Math.PI / height + 8)) * 2;
                for (int k = -width; k <= width; k++) {
                    for (int m = -width; m <= width; m++) {
                        for (int l = -4; l < 0; l++) {
                            if (k * k + m * m <= remove * remove) {
                                if (i == 1) {
                                    iSeedReader.setBlock(blockPos.offset(k, l + trunkHeight + 2, m), GKBlocks.LAWYERS_WIG_MUSHROOM_BLOCK.get().defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }
            }
        }

        int sliceSize = 1;
        for (int x = -sliceSize; x <= sliceSize; x++) {
            for (int z = -sliceSize; z <= sliceSize; z++) {
                if (Math.abs(x) == sliceSize && Math.abs(z) == sliceSize) {
                    continue;
                }
                BlockPos slicePos = new BlockPos(blockPos);
                if (!canPlace(iSeedReader, slicePos)) {
                    return false;
                }
                iSeedReader.setBlock(blockPos.offset(x, trunkHeight + 2, z), GKBlocks.LAWYERS_WIG_MUSHROOM_BLOCK.get().defaultBlockState(), 2);
                iSeedReader.setBlock(blockPos.offset(x, trunkHeight - height + 1, z), GKBlocks.LAWYERS_WIG_MUSHROOM_BLOCK.get().defaultBlockState(), 2);
            }
        }

        fill(iSeedReader, filler, false);
        fill(iSeedReader, leavesFiller, true);
        return false;
    }

    public static boolean makeBranch(ArrayList<Entry> filler, ArrayList<Entry> leavesFiller, ISeedReader reader, BlockPos pos, int height) {
        for (int k = 0; k < height; k++) {
            BlockPos branchPos = pos.above(k);
            if (!canPlace(reader, branchPos)) {
                return false;
            }
            filler.add(new Entry(branchPos, trunk));
            if (k == height - 1) {
                leavesFiller.add(new Entry(branchPos.above(), cap));
            }
        }
        return true;
    }

    public static boolean makeSlice(ArrayList<Entry> filler, ISeedReader reader, BlockPos pos, int sliceSize) {
//        for (int x = -sliceSize; x <= sliceSize; x++) {
//            for (int z = -sliceSize; z <= sliceSize; z++) {
//                if (Math.abs(x) == sliceSize && Math.abs(z) == sliceSize) {
//                    continue;
//                }
                BlockPos slicePos = new BlockPos(pos);
                if (!canPlace(reader, slicePos)) {
                    return false;
                }
                filler.add(new Entry(slicePos, trunk));
//            }
//        }
        return true;
    }

    public static void fill(ISeedReader reader, ArrayList<Entry> filler, boolean careful) {
        for (Entry entry : filler) {
            if (careful && !canPlace(reader, entry.pos)) {
                continue;
            }
            reader.setBlock(entry.pos, entry.state, 3);
        }
    }

    public static boolean canGrowTree(ISeedReader reader, BlockPos pos) {
        return canPlace(reader, pos);
    }

    public static boolean canPlace(ISeedReader reader, BlockPos pos) {
        if (pos.getY() > reader.getMaxBuildHeight() || pos.getY() < 0) {
            return false;
        }
        return reader.getBlockState(pos).getBlock().equals(GKBlocks.LAWYERS_WIG_MUSHROOM.get()) || reader.isEmptyBlock(pos) || reader.getBlockState(pos).getMaterial().isReplaceable();
    }
}