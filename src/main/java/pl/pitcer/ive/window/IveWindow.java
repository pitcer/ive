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
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pl.pitcer.ive.IconLoader;
import pl.pitcer.ive.image.IveImageView;
import pl.pitcer.ive.image.loader.ImageLoader;
import pl.pitcer.ive.image.loader.SortOrder;
import pl.pitcer.ive.listener.KeyPressedListener;
import pl.pitcer.ive.listener.MouseDraggedListener;
import pl.pitcer.ive.listener.MousePressedListener;
import pl.pitcer.ive.listener.ScrollListener;
import pl.pitcer.ive.menu.IveContextMenu;

/**
 * Wrapper for {@link Stage} class
 */
public final class IveWindow implements Titled, FullScreenable, Resizable {

    private static final Path IVE_ICON_PATH = Path.of("icons", "ive");
    private static final Path STYLESHEET_PATH = Path.of("styles", "stylesheet.css");
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
    }

    private Scene createScene() {
        var imageView = createImageView();
        var pane = new StackPane(imageView);
        pane.setAlignment(Pos.CENTER);
        var scene = new Scene(pane);
        var stylesheets = scene.getStylesheets();
        var stylesheetUrl = getStylesheetUrl();
        stylesheets.add(stylesheetUrl);
        setListeners(scene, imageView);
        return scene;
    }

    private void setListeners(final Scene scene, final IveImageView imageView) {
        var keyListener = new KeyPressedListener(imageView, imageView, this);
        var scrollListener = new ScrollListener(imageView);
        var mousePosition = new SimpleObjectProperty<>(Point2D.ZERO);
        var mousePressedListener = new MousePressedListener(mousePosition);
        var mouseDraggedListener = new MouseDraggedListener(imageView, mousePosition);
        scene.setOnKeyPressed(keyListener);
        scene.setOnScroll(scrollListener);
        scene.setOnMousePressed(mousePressedListener);
        scene.setOnMouseDragged(mouseDraggedListener);
    }

    private IveImageView createImageView() {
        var imageLoader = new ImageLoader(SortOrder.NAME_ASCENDING);
        var imageView = new IveImageView(imageLoader, this, this);
        var contextMenu = new IveContextMenu(imageLoader, imageView);
        contextMenu.initialize();
        bindProperties(imageView);
        imageView.setOnContextMenuRequested(event -> contextMenu.show(this.stage, event.getScreenX(), event.getScreenY()));
        imageView.loadImages();
        imageView.showNextImage();
        return imageView;
    }

    private void bindProperties(final IveImageView imageView) {
        var imageViewFitWidth = imageView.fitWidthProperty();
        var imageViewFitHeight = imageView.fitHeightProperty();
        var stageWidthProperty = this.stage.widthProperty();
        var stageHeightProperty = this.stage.heightProperty();
        imageViewFitWidth.bind(stageWidthProperty);
        imageViewFitHeight.bind(stageHeightProperty);
    }

    private String getStylesheetUrl() {
        var path = STYLESHEET_PATH.toString();
        var stylesheetUrl = ClassLoader.getSystemResource(path);
        return stylesheetUrl.toExternalForm();
    }

    private void addIcons() {
        var loadedIcons = ICON_LOADER.loadFromResources(IVE_ICON_PATH);
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
    public void setMinimumWidth(final double width) {
        var bounds = getScreenBounds();
        var screenWidth = bounds.getWidth();
        var minimumWidth = Math.min(width, screenWidth * 0.9);
        this.stage.setMinWidth(minimumWidth);
    }

    @Override
    public void setMinimumHeight(final double height) {
        var bounds = getScreenBounds();
        var screenHeight = bounds.getHeight();
        var minimumHeight = Math.min(height, screenHeight * 0.9);
        this.stage.setMinHeight(minimumHeight);
    }

    private Rectangle2D getScreenBounds() {
        var screen = Screen.getPrimary();
        return screen.getBounds();
    }

    @Override
    public void center() {
        this.stage.centerOnScreen();
    }
}
