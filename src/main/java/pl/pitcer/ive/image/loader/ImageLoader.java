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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class responsible for loading image files from working directory
 */
public final class ImageLoader implements SortOrderable {

    private static final FileFilter IMAGE_FILTER = new ImageFilter();
    private static final File CURRENT_DIRECTORY = getCurrentDirectory();

    private static File getCurrentDirectory() {
        var currentDirectoryPath = System.getProperty("user.dir");
        return new File(currentDirectoryPath);
    }

    private SortOrder sortOrder;

    public ImageLoader(final SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<File> loadImages() {
        var files = CURRENT_DIRECTORY.listFiles(IMAGE_FILTER);
        var comparator = this.sortOrder.getComparator();
        return Stream.of(files)
            .sorted(comparator)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public SortOrder getSortOrder() {
        return this.sortOrder;
    }

    @Override
    public void setSortOrder(final SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
