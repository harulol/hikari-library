package dev.hawu.plugins.api.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.CallSite;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class LambdaFactoryTest {

    public int twoParams(final int a, final int b) {
        return a + b;
    }

    public static int threeParams(final int a, final int b, final int c) {
        return a + b + c;
    }

    public void arityParams(final int... params) {
        System.out.println(Arrays.stream(params).sum());
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
        Assertions.assertEquals(3, LambdaFactory.invoke(new LambdaFactoryTest(), callSite, 1, 2));
    }

    @Test
    public void test_generateCallSiteThreeParams() throws Throwable {
        final Method method = LambdaFactoryTest.class.getDeclaredMethod("threeParams", int.class, int.class, int.class);
        LambdaFactory.registerInterface(IntTernaryOperator.class);
        final CallSite callSite = LambdaFactory.generate(method);
        Assertions.assertEquals(6, LambdaFactory.invokeStatic(callSite, 1, 2, 3));
    }

    @Test
    public void test_generateCallSiteArityParams() throws Throwable {
        final Method method = LambdaFactoryTest.class.getDeclaredMethod("arityParams", int[].class);
        final CallSite callSite = LambdaFactory.generate(method);
        LambdaFactory.invoke(new LambdaFactoryTest(), callSite, new int[]{1, 2, 3, 4, 5});
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
