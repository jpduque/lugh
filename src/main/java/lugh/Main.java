package lugh;

import lugh.complexity_analizer.Analizer;
import lugh.gitApi.GitApi;
import lugh.patternDetection.Detector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static String project = "saeidzebardast/java-design-patterns";
    private static Analizer analizer = new Analizer();

    public static void main(String[] args) throws IOException {
        GitApi gitApi = new GitApi();
        List<String> commitList = gitApi.commitList(project);
        List<Map> retrievedAnalysis = new ArrayList<>();
        for (String commit : commitList) {
            gitApi.getGitRepo(project, commit);
            List<String> patterns = callPatterns();
            executePatternDetection(patterns);
            retrievedAnalysis.add(analizer.runStaticAnalysis());
            gitApi.cleanTemporal();
        }
        int i = 0;
        for (Map test : retrievedAnalysis) {
            System.out.println("*********************************** Commit " + ++i + "******************");
            System.out.println(test.values());
        }
    }

    private static List<String> callPatterns() {
        List<String> results = new ArrayList<String>();
        String templatesFolder = Main.class.getClassLoader().getResource("patterns").getFile();
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
        String sourceFolder = Main.class.getClassLoader().getResource("temp").getFile();
        String templatesFolder = Main.class.getClassLoader().getResource("patterns").getFile();
        for (String pattern : patterns) {
            detector.runAnalysis(sourceFolder, templatesFolder + "/" + pattern);
        }
    }
}

