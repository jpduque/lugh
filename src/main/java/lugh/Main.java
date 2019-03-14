package lugh;

import lugh.complexity_analizer.Analizer;
import lugh.gitApi.GitApi;
import lugh.patternDetection.Detector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.tongfei.progressbar.*;

public class Main {

    private static String project = "saeidzebardast/java-design-patterns";
    private static Analizer analizer = new Analizer();
    private static GitApi gitApi = new GitApi();

    public static void main(String[] args) throws IOException {

        List<String> commitList = gitApi.commitList(project);
        List<Map> retrievedAnalysis = new ArrayList<>();
        try (ProgressBar pb = new ProgressBar("Running Static Analysis", 100)) {
            for (String commit : commitList) {
                gitApi.getGitRepo(project, commit);
                List<String> patterns = callPatterns();
                executePatternDetection(patterns);
                retrievedAnalysis.add(analizer.runStaticAnalysis());
                gitApi.cleanTemporal();
                pb.stepTo(((commitList.indexOf(commit)+1)*100)/commitList.size());
                pb.setExtraMessage("Analyzing");
            }
        }
        int i = 0;
        for (Map test : retrievedAnalysis) {
            System.out.println("*********************************** Commit " + ++i + "******************");
            System.out.println(test.values());
        }
    }

    private static List<String> callPatterns() {
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

    private static void executePatternDetection(List<String> patterns) {
        Detector detector = new Detector();
        String sourceFolder = System.getProperty("user.dir") + "/temp";
        String templatesFolder = System.getProperty("user.dir") + "/patterns";
        for (String pattern : patterns) {
            detector.runAnalysis(sourceFolder, templatesFolder + "/" + pattern);
        }
    }
}

