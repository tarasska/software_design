package ru.akirakozov.sd.refactoring.entities;

import java.util.Objects;

public class Product {
    private final String name;
    private final long price;

    public Product(String name, long price) {
        Objects.requireNonNull(name);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            Product other = (Product) obj;
            return name.equals(other.name) && price == other.price;
        }
        return false;
    }
}
