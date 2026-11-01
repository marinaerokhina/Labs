package com.deepcloner;

import java.util.*;

import com.deepcloner.Api.*;
import com.deepcloner.Domain.Annotations.*;
import com.deepcloner.Infrastructure.Strategy.DateTimeStrategy;

class Address {
    String city;
    String street;
    public Address() {}
    public Address(String c, String s) { this.city = c; this.street = s; }

    @Override
    public String toString() {
        return "Address{" + city + ", " + street + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address other)) return false;
        return Objects.equals(city, other.city) &&
               Objects.equals(street, other.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street);
    }
}

class Person {
    String name;
    int age;
    Address address;
    @ShallowClone
    List<String> tags = new ArrayList<>();
    @CloneIgnore
    String cached = "SHOULD_BE_NULL";
    @CloneWith(DateTimeStrategy.class)
    Date createdAt = new Date();
    Person friend; // для проверки циклов

    public Person() {}

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", tags=" + tags +
                ", cached='" + cached + '\'' +
                ", createdAt=" + createdAt +
                ", friend=" + (friend != null ? friend.name : "null") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person other)) return false;

        return age == other.age &&
               Objects.equals(name, other.name) &&
               Objects.equals(address, other.address) &&
               Objects.equals(tags, other.tags) &&
               Objects.equals(createdAt, other.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, address, tags, createdAt);
    }
}


public class App {

    public static void main(String[] args) {
        System.out.println("==== DEMO: DEEP CLONER ====");

        // создаём объект с вложенностями
        Person alice = new Person();
        alice.name = "Alice";
        alice.age = 30;
        alice.address = new Address("Novosibirs", "Nemirovicha");
        alice.tags.add("admin");
        alice.tags.add("verified");

        // создаём цикл
        Person bob = new Person();
        bob.name = "Bob";
        bob.age = 28;
        bob.address = new Address("Moscow", "Petuhova");
        alice.friend = bob;
        bob.friend = alice;

        // клонируем
        System.out.println("\n--- ORIGINAL ---");
        printObjectGraph(alice);

        Cloner cloner = new ClonerBuilder().build();
        Person clone = cloner.deepClonee(alice);

        System.out.println("\n--- CLONE ---");
        printObjectGraph(clone);

        // проверка ссылочной целостности и различий
        System.out.println("\n--- CHECKS ---");
        System.out.printf("alice == clone: %s%n", alice.equals(clone));
        System.out.printf("alice.address == clone.address: %s%n", alice.address.equals(clone.address));
        System.out.printf("alice.tags == clone.tags (shallow): %s%n", alice.tags == clone.tags);
        System.out.printf("alice.cached: %s, clone.cached: %s%n", alice.cached, clone.cached);
        System.out.printf("alice.friend.friend == alice ? %s%n", alice.friend.friend == alice);
        System.out.printf("clone.friend.friend == clone ? %s%n", clone.friend.friend == clone);
        System.out.printf("Date deep copied? %s%n", alice.createdAt != clone.createdAt &&
                alice.createdAt.equals(clone.createdAt));

        System.out.println("\n==== DONE ====");
    }

    private static void printObjectGraph(Person p) {
        System.out.printf("Person: %s%n", p);
        System.out.printf("  -> Address: %s%n", p.address);
        System.out.printf("  -> Tags: %s%n", p.tags);
        System.out.printf("  -> Friend: %s%n", p.friend != null ? p.friend.name : "null");
        if (p.friend != null) {
            System.out.printf("     -> Friend.address: %s%n", p.friend.address);
            System.out.printf("     -> Friend.friend: %s%n", p.friend.friend != null ? p.friend.friend.name : "null");
        }
    }
}
