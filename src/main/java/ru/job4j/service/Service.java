package ru.job4j.service;

import java.util.Collection;

public interface Service<E> {
    void buy(int id, String userName, String phone);
}
