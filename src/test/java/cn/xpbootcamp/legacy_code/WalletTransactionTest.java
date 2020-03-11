package cn.xpbootcamp.legacy_code;

import cn.xpbootcamp.legacy_code.entity.User;
import cn.xpbootcamp.legacy_code.utils.RedisDistributedLock;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.transaction.InvalidTransactionException;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RedisDistributedLock.class})
class WalletTransactionTest {

    User userA = new User(1, 1000);
    User userB = new User(2, 1000);
    long productId = 001;
    String orderId = "orderId";
    double amount = 500;

    private final WalletTransaction walletTransaction = new WalletTransaction(null, userA.getId(), userB.getId(), productId, orderId, amount);

    @Test
    @Ignore
    public void should_userB_got_500_more_when_userA_transfer_500_to_it() throws InvalidTransactionException {
        PowerMockito.mockStatic(RedisDistributedLock.class);

        walletTransaction.execute();

        PowerMockito.when(RedisDistributedLock.getSingletonInstance().lock(anyString())).thenReturn(true);

        Assertions.assertEquals(500, userA.getBalance());
        Assertions.assertEquals(1500, userB.getBalance());
    }

}