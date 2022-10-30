package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_MENU_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;
import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    public Order create(final OrderRequest request) {
        final Order order = request.toOrder();
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        validSize(orderLineItems);
        final Order saveConditionOrder = convertSaveConditionOrder(request.getOrderTableId(), order);
        return orderDao.save(saveConditionOrder);
    }

    private Order convertSaveConditionOrder(final Long orderTableId, final Order order) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        return new Order(orderTable.getTableGroupId(), COOKING.name(), LocalDateTime.now(),
                order.getOrderLineItems());
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));
    }

    private void validSize(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new CustomIllegalArgumentException(NOT_FOUND_MENU_EXCEPTION);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderDao.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        orderDao.save(savedOrder);
        return savedOrder;
    }
}
