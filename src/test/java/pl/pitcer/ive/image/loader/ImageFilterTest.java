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

package pl.pitcer.ive.image.loader;

import java.io.File;
import java.io.FileFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.pitcer.ive.Resources;

public class ImageFilterTest {

    private final FileFilter imageFilter = new ImageFilter();

    @Test
    public void testDirectoryFile() {
        File directory = new File(".");
        boolean result = this.imageFilter.accept(directory);
        Assertions.assertFalse(result);
    }

    @Test
    public void testNotExistedNonImageFile() {
        File nonImageFile = new File("test.txt");
        boolean result = this.imageFilter.accept(nonImageFile);
        Assertions.assertFalse(result);
    }

    @Test
    public void testExistedNonImageFile() {
        File nonImageFile = Resources.getResource("image/non-image.txt");
        boolean result = this.imageFilter.accept(nonImageFile);
        Assertions.assertFalse(result);
    }

    @Test
    public void testNotExistedPngImageFile() {
        File imageFile = new File("image.png");
        boolean result = this.imageFilter.accept(imageFile);
        Assertions.assertFalse(result);
    }

    @Test
    public void testExistedPngImageFile() {
        File imageFile = Resources.getResource("image/image.png");
        boolean result = this.imageFilter.accept(imageFile);
        Assertions.assertTrue(result);
    }
}
