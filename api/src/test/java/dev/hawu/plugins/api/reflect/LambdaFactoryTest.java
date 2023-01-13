package dev.hawu.plugins.api.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("GetInvokedName with Runnable class")
    public void getInvokedNameRunnable() {
        Assertions.assertEquals("run", LambdaFactory.getInvokedName(Runnable.class));
    }

    @Test
    @DisplayName("GetInvokedName with Consumer class")
    public void getInvokedNameConsumer() {
        Assertions.assertEquals("accept", LambdaFactory.getInvokedName(Consumer.class));
    }

    @Test
    @DisplayName("GetInvokedName with Function class")
    public void getInvokedNameFunction() {
        Assertions.assertEquals("apply", LambdaFactory.getInvokedName(Function.class));
    }

    @Test
    @DisplayName("GetInvokedName with BiConsumer class")
    public void getInvokedNameBiConsumer() {
        Assertions.assertEquals("accept", LambdaFactory.getInvokedName(BiConsumer.class));
    }

    @Test
    @DisplayName("GetInvokedName with custom IntTernaryOperator")
    public void getInvokedNameRandomTest() {
        Assertions.assertEquals("oneFunction", LambdaFactory.getInvokedName(IntTernaryOperator.class));
    }

    @Test
    @DisplayName("Generate with two parameters method")
    public void generateTwoParams() throws Throwable {
        final Method method = LambdaFactoryTest.class.getDeclaredMethod("twoParams", int.class, int.class);
        final CallSite callSite = LambdaFactory.generate(method);
        Assertions.assertEquals(3, LambdaFactory.invoke(new LambdaFactoryTest(), callSite, 1, 2));
    }

    @Test
    @DisplayName("Generate with three parameters method")
    public void generateThreeParams() throws Throwable {
        final Method method = LambdaFactoryTest.class.getDeclaredMethod("threeParams", int.class, int.class, int.class);
        LambdaFactory.registerInterface(IntTernaryOperator.class);
        final CallSite callSite = LambdaFactory.generate(method);
        Assertions.assertEquals(6, LambdaFactory.invokeStatic(callSite, 1, 2, 3));
    }

    @Test
    @DisplayName("Generate with arity parameters method")
    public void generateArityParams() throws Throwable {
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
