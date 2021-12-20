import com.market.domain.Item;
import com.market.domain.Order;
import com.market.domain.User;
import com.market.service.PurchaseStatus;
import com.market.service.ShopService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShopServiceTest {
    private Map<Item, Long> goods = new HashMap<>();

    @AfterEach
    public void cleanup() {
        goods.clear();
    }

    @Test
    public void shouldSuccessfullyMakePurchase() {
        goods.put(TestData.validItem, 1L);
        ShopService shop = new ShopService(getItemsQuantity());
        User user = new User("John", 200d);
        Order order = new Order(user, Arrays.asList(TestData.validItem));

        PurchaseStatus status = shop.makePurchase(order);

        assertEquals(status, PurchaseStatus.OK,
                "Incorrect status after purchase");
    }

    @Test
    public void shouldCorrectlyDecreaseBalance() {
        goods.put(TestData.validItem, 2L);
        ShopService shop = new ShopService(getItemsQuantity());

        double userInitialBalance = 200d;
        User user = new User("John", userInitialBalance);

        List<Item> itemsInOrder = Arrays.asList(TestData.validItem, TestData.validItem);
        Order order = new Order(user, itemsInOrder);

        double itemsTotalPrice = TestData.validItem.getCost() * 2;
        double expectedUserBalance = roundAvoid(userInitialBalance - itemsTotalPrice, 2);

        shop.makePurchase(order);

        assertEquals(user.getBalance(), expectedUserBalance,
                "User has incorrect balance");
    }

    private Map<Long, Long> getItemsQuantity() {
        return goods.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getId(), Map.Entry::getValue));
    }

    private double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
