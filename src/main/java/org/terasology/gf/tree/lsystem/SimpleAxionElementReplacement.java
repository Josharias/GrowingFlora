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


import org.terasology.utilities.random.Random;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class SimpleAxionElementReplacement implements AxionElementReplacement {
    private float probabilitySum;
    private String defaultReplacement;
    private List<Float> probabilities = new ArrayList<>();
    private List<ReplacementGenerator> replacements = new ArrayList<>();

    public SimpleAxionElementReplacement(String defaultReplacement) {
        this.defaultReplacement = defaultReplacement;

        probabilities.add(1f);
        replacements.add(null);
    }

    public void addReplacement(float probability, String replacement) {
        addReplacement(probability, new StaticReplacementGenerator(replacement));
    }

    public void addReplacement(float probability, ReplacementGenerator replacement) {
        if (probabilitySum + probability > 1f) {
            throw new IllegalArgumentException("Sum of probabilities exceeds 1");
        }
        probabilitySum += probability;

        probabilities.add(1 - probabilitySum);
        replacements.add(replacement);
    }

    @Override
    public String getReplacement(Random random, String currentAxiom) {
        for (int i = 0, size = probabilities.size(); i < size - 1; i++) {
            float randomValue = random.nextFloat();
            if (probabilities.get(i) > randomValue && probabilities.get(i + 1) <= randomValue) {
                return replacements.get(i + 1).generateReplacement(random, currentAxiom);
            }
        }
        return defaultReplacement;
    }

    private final class StaticReplacementGenerator implements ReplacementGenerator {
        private String result;

        private StaticReplacementGenerator(String result) {
            this.result = result;
        }

        @Override
        public String generateReplacement(Random rnd, String currentAxiom) {
            return result;
        }
    }

    public interface ReplacementGenerator {
        String generateReplacement(Random rnd, String currentAxion);
    }
}
