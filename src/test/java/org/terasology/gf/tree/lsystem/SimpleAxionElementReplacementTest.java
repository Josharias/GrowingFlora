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

import org.junit.Test;
import org.mockito.Mockito;
import org.terasology.utilities.random.Random;

import static org.junit.Assert.assertEquals;

public class SimpleAxionElementReplacementTest {
    private SimpleAxionElementReplacement replacements = new SimpleAxionElementReplacement("A");

    @Test
    public void testNoReplacements() {
        assertEquals("A", replacements.getReplacement(createRandom(0f), ""));
        assertEquals("A", replacements.getReplacement(createRandom(0.5f), ""));
        assertEquals("A", replacements.getReplacement(createRandom(0.99f), ""));
    }

    @Test
    public void testOneReplacement() {
        replacements.addReplacement(1, "B");
        assertEquals("B", replacements.getReplacement(createRandom(0f), ""));
        assertEquals("B", replacements.getReplacement(createRandom(0.5f), ""));
        assertEquals("B", replacements.getReplacement(createRandom(0.99f), ""));
    }

    @Test
    public void testTwoReplacementWholeProbability() {
        replacements.addReplacement(0.5f, "B");
        replacements.addReplacement(0.5f, "C");
        assertEquals("C", replacements.getReplacement(createRandom(0f), ""));
        assertEquals("B", replacements.getReplacement(createRandom(0.5f), ""));
        assertEquals("B", replacements.getReplacement(createRandom(0.99f), ""));
    }

    @Test
    public void testTwoReplacementWithDefault() {
        replacements.addReplacement(0.3f, "B");
        replacements.addReplacement(0.3f, "C");
        assertEquals("A", replacements.getReplacement(createRandom(0f), ""));
        assertEquals("A", replacements.getReplacement(createRandom(0.2f), ""));
        assertEquals("A", replacements.getReplacement(createRandom(0.3f), ""));
        assertEquals("C", replacements.getReplacement(createRandom(0.4f), ""));
        assertEquals("C", replacements.getReplacement(createRandom(0.5f), ""));
        assertEquals("C", replacements.getReplacement(createRandom(0.6f), ""));
        assertEquals("B", replacements.getReplacement(createRandom(0.7f), ""));
        assertEquals("B", replacements.getReplacement(createRandom(0.99f), ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGoingAboveOne() {
        replacements.addReplacement(0.3f, "B");
        replacements.addReplacement(0.8f, "C");
    }

    private Random createRandom(float value) {
        Random rnd = Mockito.mock(Random.class);
        Mockito.when(rnd.nextFloat()).thenReturn(value);
        return rnd;
    }
}
