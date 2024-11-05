package test1;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestLock {

//    @org.junit.jupiter.api.Test
//    public void testSynchronized() {
//        Account account = new Account();
//        ExecutorService service = Executors.newFixedThreadPool(100);
//
//        for (int i = 1; i <= 100; i++) {
//            service.execute(new DepositThread(account, 10));
//        }
//
//        service.shutdown();
//
//        while (!service.isTerminated()) {
//        }
//
//        System.out.println("Balance: " + account.getBalance());
//    }
//
//    @org.junit.jupiter.api.Test
//    public void testReentrantLock() {
//        AccountReentrantLock account = new AccountReentrantLock();
//        ExecutorService service = Executors.newFixedThreadPool(100);
//
//        for (int i = 1; i <= 100; i++) {
//            service.execute(new DepositThreadReentrantLock(account, 10));
//        }
//
//        service.shutdown();
//
//        while (!service.isTerminated()) {
//        }
//
//        System.out.println("Balance: " + account.getBalance());
//    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(TestLock.class.getSimpleName())
                .measurementIterations(3)
                .warmupIterations(1)
                .mode(Mode.AverageTime)
                .forks(1)
                .shouldDoGC(true)
                .build();
        new Runner(options).run();
    }

    @State(Scope.Thread)
    public static class MyState {
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static void testSynchronized(MyState state) {
        Account account = new Account();
        ExecutorService service = Executors.newFixedThreadPool(100);

        for (int i = 1; i <= 100; i++) {
            service.execute(new DepositThread(account, 10));
        }

        service.shutdown();

        while (!service.isTerminated()) {
        }

        System.out.println("Balance: " + account.getBalance());
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static void testReentrantLock(MyState state) {
        AccountReentrantLock account = new AccountReentrantLock();
        ExecutorService service = Executors.newFixedThreadPool(100);

        for (int i = 1; i <= 100; i++) {
            service.execute(new DepositThreadReentrantLock(account, 10));
        }

        service.shutdown();

        while (!service.isTerminated()) {
        }

        System.out.println("Balance: " + account.getBalance());
    }

}
