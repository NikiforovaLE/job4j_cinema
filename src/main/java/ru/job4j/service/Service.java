package ru.job4j.service;

import java.util.Collection;

public interface Service<E> {
    Collection<E> findAll();

    E findById(int id);

    void buy(int id, String userName, String phone);
}
