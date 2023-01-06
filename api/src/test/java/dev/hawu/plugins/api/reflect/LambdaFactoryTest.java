package dev.hawu.plugins.api.reflect;

import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class LambdaFactoryTest {

    @Test
    public void test_getInvokedNameRunnable() {
        assert "run".equals(LambdaFactory.getInvokedName(Runnable.class));
    }

    @Test
    public void test_getInvokedNameConsumer() {
        assert "accept".equals(LambdaFactory.getInvokedName(Consumer.class));
    }

    @Test
    public void test_getInvokedNameFunction() {
        assert "apply".equals(LambdaFactory.getInvokedName(Function.class));
    }

    @Test
    public void test_getInvokedNameBiConsumer() {
        assert "accept".equals(LambdaFactory.getInvokedName(BiConsumer.class));
    }

    @Test
    public void test_getInvokedNameRandomTest() {
        assert "oneFunction".equals(LambdaFactory.getInvokedName(RandomTest.class));
    }

    @FunctionalInterface
    interface RandomTest {

        void oneFunction(int oneParameter);

        default int defaultFunction(int parameter) {
            return parameter + 1;
        }

        boolean equals(Object obj);

        int hashCode();

    }

}
