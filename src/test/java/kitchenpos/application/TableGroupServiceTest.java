package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.INVALID_TABLE_UNGROUP_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.ui.dto.request.TableGroupRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TableGroup 회식 때 처럼 여러 테이블을 한꺼번에 계산해야할 때, 해당 테이블은 한 그룹이다 명시하는 느낌의 도메인입니다. `생성 시간`과 `테이블들` 을 가지고 있습니다.
 *
 * <br>
 * - `create`(테이블 그룹을 생성합니다.)
 * <br>
 * - 테이블 List 와 DB 에서 찾아온  찾아온 테이블 객체의 수가 일치해야합니다.
 * <br>
 * - `ungroup`(테이블 그룹을 해지합니다.)
 * <br>
 * - `주문 상태가 준비중`이나, `식사중`이면 `예외`를 반환합니다.
 */
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    /**
     * public TableGroupResponse create(final TableGroupRequest request) { final List<OrderTable> savedOrderTables =
     * getRequestOrderTables(request.getOrderTables()); validateSize(request.getOrderTables(), savedOrderTables); final
     * TableGroup tableGroup = convertSavaTableGroup(savedOrderTables); final TableGroup saveTableGroup =
     * tableGroupDao.save(tableGroup); return TableGroupResponse.from(saveTableGroup); }
     */

    @Test
    void 존재하지_않는_테이블로_그룹을_생성하면_예외를_반환한다() {
        final TableGroupRequest request = new TableGroupRequest(Arrays.asList(테이블_요청_생성(1L), 테이블_요청_생성(2L)));

        Assertions.assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_FOUND_TABLE_EXCEPTION.getMessage());
    }

    @Test
    void 조리중_일때_그룹을_해지하면_예외를_반환한다() {
        그룹_내_주문_상태를_진행중으로_설정();
        그룹_id로_조회시_두개_반환하도록_세팅();

        Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_TABLE_UNGROUP_EXCEPTION.getMessage());
    }
}
