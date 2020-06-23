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

import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pl.pitcer.ive.image.ImageDisplay;
import pl.pitcer.ive.image.loader.Reloadable;
import pl.pitcer.ive.window.FullScreenable;

public final class KeyPressedListener implements EventHandler<KeyEvent> {

    private final ImageDisplay imageDisplay;
    private final FullScreenable windowInFullScreen;
    private final Reloadable reloadableImageView;
    private final Map<KeyCode, Runnable> handlers;

    public KeyPressedListener(final ImageDisplay imageDisplay, final Reloadable reloadableImageView, final FullScreenable windowInFullScreen) {
        this.imageDisplay = imageDisplay;
        this.windowInFullScreen = windowInFullScreen;
        this.reloadableImageView = reloadableImageView;
        this.handlers = createHandlers();
    }

    private Map<KeyCode, Runnable> createHandlers() {
        return Map.ofEntries(
            Map.entry(KeyCode.RIGHT, this::handleNextKey),
            Map.entry(KeyCode.LEFT, this::handlePreviousKey),
            Map.entry(KeyCode.F11, this::handleFullScreen),
            Map.entry(KeyCode.F5, this::handleReload)
        );
    }

    @Override
    public void handle(final KeyEvent event) {
        var keyCode = event.getCode();
        var handler = this.handlers.get(keyCode);
        if (handler == null) {
            return;
        }
        handler.run();
    }

    private void handleNextKey() {
        this.imageDisplay.showNextImage();
    }

    private void handlePreviousKey() {
        this.imageDisplay.showPreviousImage();
    }

    private void handleFullScreen() {
        this.windowInFullScreen.toggleFullScreen();
    }

    private void handleReload() {
        this.reloadableImageView.reload();
    }
}
