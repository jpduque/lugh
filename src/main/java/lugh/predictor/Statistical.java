package lugh.predictor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Statistical {

    private Map<String, Double> increaseProbability;
    private Map<String, Double> finalProbability;
    private Map<String, Double> forecastValue;
    private Map<String, Integer> intForecastValue;
    private List<Map<String, Double>> probabilities;
    private List<Map<String,Integer>> bugs;
    private List<Integer> omegaSum;
    private List<Map<String, Integer>> phiSum;

    public Map<String, Integer> executeForecast(List<Map<String, Integer>> bugs) {
        this.bugs = bugs;
        omegaSum = new ArrayList<>();
        phiSum = new ArrayList<>();
        probabilities = new ArrayList<>();

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
        return forecast(probabilities, phiSum);
    }

    private Map<String, Integer> forecast(List<Map<String, Double>> probMatrix, List<Map<String, Integer>> bugsMatrix) {
        Map<String, Double> commit = probMatrix.get(0);
        finalProbability = new HashMap<>();
        increaseProbability = new HashMap<>();
        forecastValue = new HashMap<>();
        intForecastValue = new HashMap<>();
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
        return intForecastValue;
    }

    public Map<String, Double> getIncreaseProbability() {
        return increaseProbability;
    }

    public void setIncreaseProbability(Map<String, Double> increaseProbability) {
        this.increaseProbability = increaseProbability;
    }

    public Map<String, Double> getFinalProbability() {
        return finalProbability;
    }

    public void setFinalProbability(Map<String, Double> finalProbability) {
        this.finalProbability = finalProbability;
    }

    public Map<String, Double> getForecastValue() {
        return forecastValue;
    }

    public void setForecastValue(Map<String, Double> forecastValue) {
        this.forecastValue = forecastValue;
    }

    public Map<String, Integer> getIntForecastValue() {
        return intForecastValue;
    }

    public void setIntForecastValue(Map<String, Integer> intForecastValue) {
        this.intForecastValue = intForecastValue;
    }

    public List<Map<String, Double>> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(List<Map<String, Double>> probabilities) {
        this.probabilities = probabilities;
    }

    public List<Map<String,Integer>> getBugs() {
        return bugs;
    }

    public void setBugs(List<Map<String,Integer>> bugs) {
        this.bugs = bugs;
    }

    public List<Map<String, Integer>> getPhiSum() {
        return phiSum;
    }

    public void setPhiSum(List<Map<String, Integer>> phiSum) {
        this.phiSum = phiSum;
    }

    public List<Integer> getOmegaSum() {
        return omegaSum;
    }

    public void setOmegaSum(List<Integer> omegaSum) {
        this.omegaSum = omegaSum;
    }
}
