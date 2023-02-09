package com.urise.webapp;

public class DeadLockDemo {
    public static void main(String[] args) {

        final Resource res1 = new Resource("Ресурс 1");
        final Resource res2 = new Resource("Ресурс 2");

        lock(res1, res2);
        lock(res2, res1);

    }

    private static void lock(Resource res1, Resource res2) {
        new Thread(() -> {
            System.out.println("Ожидаем " + res1.getResourceName());
            synchronized (res1) {
                System.out.println("Заблокировали " + res1.getResourceName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Ожидаем " + res2.getResourceName());
                synchronized (res2) {
                    System.out.println("Заблокировали " + res2.getResourceName());
                }
            }
        }).start();
    }

    private static class Resource {
        private final String resourceName;

        private Resource(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getResourceName() {
            return resourceName;
        }

    }

}
