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

package pl.pitcer.ive;

import java.nio.file.Path;
import java.util.Set;
import javafx.application.Application;
import javafx.beans.value.WritableStringValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.pitcer.ive.image.ImageLoader;
import pl.pitcer.ive.image.IveImageView;
import pl.pitcer.ive.listener.KeyPressedListener;

public class IveApplication extends Application {

    public static final String WINDOW_TITLE = "Ive";

    private static final IconLoader ICON_LOADER = createIconLoader();

    private static IconLoader createIconLoader() {
        var iconSizes = Set.of("16", "24", "32", "48", "64", "128", "256", "512");
        return new IconLoader("png", iconSizes);
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle(WINDOW_TITLE);
        var windowTitle = primaryStage.titleProperty();
        var scene = createScene(windowTitle);
        primaryStage.setScene(scene);
        addIcons(primaryStage);
        primaryStage.show();
    }

    private static Scene createScene(final WritableStringValue windowTitle) {
        var imageView = createImageView(windowTitle);
        var group = new Group(imageView);
        var scene = new Scene(group);
        var keyListener = new KeyPressedListener(imageView);
        scene.setOnKeyPressed(keyListener);
        return scene;
    }

    private static IveImageView createImageView(final WritableStringValue windowTitle) {
        var imageLoader = new ImageLoader();
        var imageView = new IveImageView(imageLoader, windowTitle);
        imageView.loadImages();
        imageView.showNextImage();
        return imageView;
    }

    private static void addIcons(final Stage primaryStage) {
        Path iconPath = Path.of("icons", "ive");
        var loadedIcons = ICON_LOADER.loadFromResources(iconPath);
        var stageIcons = primaryStage.getIcons();
        stageIcons.addAll(loadedIcons);
    }
}
