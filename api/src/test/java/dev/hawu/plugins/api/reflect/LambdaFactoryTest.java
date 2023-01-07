package dev.hawu.plugins.api.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.CallSite;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

public class LambdaFactoryTest {

    public int twoParams(final int a, final int b) {
        return a + b;
    }

    public int threeParams(final int a, final int b, final int c) {
        return a + b + c;
    }

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
        assert "oneFunction".equals(LambdaFactory.getInvokedName(IntTernaryOperator.class));
    }

    @Test
    public void test_generateCallSiteTwoParams() throws Throwable {
        final Method method = LambdaFactoryTest.class.getDeclaredMethod("twoParams", int.class, int.class);
        final CallSite callSite = LambdaFactory.generate(method);

        IntBinaryOperator consumer = (IntBinaryOperator) callSite.getTarget().bindTo(new LambdaFactoryTest()).invoke();
        Assertions.assertEquals(3, consumer.applyAsInt(1, 2));
    }

    @Test
    public void test_generateCallSiteThreeParams() throws Throwable {
        final Method method = LambdaFactoryTest.class.getDeclaredMethod("threeParams", int.class, int.class, int.class);
        LambdaFactory.registerInterface(IntTernaryOperator.class);
        final CallSite callSite = LambdaFactory.generate(method);

        IntTernaryOperator consumer = (IntTernaryOperator) callSite.getTarget().bindTo(new LambdaFactoryTest()).invoke();
        Assertions.assertEquals(6, consumer.oneFunction(1, 2, 3));
    }

    @FunctionalInterface
    interface IntTernaryOperator {

        int oneFunction(int p1, int p2, int p3);

        default int defaultFunction(int parameter) {
            return parameter + 1;
        }

        boolean equals(Object obj);

        int hashCode();

    }

}
