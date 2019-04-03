package lugh.predictor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Statistical {

    public void executeForecast(List<Map> bugs) {
        List<Integer> omegaSum = new ArrayList<>();
        List<Map<String, Integer>> phiSum = new ArrayList<>();
        List<Map<String, Double>> probabilities = new ArrayList<>();

        int index = 0;
        for (Map<String, Integer> data : bugs) {
            Map<String, Integer> phis = new HashMap<>();
            omegaSum.add(data.values().stream().mapToInt(i -> i).sum());
            for (int i = 0; i <= index; i++) {
                Map<String, Integer> commit = bugs.get(i);
                commit.forEach((k, v) -> phis.merge(k, v, Integer::sum));
            }
            index++;
            phiSum.add(phis);
        }

        int commitTotal = 0;
        for (int i = 0; i < omegaSum.size(); i++) {
            Map<String, Double> probs = new HashMap<>();
            commitTotal += omegaSum.get(i);
            for (String qa : phiSum.get(i).keySet()) {
                double pPhiOmega = Double.valueOf(String.valueOf(bugs.get(i).get(qa))) / Double.valueOf(omegaSum.get(i));
                double pOmega = Double.valueOf(phiSum.get(i).get(qa)) / (double) commitTotal;
                double pPhi = Double.valueOf(omegaSum.get(i)) / (double) commitTotal;
                probs.put(qa, (pPhiOmega * pPhi) / pOmega);
            }
            probabilities.add(probs);
        }
        forecast(probabilities, phiSum);
    }

    private void forecast(List<Map<String, Double>> probMatrix, List<Map<String, Integer>> bugsMatrix) {
        Map<String, Double> commit = probMatrix.get(0);
        Map<String, Double> finalProbability = new HashMap<>();
        Map<String, Double> increaseProbability = new HashMap<>();
        Map<String, Double> forecastValue = new HashMap<>();
        Map<String, Integer> intForecastValue = new HashMap<>();
        for (Map<String, Double> comm : probMatrix) {
            for (String qa : comm.keySet()) {
                if (Double.isNaN(commit.get(qa)) || comm.get(qa) > commit.get(qa))
                    commit.put(qa, comm.get(qa));
            }
        }

        double commitProbability = 1;
        for (double probability : probMatrix.get(probMatrix.size() - 1).values()) {
            if (probability != 0 && !Double.isNaN(probability))
                commitProbability = commitProbability * probability;
        }

        for (String qa : commit.keySet()) {
            increaseProbability.put(qa, commit.get(qa) * commitProbability);
        }

        for (String qa : commit.keySet()) {
            finalProbability.put(qa, probMatrix.get(probMatrix.size() - 1).get(qa) + increaseProbability.get(qa));
        }

        for (String qa : finalProbability.keySet()) {
            forecastValue.put(qa, Double.valueOf(String.valueOf(bugsMatrix.get(bugsMatrix.size() - 1).get(qa))) * finalProbability.get(qa));
        }

        for (String qa : forecastValue.keySet()) {
            intForecastValue.put(qa, forecastValue.get(qa).intValue());
        }
//        System.out.println(increaseProbability);
//        System.out.println(finalProbability);
//        System.out.println(forecastValue);
        System.out.println("Forecasted Probability");
        System.out.println(intForecastValue);
    }
}
