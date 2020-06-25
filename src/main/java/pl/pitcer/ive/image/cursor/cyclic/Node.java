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

import org.jetbrains.annotations.Nullable;

/**
 * Representation of {@link CyclicCursor} tape nodes
 *
 * @param <T> type of value stored in the node
 */
final class Node<T> {

    @Nullable
    private Node<T> next;
    @Nullable
    private Node<T> previous;
    private T value;

    Node(final T value) {
        this.value = value;
    }

    public boolean hasNext() {
        return this.next != null;
    }

    @Nullable
    public Node<T> getNext() {
        return this.next;
    }

    public void setNext(@Nullable final Node<T> next) {
        this.next = next;
    }

    public boolean hasPrevious() {
        return this.previous != null;
    }

    @Nullable
    public Node<T> getPrevious() {
        return this.previous;
    }

    public void setPrevious(@Nullable final Node<T> previous) {
        this.previous = previous;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(final T value) {
        this.value = value;
    }
}
