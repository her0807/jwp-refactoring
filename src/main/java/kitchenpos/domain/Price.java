package kitchenpos.domain;

import static kitchenpos.application.exception.ExceptionType.INVALID_PRICE_EXCEPTION;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.application.exception.CustomIllegalArgumentException;

public class Price {

    private BigDecimal value;

    public Price() {
    }

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomIllegalArgumentException(INVALID_PRICE_EXCEPTION);
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
