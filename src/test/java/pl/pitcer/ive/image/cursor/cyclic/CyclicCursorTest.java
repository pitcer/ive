/*
 * MIT License
 *
 * Copyright (c) 2020 Piotr Dobiech
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pl.pitcer.ive.image.cursor.cyclic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CyclicCursorTest {

    private static final List<String> NUMBERS = List.of("0", "1", "2", "3", "4");

    @Test
    public void testCursorHasProperSize() {
        var cursor = new CyclicCursor<>(NUMBERS);
        int size = cursor.getSize();
        Assertions.assertEquals(5, size);
    }

    @Test
    public void testCursorHasProperValues() {
        var cursor = new CyclicCursor<>(NUMBERS);
        List<String> values = new ArrayList<>();
        while (cursor.hasNext()) {
            cursor.moveNext();
            String currentElement = cursor.getCurrentValue();
            values.add(currentElement);
        }
        Assertions.assertEquals(NUMBERS, values);
    }

    @Test
    public void testCursorHasProperValuesCollectedByStream() {
        var cursor = new CyclicCursor<>(NUMBERS);
        List<String> values = cursor.stream()
            .collect(Collectors.toUnmodifiableList());
        Assertions.assertEquals(NUMBERS, values);
    }

    @Test
    public void testFirstNextReturnsFirstValue() {
        var cursor = new CyclicCursor<>(NUMBERS);
        Assertions.assertThrows(NoSuchElementException.class, cursor::getCurrentValue);
        cursor.moveNext();
        Assertions.assertEquals("0", cursor.getCurrentValue());
    }

    @Test
    public void testLastNextReturnsFirstValue() {
        var cursor = new CyclicCursor<>(NUMBERS, 4);
        Assertions.assertEquals("4", cursor.getCurrentValue());
        cursor.moveNext();
        Assertions.assertEquals("0", cursor.getCurrentValue());
    }

    @Test
    public void testFirstPreviousReturnsLastValue() {
        var cursor = new CyclicCursor<>(NUMBERS);
        Assertions.assertThrows(NoSuchElementException.class, cursor::getCurrentValue);
        cursor.movePrevious();
        Assertions.assertEquals("4", cursor.getCurrentValue());
    }

    @Test
    public void testLastPreviousReturnsOneBeforeLastValue() {
        var cursor = new CyclicCursor<>(NUMBERS, 4);
        Assertions.assertEquals("4", cursor.getCurrentValue());
        cursor.movePrevious();
        Assertions.assertEquals("3", cursor.getCurrentValue());
    }

    @Test
    public void testNextAndPreviousCombinedReturnsStartingValue() {
        var cursor = new CyclicCursor<>(NUMBERS, 0);
        Assertions.assertEquals("0", cursor.getCurrentValue());
        cursor.moveNext();
        Assertions.assertEquals("1", cursor.getCurrentValue());
        cursor.movePrevious();
        Assertions.assertEquals("0", cursor.getCurrentValue());
    }
}
