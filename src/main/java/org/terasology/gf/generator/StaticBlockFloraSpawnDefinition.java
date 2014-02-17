/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.gf.generator;

import com.google.common.base.Predicate;
import org.terasology.anotherWorld.GenerationLocalParameters;
import org.terasology.anotherWorld.GenerationParameters;
import org.terasology.anotherWorld.LocalParameters;
import org.terasology.gf.PlantType;
import org.terasology.math.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.utilities.random.FastRandom;
import org.terasology.world.ChunkView;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.chunks.ChunkConstants;

import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class StaticBlockFloraSpawnDefinition implements PlantSpawnDefinition {
    private PlantType plantType;
    private String biomeId;
    private float rarity;
    private float probability;
    private String plantId;
    private List<BlockUri> possibleBlocks;
    private Predicate<Block> groundFilter;
    private Predicate<LocalParameters> spawnCondition;

    public StaticBlockFloraSpawnDefinition(PlantType plantType, String biomeId, float rarity, float probability, String plantId,
                                           List<BlockUri> possibleBlocks, Predicate<Block> groundFilter, Predicate<LocalParameters> spawnCondition) {
        this.plantType = plantType;
        this.biomeId = biomeId;
        this.rarity = rarity;
        this.probability = probability;
        this.plantId = plantId;
        this.possibleBlocks = possibleBlocks;
        this.groundFilter = groundFilter;
        this.spawnCondition = spawnCondition;
    }

    @Override
    public PlantType getPlantType() {
        return plantType;
    }

    @Override
    public String getBiomeId() {
        return biomeId;
    }

    @Override
    public float getRarity() {
        return rarity;
    }

    @Override
    public float getProbability() {
        return probability;
    }

    @Override
    public String getPlantId() {
        return plantId;
    }

    @Override
    public void generatePlant(String seed, Vector3i chunkPos, ChunkView chunk, int x, int y, int z, GenerationParameters generationParameters) {
        if (groundFilter.apply(chunk.getBlock(x, y, z)) && chunk.getBlock(x, y + 1, z) == BlockManager.getAir()
                && shouldSpawn(chunkPos, x, y, z, generationParameters)) {
            BlockUri block = possibleBlocks.get(new FastRandom().nextInt(possibleBlocks.size()));
            Block blockToPlace = CoreRegistry.get(BlockManager.class).getBlockFamily(block).getArchetypeBlock();
            chunk.setBlock(x, y + 1, z, blockToPlace);
        }
    }

    private boolean shouldSpawn(Vector3i chunkPos, int x, int y, int z, GenerationParameters generationParameters) {
        return (spawnCondition == null || spawnCondition.apply(new GenerationLocalParameters(generationParameters,
                new Vector3i(chunkPos.x * ChunkConstants.SIZE_X + x, y, chunkPos.z * ChunkConstants.SIZE_Z + z))));
    }
}