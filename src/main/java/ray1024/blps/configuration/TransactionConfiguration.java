package ray1024.blps.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class TransactionConfiguration {
    private JtaTransactionManager jtaTransactionManager() {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManagerName("java:jboss/TransactionManager");
        transactionManager.setUserTransactionName("java:jboss/UserTransaction");
        transactionManager.setTransactionSynchronizationRegistryName("java:jboss/TransactionSynchronizationRegistry");
        transactionManager.setAllowCustomIsolationLevels(true);
        transactionManager.afterPropertiesSet();
        return transactionManager;
    }
}
