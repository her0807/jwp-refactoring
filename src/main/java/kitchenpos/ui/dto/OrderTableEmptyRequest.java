package kitchenpos.ui.dto;

public class OrderTableEmptyRequest {
    private boolean empty;

    public OrderTableEmptyRequest() {
    }

    public OrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

