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

import java.util.NoSuchElementException;
import org.jetbrains.annotations.Nullable;
import pl.pitcer.ive.image.cursor.MutableCursor;

public final class CyclicCursor<T> implements MutableCursor<T> {

    private static final int RESET_INDEX = -1;

    @Nullable
    private Node<T> firstNode;
    @Nullable
    private Node<T> lastNode;
    @Nullable
    private Node<T> currentNode;
    private int currentIndex;
    private int size;

    public CyclicCursor(final Iterable<? extends T> values) {
        this(values, RESET_INDEX);
    }

    public CyclicCursor(final Iterable<? extends T> values, final int startingIndex) {
        this.currentNode = null;
        int elementsCount = 0;
        Node<T> firstNode = null;
        Node<T> currentNode = null;
        for (final T element : values) {
            Node<T> node = new Node<>(element);
            if (firstNode == null) {
                firstNode = node;
            } else {
                currentNode.setNext(node);
                node.setPrevious(currentNode);
            }
            currentNode = node;
            if (elementsCount == startingIndex) {
                this.currentNode = node;
            }
            elementsCount++;
        }
        this.firstNode = firstNode;
        this.lastNode = currentNode;
        this.currentIndex = startingIndex;
        this.size = elementsCount;
    }

    @Override
    public boolean hasNext() {
        return isReset() && this.firstNode != null || this.currentNode != null && this.currentNode.hasNext();
    }

    @Override
    public void moveNext() {
        if (isReset()) {
            this.currentNode = this.firstNode;
        } else if (this.currentNode != null && !isLast()) {
            this.currentNode = this.currentNode.getNext();
        } else {
            this.currentNode = this.firstNode;
        }
        this.currentIndex = Math.floorMod(this.currentIndex + 1, this.size);
    }

    @Override
    public boolean hasPrevious() {
        return isReset() && this.lastNode != null || this.currentNode != null && this.currentNode.hasPrevious();
    }

    @Override
    public void movePrevious() {
        if (isReset()) {
            this.currentNode = this.lastNode;
        } else if (this.currentNode != null && !isFirst()) {
            this.currentNode = this.currentNode.getPrevious();
        } else {
            this.currentNode = this.lastNode;
        }
        this.currentIndex = Math.floorMod(this.currentIndex - 1, this.size);
    }

    @Override
    public T getCurrentValue() {
        if (isReset()) {
            throw new NoSuchElementException("Cursor at the reset point has no value");
        }
        if (this.currentNode == null) {
            throw new NoSuchElementException("Cursor at a non-existent node has no value");
        }
        return this.currentNode.getValue();
    }

    @Override
    public int getIndex() {
        return this.currentIndex;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void reset() {
        this.currentIndex = RESET_INDEX;
    }

    @Override
    public void set(final T value) {
        if (this.currentNode != null) {
            this.currentNode.setValue(value);
        }
    }

    @Override
    public void remove() {
        if (this.currentNode != null) {
            Node<T> next = this.currentNode.getNext();
            Node<T> previous = this.currentNode.getPrevious();
            if (next != null) {
                next.setPrevious(previous);
            }
            if (previous != null) {
                previous.setNext(next);
            }
            if (isFirst()) {
                this.firstNode = next;
            }
            if (isLast()) {
                this.lastNode = previous;
            }
        }
    }

    private boolean isFirst() {
        return this.currentIndex == 0;
    }

    private boolean isLast() {
        return this.currentIndex == this.size - 1;
    }

    private boolean isReset() {
        return this.currentIndex == RESET_INDEX;
    }
}
