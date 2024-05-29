package kitchenpos.menus.tobe.domain;

import kitchenpos.menus.application.FakeProductClient;
import kitchenpos.menus.dto.MenuProductCreateRequest;
import kitchenpos.menus.tobe.domain.menu.MenuProducts;
import kitchenpos.menus.tobe.domain.menu.ProductClient;
import kitchenpos.products.application.InMemoryProductRepository;
import kitchenpos.products.tobe.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static kitchenpos.Fixtures.INVALID_ID;
import static kitchenpos.Fixtures.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴구성상품 목록")
class MenuProductsTest {
    private static MenuProductCreateRequest menuProductA;

    List<MenuProductCreateRequest> menuProductsRequest;
    private ProductClient productClient;

    @BeforeEach
    void setUp() {
        ProductRepository productRepository = new InMemoryProductRepository();
        productClient = new FakeProductClient(productRepository);

        UUID productA_ID = productRepository.save(product("후라이드치킨", 10_000)).getId();
        UUID productB_ID = productRepository.save(product("양념치킨", 12_000)).getId();
        menuProductA = new MenuProductCreateRequest(productA_ID, 1L);
        MenuProductCreateRequest menuProductB = new MenuProductCreateRequest(productB_ID, 2L);
        menuProductsRequest = List.of(menuProductA, menuProductB);
    }

    @DisplayName("[성공] 이미 생성된 1개이상의 메뉴구성상품으로 구성된 메뉴구성상품 목록을 생성한다.")
    @Test
    void create() {
        MenuProducts actual = MenuProducts.from(menuProductsRequest, productClient);

        assertAll(
                () -> assertThat(actual).isEqualTo(MenuProducts.from(menuProductsRequest, productClient)),
                () -> assertThat(actual.getValues()).hasSize(2)
        );
    }

    @DisplayName("[실패] 메뉴구성상품 목록을 생성할 때, 메뉴구성상품이 1개 이상 구성되어야 한다.")
    @MethodSource("nullOrEmptyMenuProducts")
    @ParameterizedTest
    void fail_create(final List<MenuProductCreateRequest> menuProducts) {
        assertThatThrownBy(() -> MenuProducts.from(menuProducts, productClient))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("[실패] 메뉴구성상품 목록을 생성할 때, 생성되지 않은 상품이거나 같은상품을 중복해서 구성하지 않는다.")
    @MethodSource("invalidMenuProducts")
    @ParameterizedTest
    void fail2_create(final List<MenuProductCreateRequest> menuProducts) {
        assertThatThrownBy(() -> MenuProducts.from(menuProducts, productClient))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[성공] 메뉴구성상품 목록의 가격의 총합을 구할 수 있다.")
    @Test
    void sumMenuProductsPrice() {
        MenuProducts menuProducts = MenuProducts.from(menuProductsRequest, productClient);

        BigDecimal actual = menuProducts.sumPrice();

        assertThat(actual).isEqualTo(BigDecimal.valueOf(34_000));
    }

    private static List<Arguments> nullOrEmptyMenuProducts() {
        return Arrays.asList(
                null,
                Arguments.of(Collections.emptyList()),
                Arguments.of(List.of(new MenuProductCreateRequest(INVALID_ID, 2L)))
        );
    }

    private static List<Arguments> invalidMenuProducts() {
        return List.of(
                Arguments.of(List.of(new MenuProductCreateRequest(INVALID_ID, 2L))),
                Arguments.of(List.of(menuProductA, menuProductA, menuProductA))
        );
    }
}