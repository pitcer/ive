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
import java.util.Comparator;

public enum SortOrder {

    NAME_ASCENDING("A-Z", Comparators.NAME_COMPARATOR),
    NAME_DESCENDING("Z-A", Comparators.NAME_COMPARATOR.reversed()),
    MODIFICATION_TIME_ASCENDING("First Modified", Comparators.TIME_COMPARATOR),
    MODIFICATION_TIME_DESCENDING("Last Modified", Comparators.TIME_COMPARATOR.reversed()),
    SIZE_ASCENDING("Smallest", Comparators.SIZE_COMPARATOR),
    SIZE_DESCENDING("Largest", Comparators.SIZE_COMPARATOR.reversed());

    private String menuName;

    private Comparator<File> comparator;

    SortOrder(final String menuName, final Comparator<File> comparator) {
        this.menuName = menuName;
        this.comparator = comparator;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public Comparator<File> getComparator() {
        return this.comparator;
    }

    private static final class Comparators {

        private static final Comparator<File> NAME_COMPARATOR = Comparator.comparing(File::getName);
        private static final Comparator<File> TIME_COMPARATOR = Comparator.comparingLong(File::lastModified);
        private static final Comparator<File> SIZE_COMPARATOR = Comparator.comparingLong(File::length);
    }
}
