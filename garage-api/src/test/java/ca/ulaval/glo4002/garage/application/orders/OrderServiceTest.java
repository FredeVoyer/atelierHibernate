package ca.ulaval.glo4002.garage.application.orders;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.garage.application.orders.dto.OrderMapper;
import ca.ulaval.glo4002.garage.application.orders.dto.OrderSummary;
import ca.ulaval.glo4002.garage.domain.orders.Order;
import ca.ulaval.glo4002.garage.domain.orders.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderMapper orderMapper;

    @InjectMocks
    OrderService orderService;

    @Test
    void listOrders_fetchesAllOrdersFromRepository() {
        orderService.listOrders();

        verify(orderRepository).findAll();
    }

    @Test
    void listOrders_transformsAllOrdersToSummaries() {
        List<Order> savedOrders = givenOrdersExist();
        List<OrderSummary> expectedSummaries = new ArrayList<>();
        willReturn(expectedSummaries).given(orderMapper).toDto(savedOrders);

        List<OrderSummary> actualSummaries = orderService.listOrders();

        assertSame(expectedSummaries, actualSummaries);
    }

    private List<Order> givenOrdersExist() {
        List<Order> savedOrders = new ArrayList<>();
        willReturn(savedOrders).given(orderRepository).findAll();
        return savedOrders;
    }
}