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

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.ScrollEvent;
import pl.pitcer.ive.image.Viewportable;

public class ScrollListener implements EventHandler<ScrollEvent> {

    private static final double ZOOM_SCALAR = 1.1;
    private static final double INVERSE_ZOOM_SCALAR = 1 / ZOOM_SCALAR;

    private Viewportable imageView;

    public ScrollListener(final Viewportable imageView) {
        this.imageView = imageView;
    }

    @Override
    public void handle(final ScrollEvent event) {
        var previousViewport = this.imageView.getViewport();
        var delta = event.getDeltaY();
        var viewport = getViewport(previousViewport, delta);
        this.imageView.setViewport(viewport);
    }

    private Rectangle2D getViewport(final Rectangle2D previousViewport, final double scrollDelta) {
        if (scrollDelta < 0) {
            return getViewport(previousViewport, ZOOM_SCALAR, -1);
        } else if (scrollDelta == 0) {
            return previousViewport;
        } else {
            return getViewport(previousViewport, INVERSE_ZOOM_SCALAR, 1);
        }
    }

    private Rectangle2D getViewport(final Rectangle2D previousViewport, final double scalar, final int sign) {
        var x = previousViewport.getMinX();
        var y = previousViewport.getMinY();
        var width = previousViewport.getWidth();
        var height = previousViewport.getHeight();
        var dx = Math.abs(width * scalar - width) / 2;
        var dy = Math.abs(height * scalar - height) / 2;
        return new Rectangle2D(x + sign * dx, y + sign * dy, width * scalar, height * scalar);
    }
}
