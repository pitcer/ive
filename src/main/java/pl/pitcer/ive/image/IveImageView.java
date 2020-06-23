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
import java.util.ListIterator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.pitcer.ive.image.list.CyclicListIterator;
import pl.pitcer.ive.window.Resizable;
import pl.pitcer.ive.window.Titled;

public final class IveImageView extends ImageView implements ImageDisplay {

    private final ImageLoader imageLoader;
    private final Titled titledWindow;
    private final Resizable resizableWindow;
    private ListIterator<File> imagesIterator;

    public IveImageView(final ImageLoader imageLoader, final Titled titledWindow, final Resizable resizableWindow) {
        this.imageLoader = imageLoader;
        this.titledWindow = titledWindow;
        this.resizableWindow = resizableWindow;
    }

    public void loadImages() {
        var images = this.imageLoader.loadImages();
        this.imagesIterator = new CyclicListIterator<>(images);
    }

    @Override
    public void showNextImage() {
        var file = this.imagesIterator.next();
        setImage(file);
    }

    @Override
    public void showPreviousImage() {
        var file = this.imagesIterator.previous();
        setImage(file);
    }

    private void setImage(final File file) {
        try (var imageInputStream = new FileInputStream(file)) {
            var image = new Image(imageInputStream);
            setImage(image);
            var imageName = file.getName();
            this.titledWindow.setTitleSuffix(" - " + imageName);
            this.resizableWindow.adjustSize();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }
}
