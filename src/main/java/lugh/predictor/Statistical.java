package lugh.predictor;


import lugh.gitApi.Commit;
import org.apache.commons.math3.exception.ZeroException;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;


public class Statistical {

//    public static void main(String[] args) {
    public static void map(List<Map> bugs) {
//        List<Map<String, Integer>> bugs = testdata();
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
                double pPhiOmega =  Double.valueOf(String.valueOf(bugs.get(i).get(qa)))/ Double.valueOf(omegaSum.get(i));
                double pOmega = Double.valueOf(phiSum.get(i).get(qa)) / (double) commitTotal;
                double pPhi = Double.valueOf(omegaSum.get(i)) / (double) commitTotal;
                probs.put(qa, (pPhiOmega * pPhi) / pOmega);
            }
            probabilities.add(probs);
        }

        for(Map<String, Double> probs:probabilities)
            System.out.println(probs);
    }

    private static List<Map<String, Integer>> testdata() {
        List<Map<String, Integer>> bugsTest = new ArrayList<>();
        Map<String, Integer> testValues = new HashMap<>();
        testValues.put("a", 0);
        testValues.put("b", 0);
        testValues.put("c", 0);
        testValues.put("d", 0);
        testValues.put("e", 0);
        testValues.put("f", 0);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 0);
        bugsTest.add(testValues);
        testValues = new HashMap<>();
        testValues.put("a", 0);
        testValues.put("b", 0);
        testValues.put("c", 0);
        testValues.put("d", 0);
        testValues.put("e", 0);
        testValues.put("f", 0);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 0);
        bugsTest.add(testValues);
        testValues = new HashMap<>();
        testValues.put("a", 0);
        testValues.put("b", 0);
        testValues.put("c", 0);
        testValues.put("d", 3);
        testValues.put("e", 0);
        testValues.put("f", 0);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 1);
        bugsTest.add(testValues);
        testValues = new HashMap<>();
        testValues.put("a", 3);
        testValues.put("b", 0);
        testValues.put("c", 0);
        testValues.put("d", 4);
        testValues.put("e", 0);
        testValues.put("f", 2);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 1);
        bugsTest.add(testValues);
        testValues = new HashMap<>();
        testValues.put("a", 3);
        testValues.put("b", 0);
        testValues.put("c", 0);
        testValues.put("d", 4);
        testValues.put("e", 0);
        testValues.put("f", 2);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 1);
        bugsTest.add(testValues);
        testValues = new HashMap<>();
        testValues.put("a", 11);
        testValues.put("b", 2);
        testValues.put("c", 6);
        testValues.put("d", 13);
        testValues.put("e", 0);
        testValues.put("f", 6);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 3);
        bugsTest.add(testValues);
        testValues = new HashMap<>();
        testValues.put("a", 11);
        testValues.put("b", 2);
        testValues.put("c", 6);
        testValues.put("d", 13);
        testValues.put("e", 0);
        testValues.put("f", 6);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 3);
        bugsTest.add(testValues);
        testValues = new HashMap<>();
        testValues.put("a", 11);
        testValues.put("b", 2);
        testValues.put("c", 6);
        testValues.put("d", 13);
        testValues.put("e", 0);
        testValues.put("f", 7);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 3);
        bugsTest.add(testValues);
        testValues = new HashMap<>();
        testValues.put("a", 11);
        testValues.put("b", 2);
        testValues.put("c", 6);
        testValues.put("d", 13);
        testValues.put("e", 0);
        testValues.put("f", 7);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 3);
        bugsTest.add(testValues);
        testValues = new HashMap<>();
        testValues.put("a", 11);
        testValues.put("b", 2);
        testValues.put("c", 6);
        testValues.put("d", 13);
        testValues.put("e", 0);
        testValues.put("f", 7);
        testValues.put("g", 0);
        testValues.put("h", 0);
        testValues.put("i", 3);
        bugsTest.add(testValues);
        return bugsTest;
    }

}
