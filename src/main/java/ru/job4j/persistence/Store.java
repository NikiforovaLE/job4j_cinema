package ru.job4j.persistence;

import java.util.Collection;

public interface Store<E> {
    Collection<E> findAll(int id);

    E findById(int id);

    void save(E e);

    void delete(int id);
}