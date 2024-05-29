package kitchenpos;

import kitchenpos.eatinorders.domain.Order;
import kitchenpos.eatinorders.domain.OrderLineItem;
import kitchenpos.eatinorders.domain.OrderStatus;
import kitchenpos.eatinorders.domain.OrderTable;
import kitchenpos.eatinorders.domain.OrderType;
import kitchenpos.menus.tobe.domain.menu.Menu;
import kitchenpos.menus.tobe.domain.menugroup.MenuGroup;
import kitchenpos.menus.tobe.domain.menu.MenuName;
import kitchenpos.menus.tobe.domain.menu.MenuPrice;
import kitchenpos.menus.tobe.domain.menu.MenuProduct;
import kitchenpos.menus.tobe.domain.menugroup.MenuGroupName;
import kitchenpos.menus.tobe.domain.menu.MenuProducts;
import kitchenpos.products.tobe.domain.Product;
import kitchenpos.products.tobe.domain.ProductName;
import kitchenpos.products.tobe.domain.ProfanityChecker;
import kitchenpos.products.tobe.domain.ProductPrice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Fixtures {
    public static final UUID INVALID_ID = new UUID(0L, 0L);

    public static Menu menu() {
        return menu(19_000L, true, menuProduct());
    }

    public static Menu menu(final long price, final MenuProduct... menuProducts) {
        return menu(price, false, menuProducts);
    }

    public static Menu menu(final long price, final boolean displayed, final MenuProduct... menuProducts) {
        return new Menu(
                MenuName.from("후라이드+후라이드"),
                MenuPrice.from(price),
                menuGroup(),
                displayed,
                MenuProducts.from(Arrays.asList(menuProducts))
        );
    }

    public static MenuGroup menuGroup() {
        return menuGroup("두마리메뉴");
    }

    public static MenuGroup menuGroup(final String name) {
        return MenuGroup.from(MenuGroupName.from(name));
    }

    public static MenuProduct menuProduct() {
        return menuProduct(product(), 2L);
    }

    public static MenuProduct menuProduct(final Product product, final long quantity) {
        return new MenuProduct(new Random().nextLong(), product.getId(), ProductPrice.from(product.getPrice()), quantity);
    }

    public static Order order(final OrderStatus status, final String deliveryAddress) {
        final Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setType(OrderType.DELIVERY);
        order.setStatus(status);
        order.setOrderDateTime(LocalDateTime.of(2020, 1, 1, 12, 0));
        order.setOrderLineItems(List.of(orderLineItem()));
        order.setDeliveryAddress(deliveryAddress);
        return order;
    }

    public static Order order(final OrderStatus status) {
        final Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setType(OrderType.TAKEOUT);
        order.setStatus(status);
        order.setOrderDateTime(LocalDateTime.of(2020, 1, 1, 12, 0));
        order.setOrderLineItems(List.of(orderLineItem()));
        return order;
    }

    public static Order order(final OrderStatus status, final OrderTable orderTable) {
        final Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setType(OrderType.EAT_IN);
        order.setStatus(status);
        order.setOrderDateTime(LocalDateTime.of(2020, 1, 1, 12, 0));
        order.setOrderLineItems(List.of(orderLineItem()));
        order.setOrderTable(orderTable);
        return order;
    }

    public static OrderLineItem orderLineItem() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(new Random().nextLong());
        orderLineItem.setMenu(menu());
        return orderLineItem;
    }

    public static OrderTable orderTable() {
        return orderTable(false, 0);
    }

    public static OrderTable orderTable(final boolean occupied, final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(UUID.randomUUID());
        orderTable.setName("1번");
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setOccupied(occupied);
        return orderTable;
    }

    public static Product product() {
        return product("후라이드", 16_000L);
    }

    public static Product product(final String name, final long price) {
        return Product.from(ProductName.from(name), ProductPrice.from(price));
    }

    public static Product product(final String name, final long price, ProfanityChecker profanityChecker) {
        return Product.from(ProductName.from(name, profanityChecker), ProductPrice.from(price));
    }
}
