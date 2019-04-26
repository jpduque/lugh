package lugh;

import lugh.complexity_analizer.Analizer;
import lugh.gitApi.GitApi;
import lugh.patternDetection.Detector;
import lugh.predictor.Statistical;
import me.tongfei.progressbar.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static Analizer analizer = new Analizer();
    private static GitApi gitApi = new GitApi();
    private static Statistical forecaster = new Statistical();

    public static void main(String args[]) throws IOException {
        String owner = args[0];
        String repo = args[1];
        String project = owner + "/" + repo;
        List<String> commitList = gitApi.commitList(project);
        List<Map<String, Integer>> retrievedAnalysis = new ArrayList<>();
        try (ProgressBar pb = new ProgressBar("Running Static Analysis", 100)) {
            for (String commit : commitList) {
                gitApi.getGitRepo(project, commit);
                List<String> patterns = callPatterns();
                executePatternDetection(patterns);
                retrievedAnalysis.add(analizer.runStaticAnalysis());
                gitApi.cleanTemporal();
                pb.stepTo(((commitList.indexOf(commit) + 1) * 100) / commitList.size());
                pb.setExtraMessage("Analyzing");
            }
        }
        System.out.println(forecaster.executeForecast(retrievedAnalysis));
    }

    public static List<String> callPatterns() {
        List<String> results = new ArrayList<String>();
        String templatesFolder = System.getProperty("user.dir") + "/patterns";
        File[] files = new File(templatesFolder).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }

    public static int executePatternDetection(List<String> patterns) {
        int count = 0;
        Detector detector = new Detector();
        String sourceFolder = System.getProperty("user.dir") + "/temp";
        String templatesFolder = System.getProperty("user.dir") + "/patterns";
        for (String pattern : patterns) {
            detector.runAnalysis(sourceFolder, templatesFolder + "/" + pattern);
            count = count + detector.getDpcount();
        }
        return count;
    }
}

