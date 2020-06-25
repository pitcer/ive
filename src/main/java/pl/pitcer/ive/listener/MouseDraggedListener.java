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

package pl.pitcer.ive.listener;

import javafx.beans.value.WritableObjectValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import pl.pitcer.ive.image.Viewportable;

/**
 * Mouse dragged event listener
 */
public class MouseDraggedListener implements EventHandler<MouseEvent> {

    private static final double SHIFT_SCALAR = 0.15;

    private final Viewportable imageView;
    private WritableObjectValue<Point2D> mousePosition;

    public MouseDraggedListener(final Viewportable imageView, final WritableObjectValue<Point2D> mousePosition) {
        this.imageView = imageView;
        this.mousePosition = mousePosition;
    }

    @Override
    public void handle(final MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            var previousPosition = this.mousePosition.get();
            var previousX = previousPosition.getX();
            var previousY = previousPosition.getY();
            var x = event.getX();
            var y = event.getY();
            var dx = x - previousX;
            var dy = y - previousY;
            var viewport = getViewport(dx, dy);
            this.imageView.setViewport(viewport);
            var mousePosition = new Point2D(x, y);
            this.mousePosition.set(mousePosition);
        }
    }

    private Rectangle2D getViewport(final double dx, final double dy) {
        var previousViewport = this.imageView.getViewport();
        var x = previousViewport.getMinX();
        var y = previousViewport.getMinY();
        var width = previousViewport.getWidth();
        var height = previousViewport.getHeight();
        var primaryViewport = this.imageView.getPrimaryViewport();
        var primaryWidth = primaryViewport.getWidth();
        var primaryHeight = primaryViewport.getHeight();
        var widthZoomScalar = width / primaryWidth;
        var heightZoomScalar = height / primaryHeight;
        return new Rectangle2D(x - SHIFT_SCALAR * widthZoomScalar * dx, y - SHIFT_SCALAR * heightZoomScalar * dy, width, height);
    }
}
