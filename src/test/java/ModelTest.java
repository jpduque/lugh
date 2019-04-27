import com.fasterxml.jackson.core.sym.NameN;
import lugh.Main;
import lugh.complexity_analizer.Analizer;
import lugh.gitApi.GitApi;
import lugh.predictor.Statistical;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class ModelTest {

    private static String project = "saeidzebardast/java-design-patterns";
    private static Analizer analizer = new Analizer();
    private static GitApi gitApi = new GitApi();
    private static Statistical forecaster = new Statistical();
    private static List<Map<String, Integer>> retrievedAnalysis = new ArrayList<>();
    @BeforeClass
    public static void runAnalisis() throws IOException {
        int i = 0;
        List<String> commitList = gitApi.commitList(project);
        for (String commit : commitList) {
            gitApi.getGitRepo(project, commit);
            List<String> patterns = Main.callPatterns();
            Main.executePatternDetection(patterns);
//                dpCount = dpCount + Main.executePatternDetection(patterns);
            retrievedAnalysis.add(analizer.runStaticAnalysis());
//                classCount = classCount +  analizer.getFilesNum();
            analizer.getFilesNum();
            gitApi.cleanTemporal();
            System.out.println("running on commit " + ++i);
        }
    }

    @Test
    public void GetErrorMatrix() throws IOException {
        int classCount = 0;
        int dpCount = 0;
        List<List<Double>> errorMatrix = new ArrayList<>();
        List<Map<String, Integer>> bugs = new ArrayList<Map<String, Integer>>();
        for (Map<String, Integer> data : retrievedAnalysis) {
            bugs.add(data);
        }
        for (int i = bugs.size() - 1; i > 0; i--) {
            List<Double> errorPercentage = new ArrayList<>();
            bugs.remove(bugs.get(bugs.size() - 1));
            forecaster.executeForecast(bugs);
            for (String key : forecaster.getForecastValue().keySet()) {
                errorPercentage.add(abs(forecaster.getForecastValue().get(key) - bugs.get(bugs.size() - 1).get(key)) / bugs.get(bugs.size() - 1).get(key));
            }
            errorMatrix.add(errorPercentage);
        }

        System.out.println("\nPrinting the Deface Matrix");
        for(List<Double> errorPercentage : errorMatrix){
            System.out.println(errorPercentage);
        }
        System.out.println("\n===================================================================\n");
    }

    @Test
    public void GetPrecisionMatrix() throws IOException {
        int classCount = 0;
        int dpCount = 0;
        List<List<Double>> errorMatrix = new ArrayList<>();
        List<Map<String, Integer>> bugs = new ArrayList<Map<String, Integer>>();
        for (Map<String, Integer> data : retrievedAnalysis) {
            bugs.add(data);
        }
        for (int i = bugs.size() - 1; i > 0; i--) {
            List<Double> errorPercentage = new ArrayList<>();
            bugs.remove(bugs.get(bugs.size() - 1));
            forecaster.executeForecast(bugs);
            for (String key : forecaster.getForecastValue().keySet()) {
                errorPercentage.add(abs(1-(forecaster.getForecastValue().get(key) - bugs.get(bugs.size() - 1).get(key)) / bugs.get(bugs.size() - 1).get(key)));
            }
            errorMatrix.add(errorPercentage);
        }

        System.out.println("\nPrinting the Precision Matrix");
        for(List<Double> errorPercentage : errorMatrix){
            System.out.println(errorPercentage);
        }
        System.out.println("\n===================================================================\n");
    }

    @Ignore
    @Test
    public void GetRocValues() throws IOException {
        int classCount = 0;
        int dpCount = 0;

        List<Map<String, Double>> forecastedValues = new ArrayList<>();
        List<Double> realTotalValues = new ArrayList<>();
        List<Double> forecastedTotalValues = new ArrayList<>();

        List<Double> sensibility = new ArrayList<>();
        List<Double> specificity = new ArrayList<>();

        List<Map<String, Integer>> bugs = new ArrayList<Map<String, Integer>>();
        for (Map<String, Integer> data : retrievedAnalysis) {
            bugs.add(data);
        }

        forecaster.executeForecast(bugs);
        List<Map<String, Double>> realValues = forecaster.getProbabilities();

        for (int i = bugs.size() - 1; i > 0; i--)
            forecastedValues.add(forecaster.getFinalProbability());
        for(Map<String, Double> realValue : realValues)
            realTotalValues.add(realValue.values().stream().mapToDouble(i -> i).sum());
        for(Map<String, Double> realValue : forecastedValues)
            forecastedTotalValues.add(realValue.values().stream().mapToDouble(i -> i).filter(i -> i > -1).sum());

        for(int i =0;realTotalValues.size() -1 >i; i++){
            sensibility.add(realTotalValues.get(i)/(abs(forecastedTotalValues.get(i)-realTotalValues.get(i))+realTotalValues.get(i)));
        }
        for(int i =0;realTotalValues.size() -1 >i; i++){
            specificity.add(abs(forecastedTotalValues.get(i)-realTotalValues.get(i))/(abs(forecastedTotalValues.get(i)-realTotalValues.get(i))+realTotalValues.get(i)));
        }

        System.out.println("\nPrinting the sensibility array");
        System.out.println(sensibility);
        System.out.println("\n===================================================================\n");
        System.out.println("\nPrinting the specificity array");
        System.out.println(specificity);
        System.out.println("\n===================================================================\n");
    }
}
