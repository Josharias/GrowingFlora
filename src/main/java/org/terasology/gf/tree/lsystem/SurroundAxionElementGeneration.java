/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.gf.tree.lsystem;


import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class SurroundAxionElementGeneration implements AxionElementGeneration {
    private TreeBlockDefinition baseBlock;
    private TreeBlockDefinition surroundBlock;
    private float advance;
    private float innerRange;
    private float range;
    private int maxZ = Integer.MAX_VALUE;

    public SurroundAxionElementGeneration(TreeBlockDefinition baseBlock, TreeBlockDefinition surroundBlock, float advance, float range) {
        this(baseBlock, surroundBlock, advance, 0, range);
    }

    public SurroundAxionElementGeneration(TreeBlockDefinition baseBlock, TreeBlockDefinition surroundBlock, float advance, float innerRange, float range) {
        this.baseBlock = baseBlock;
        this.surroundBlock = surroundBlock;
        this.advance = advance;
        this.innerRange = innerRange;
        this.range = range;
    }

    public void setMaxZ(int maxZ) {
        this.maxZ = maxZ;
    }

    @Override
    public void generate(AxionElementGenerationCallback callback, Vector3f position, Matrix4f rotation, String axionParameter) {
        callback.setMainBlock(position, baseBlock);
        int rangeInt = (int) range;
        for (int x = -rangeInt; x <= rangeInt; x++) {
            for (int y = -rangeInt; y <= rangeInt; y++) {
                for (int z = -rangeInt; z <= Math.min(rangeInt, maxZ); z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance < innerRange) {
                        Vector3f v = new Vector3f(x, y, z);
                        rotation.transform(v);
                        Vector3f sideVec = new Vector3f(position);
                        sideVec.add(v);
                        callback.setAdditionalBlock(sideVec, baseBlock);
                    } else if (distance < range) {
                        Vector3f v = new Vector3f(x, y, z);
                        rotation.transform(v);
                        Vector3f sideVec = new Vector3f(position);
                        sideVec.add(v);
                        callback.setAdditionalBlock(sideVec, surroundBlock);
                    }
                }
            }
        }

        callback.advance(advance);
    }
}
