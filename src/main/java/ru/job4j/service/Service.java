package ru.job4j.service;

import java.util.Collection;

public interface Service<E> {
    Collection<E> findAll();

    E findById(int id);
}
