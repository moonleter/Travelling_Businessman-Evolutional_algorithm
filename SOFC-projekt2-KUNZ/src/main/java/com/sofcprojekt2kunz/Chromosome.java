package com.sofcprojekt2kunz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private final List<City> cities;
    private final Random random = new Random();

    public Chromosome(List<City> cities) {
        this.cities = new ArrayList<>(cities);
    }  //chromosome - je jedno řešení problému

    public Chromosome(Chromosome other) {
        this(other.getCities());
    }

    public List<City> getCities() {
        return cities;
    }

    public Chromosome crossover(Chromosome other) {  //křížení - vybere náhodnou pozici v předkovi a tu poté umístí do potomka
        Chromosome offspring = new Chromosome(this);

        int startPos = random.nextInt(cities.size());
        int endPos = random.nextInt(cities.size());

        for (int i = startPos; i < endPos; i++) {
            City city = offspring.getCities().get(i);
            int cityIndex = other.getCities().indexOf(city);
            Collections.swap(offspring.getCities(), i, cityIndex);
        }

        return offspring;
    }

    public void mutate() {
        int index1 = random.nextInt(cities.size());
        int index2 = random.nextInt(cities.size());
        Collections.swap(cities, index1, index2);
    }
}
