package test2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<E> {
    private Queue<E> queue;
    private int capacity;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
	
    public MyBlockingQueue(int capacity){
        //TODO: Constructor with input capacity
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public void put(E e){
        //TODO: When the queue is full,  wait until the consumer takes data and the queue has some empty buffer
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.offer(e);
            System.out.println("Thread: "+ Thread.currentThread().getName() +", Produced: " + e + ", Queue: " + queue);
            notEmpty.signal();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public E take() {
        //TODO: When queue empty,   wait until the producer puts new data into the queue
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            E item = queue.poll();
            System.out.println("Thread: "+ Thread.currentThread().getName() + ", Consumed: " + item + ", Queue: " + queue);
            notFull.signal();
            return item;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int CAPACITY = 20;
        int PRODUCER_WORK = 22;
        int PRODUCER_CNT = 20;
        int PRODUCER_OFF = 10;
        int CONSUMER_WORK = 20;
        int CONSUMER_CNT = 20;
        int CONSUMER_OFF = 10;

        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(CAPACITY);

        Runnable producer = () -> {
            for(int i=0; i<PRODUCER_WORK; i++){
                queue.put(i);
                try {
                    Thread.sleep(PRODUCER_OFF);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumer = () -> {
            for(int i=0; i<CONSUMER_WORK; i++){
                queue.take();
                try {
                    Thread.sleep(CONSUMER_OFF);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < PRODUCER_CNT; i++) {
            new Thread(producer).start();
        }
        for (int i = 0; i < CONSUMER_CNT; i++) {
            new Thread(consumer).start();
        }

    }

}
