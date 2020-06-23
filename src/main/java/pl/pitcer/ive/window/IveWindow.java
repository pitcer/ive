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

package pl.pitcer.ive.window;

import java.nio.file.Path;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import pl.pitcer.ive.IconLoader;
import pl.pitcer.ive.image.IveImageView;
import pl.pitcer.ive.image.loader.ImageLoader;
import pl.pitcer.ive.image.loader.SortOrder;
import pl.pitcer.ive.listener.KeyPressedListener;
import pl.pitcer.ive.menu.IveContextMenu;

public final class IveWindow implements Titled, FullScreenable, Resizable {

    private static final String TITLE = "Ive";
    private static final IconLoader ICON_LOADER = createIconLoader();

    private static IconLoader createIconLoader() {
        var iconSizes = Set.of("16", "24", "32", "48", "64", "128", "256", "512");
        return new IconLoader("png", iconSizes);
    }

    private Stage stage;

    public IveWindow(final Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        setupProperties();
        var scene = createScene();
        this.stage.setScene(scene);
        addIcons();
    }

    private void setupProperties() {
        this.stage.setTitle(TITLE);
        this.stage.setFullScreenExitHint(null);
        this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.stage.sizeToScene();
    }

    private Scene createScene() {
        var imageView = createImageView();
        var group = new Group(imageView);
        var scene = new Scene(group);
        var keyListener = new KeyPressedListener(imageView, this);
        scene.setOnKeyPressed(keyListener);
        return scene;
    }

    private IveImageView createImageView() {
        var imageLoader = new ImageLoader(SortOrder.NAME_ASCENDING);
        var imageView = new IveImageView(imageLoader, this, this);
        var contextMenu = new IveContextMenu(imageLoader, imageView);
        contextMenu.initialize();
        imageView.setOnContextMenuRequested(event -> contextMenu.show(this.stage, event.getScreenX(), event.getScreenY()));
        imageView.loadImages();
        imageView.showNextImage();
        return imageView;
    }

    private void addIcons() {
        Path iconPath = Path.of("icons", "ive");
        var loadedIcons = ICON_LOADER.loadFromResources(iconPath);
        var stageIcons = this.stage.getIcons();
        stageIcons.addAll(loadedIcons);
    }

    public void show() {
        this.stage.show();
    }

    @Override
    public String getTitle() {
        return this.stage.getTitle();
    }

    @Override
    public void setTitleSuffix(final String suffix) {
        this.stage.setTitle(TITLE + suffix);
    }

    @Override
    public boolean isFullScreen() {
        return this.stage.isFullScreen();
    }

    @Override
    public void toggleFullScreen() {
        boolean fullScreen = this.stage.isFullScreen();
        this.stage.setFullScreen(!fullScreen);
    }

    @Override
    public void adjustSize() {
        this.stage.sizeToScene();
        this.stage.centerOnScreen();
    }
}
