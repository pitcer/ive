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

package pl.pitcer.ive.menu;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.pitcer.ive.image.loader.Reloadable;
import pl.pitcer.ive.image.loader.SortOrder;
import pl.pitcer.ive.image.loader.SortOrderable;

/**
 * Decorator for {@link ContextMenu} class
 */
public class IveContextMenu extends ContextMenu {

    private SortOrderable imageLoader;
    private Reloadable imageView;

    public IveContextMenu(final SortOrderable imageLoader, final Reloadable imageView) {
        this.imageLoader = imageLoader;
        this.imageView = imageView;
    }

    public void initialize() {
        var items = getItems();
        var reloadMenuItem = createReloadMenuItem();
        var sortMenu = createSortOrderMenu();
        items.addAll(reloadMenuItem, sortMenu);
    }

    private MenuItem createReloadMenuItem() {
        var reloadMenuItem = new MenuItem("Reload");
        reloadMenuItem.setOnAction(event -> this.imageView.reload());
        return reloadMenuItem;
    }

    private Menu createSortOrderMenu() {
        var sortOrderMenu = new Menu("Sort images by");
        var sortOrderMenuItems = sortOrderMenu.getItems();
        sortOrderMenuItems.setAll(createSortMenuItems());
        return sortOrderMenu;
    }

    private Collection<MenuItem> createSortMenuItems() {
        return Stream.of(SortOrder.values())
            .map(this::createSortOrderMenuItem)
            .collect(Collectors.toUnmodifiableList());
    }

    private MenuItem createSortOrderMenuItem(final SortOrder order) {
        var menuName = order.getMenuName();
        var menuItem = new MenuItem(menuName);
        menuItem.setOnAction(event -> {
            this.imageLoader.setSortOrder(order);
            this.imageView.reload();
        });
        return menuItem;
    }
}
