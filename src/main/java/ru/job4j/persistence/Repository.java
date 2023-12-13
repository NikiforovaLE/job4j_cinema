package ru.job4j.persistence;

import java.util.Collection;

public interface Repository<E> {
    Collection<E> findAll();

    E findById(int id);
}