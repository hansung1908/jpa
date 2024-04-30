package com.hansung.entitiy;

public class Customer {

    private String id;
    private String name;
    private long registerDate;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
        this.registerDate = System.currentTimeMillis();
    }

    // 샘플 객체를 반환
    public static Customer sample() {
        return new Customer("ID0001", "Kim");
    }
}
