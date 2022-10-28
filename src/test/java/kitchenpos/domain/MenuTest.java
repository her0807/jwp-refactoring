package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MenuTest {
    @Test
    void create() {
        assertThatThrownBy(() ->
                new Menu(
                        "후라이드",
                        BigDecimal.valueOf(1100L),
                        1L,
                        Arrays.asList(new MenuProduct(1L, 1, BigDecimal.valueOf(1000L))))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_price() {
        assertThatThrownBy(() ->
                new Menu(
                        "후라이드",
                        BigDecimal.valueOf(-1L),
                        1L,
                        Arrays.asList(new MenuProduct(1L, 1, BigDecimal.valueOf(1000L))))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}