package lugh.predictor;


import lugh.complexity_analizer.Categories;
import org.apache.mahout.math.stats.LogLikelihood.ScoredItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Statistical {

    public static void main(String[] args) {
        Map<String, Integer> bugClassifier = new HashMap<>();
        for (Categories categ : Categories.values()) {
            Random ran = new Random();
// Assumes max and min are non-negative.
            int randomInt = 0 + ran.nextInt(10 - 0 + 1);
            bugClassifier.put(String.valueOf(categ), randomInt);
        }
        ScoredItem likelihood = new ScoredItem(bugClassifier,0);
        System.out.println(likelihood.getScore());
        System.out.println(likelihood.getItem());

    }

}
