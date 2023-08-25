package com.sofcprojekt2kunz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OptimalPath extends BorderPane {
    private final List<City> cities;
    private Pane pane;
    private Label fitnessLabel;
    private Label generationLabel;

    public OptimalPath() {
        cities = new ArrayList<>();
        pane = new Pane();
        pane.setOnMouseClicked(this::handleClick);
        setCenter(pane);
        setBottom(createBottomBox());
    }

    private HBox createBottomBox() { //GUI prvky
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(10, 10, 10, 10));

        Button runButton = new Button("Spustit Algoritmus");
        runButton.setOnAction(e -> runAlgorithm());

        TextField populationSizeField = new TextField("100");
        populationSizeField.setPrefColumnCount(4);
        Label populationSizeLabel = new Label("Velikost populace: ");

        TextField generationsField = new TextField("1000");
        generationsField.setPrefColumnCount(4);
        Label generationsLabel = new Label("Generaces: ");

        Slider mutationSlider = new Slider(0, 1, 0.1);    //výchozí hodnota je 10%
        mutationSlider.setMajorTickUnit(0.1);
        mutationSlider.setShowTickLabels(true);
        mutationSlider.setShowTickMarks(true);
        mutationSlider.setSnapToTicks(true);
        Label mutationLabel = new Label("Pravděpodobnost mutace: ");

        fitnessLabel = new Label();
        generationLabel = new Label();

        hbox.getChildren().addAll(runButton, populationSizeLabel, populationSizeField,
                generationsLabel, generationsField, mutationLabel, mutationSlider,
                fitnessLabel, generationLabel);
        return hbox;
    }

    private void handleClick(MouseEvent event) { //event handler pro kliknutí myší(přidání měst)
        City city = new City(event.getX(), event.getY());
        cities.add(city);
        drawCity(city);
    }

    private void drawCity(City city) { //kreslí město
        Circle circle = new Circle(city.getX(), city.getY(), 5, Color.RED);
        pane.getChildren().add(circle);
    }

    private void drawPath(List<City> path) { //kreslení cest
        pane.getChildren().removeIf(node -> node instanceof Line);

        for (int i = 0; i < path.size() - 1; i++) {
            Line line = new Line(path.get(i).getX(), path.get(i).getY(), path.get(i + 1).getX(), path.get(i + 1).getY());
            pane.getChildren().add(line);
        }

        Line closingLine = new Line(path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY(), path.get(0).getX(), path.get(0).getY());
        pane.getChildren().add(closingLine);
    }

    private void updateGenerationLabel(int generation) {
        generationLabel.setText("Generace: " + generation);
    } //aktualizace text labelu


    public void runAlgorithm() {  //evoluční algoritmus
        int populationSize = Integer.parseInt(((TextField) ((HBox) getBottom()).getChildren().get(2)).getText());
        int generations = Integer.parseInt(((TextField) ((HBox) getBottom()).getChildren().get(4)).getText());
        double mutationProbability = ((Slider) ((HBox) getBottom()).getChildren().get(6)).getValue();


        List<Chromosome> population = IntStream.range(0, populationSize)
                .mapToObj(i -> {
                    List<City> shuffledCities = new ArrayList<>(cities);
                    Collections.shuffle(shuffledCities);
                    return new Chromosome(shuffledCities);
                })
                .collect(Collectors.toList());

        Random random = new Random();
        initializeFitnessLabel(population);

        for (int generation = 0; generation <= generations; generation++) {
            List<Chromosome> newPopulation = new ArrayList<>();

            for (int i = 0; i < populationSize; i++) {
                Chromosome parent1 = selectParent(population);
                Chromosome parent2 = selectParent(population);

                Chromosome offspring = parent1.crossover(parent2);

                if (random.nextDouble() < mutationProbability) {
                    offspring.mutate();
                }

                newPopulation.add(offspring);
            }

            population = newPopulation;

            Chromosome bestChromosome = population.stream()
                    .min(Comparator.comparing(this::calculateFitness))
                    .orElseThrow();

            drawPath(bestChromosome.getCities());
            updateFitnessLabel(calculateFitness(bestChromosome));
            updateGenerationLabel(generation);
        }
    }

    //selekce rodičovského chromozomu ze zadané populace chromozomů tak, aby se vybral jeidnec s co nejnižší vzdálenosotí
    private Chromosome selectParent(List<Chromosome> population) {
        Random random = new Random();
        Chromosome parent1 = population.get(random.nextInt(population.size()));
        Chromosome parent2 = population.get(random.nextInt(population.size()));

        return calculateFitness(parent1) < calculateFitness(parent2) ? parent1 : parent2;
    }

    //aktualizace text labelu fitness funkce
    private void updateFitnessLabel(double fitness) {
        double distance = 1 / fitness;
        fitnessLabel.setText(String.format("Fitness: %.2f", distance));
    }


    //výpočet fitness funkce, poríčát vzdálenost mezi prvním a posledním městem a výsledek je celková vzdálenost! Proto tedy invertuju výslednou hodnotu, jelikož chci maximální hodnotu fitness funkce
    private double calculateFitness(Chromosome chromosome) {
        double distance = 0;
        List<City> cities = chromosome.getCities();
        for (int i = 0; i < cities.size() - 1; i++) {
            distance += cities.get(i).distance(cities.get(i + 1));
        }
        distance += cities.get(cities.size() - 1).distance(cities.get(0));

        // Invertuje hodnotu vzdálenosti, aby byla vhodná pro genetický algoritmus a výpočet fitness funkce
        return 1 / distance;
    }

    private void initializeFitnessLabel(List<Chromosome> population) {
        Chromosome bestChromosome = population.stream()
                .min(Comparator.comparing(this::calculateFitness))
                .orElseThrow();
        updateFitnessLabel(calculateFitness(bestChromosome));
    }


}