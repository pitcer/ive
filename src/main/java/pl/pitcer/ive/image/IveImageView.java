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

package pl.pitcer.ive.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.pitcer.ive.image.cursor.Cursor;
import pl.pitcer.ive.image.cursor.cyclic.CyclicCursor;
import pl.pitcer.ive.image.loader.ImageLoader;
import pl.pitcer.ive.image.loader.Reloadable;
import pl.pitcer.ive.window.Resizable;
import pl.pitcer.ive.window.Titled;

public final class IveImageView extends ImageView implements ImageDisplay, Reloadable, Viewportable {

    private final ImageLoader imageLoader;
    private final Titled titledWindow;
    private final Resizable resizableWindow;
    private Cursor<File> imagesCursor;

    public IveImageView(final ImageLoader imageLoader, final Titled titledWindow, final Resizable resizableWindow) {
        this.imageLoader = imageLoader;
        this.titledWindow = titledWindow;
        this.resizableWindow = resizableWindow;
        setPreserveRatio(true);
        setSmooth(true);
    }

    public void loadImages() {
        var images = this.imageLoader.loadImages();
        this.imagesCursor = new CyclicCursor<>(images);
    }

    @Override
    public void showNextImage() {
        this.imagesCursor.moveNext();
        var file = this.imagesCursor.getCurrentValue();
        setImage(file);
    }

    @Override
    public void showPreviousImage() {
        this.imagesCursor.movePrevious();
        var file = this.imagesCursor.getCurrentValue();
        setImage(file);
    }

    @Override
    public void reload() {
        loadImages();
    }

    private void setImage(final File file) {
        try (var imageInputStream = new FileInputStream(file)) {
            var image = new Image(imageInputStream);
            setImage(image);
            var imageName = file.getName();
            this.titledWindow.setTitleSuffix(" - " + imageName);
            resizeWindow(image);
            var viewport = toRectangle(image);
            setViewport(viewport);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private void resizeWindow(final Image image) {
        double width = image.getWidth();
        double height = image.getHeight();
        this.resizableWindow.setMinimumWidth(width);
        this.resizableWindow.setMinimumHeight(height);
        this.resizableWindow.center();
    }

    private static Rectangle2D toRectangle(final Image image) {
        double width = image.getWidth();
        double height = image.getHeight();
        return new Rectangle2D(0, 0, width, height);
    }
}
