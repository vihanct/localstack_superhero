package com.cleartax.training_superheroes.repos;

import com.cleartax.training_superheroes.entities.Superhero;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SuperheroRepository extends MongoRepository<Superhero, String> {
    List<Superhero> findAllByName(String name);
}
