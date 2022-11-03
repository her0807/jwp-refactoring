package kitchenpos.application;

import static kitchenpos.exception.ExceptionType.NOT_FOUND_MENU_EXCEPTION;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.ui.request.OrderRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Order 매장에서 발생하는 주문입니다.  `테이블 번호`, `주문 상태`, `주문 시간`, 주문에 속하는 수량이 있는 `메뉴`를 가지고 있습니다.
 * <p>
 * `create`(주문을 생성할 수 있습니다.) 주문 메뉴가 *null* 이면 예외를 반환합니다.
 * <p>
 * `없는 메뉴를 주문한 경우` 예외를 반환합니다.
 * <p>
 * 주문한 *테이블이 존재하지 않으면* 예외를 반환합니다.
 * <p>
 * `findAll` (전체 주문을 조회할 수 있습니다.)
 * <p>
 * `changeOrderStatus` (주문 상태를 변경할 수 있습니다.)
 * <p>
 * `없는 주문의 상태를 변경`하려고 하면 예외를 반환합니다.
 * <p>
 * `Completion 상태의 주문`인데, 변경을 시도할 경우 예외를 반환합니다.
 */

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void 주문메뉴가_null_이면_예외를_반환한다() {
        final OrderRequest 주문 = new OrderRequest(1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                new ArrayList<>());

        Assertions.assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 없는_메뉴를_주문_할_경우_예외를_반환한다() {
        final OrderRequest 주문 = 주문_요청_생성(OrderStatus.COOKING);

        메뉴_리스트_세팅(0L);

        Assertions.assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_FOUND_MENU_EXCEPTION.getMessage());
    }
}
