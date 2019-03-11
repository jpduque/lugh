package lugh;

import lugh.complexity_analizer.Analizer;
import lugh.gitApi.GitApi;
import lugh.pattern_detection.Detector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static String encodedUrl = "https://github.com/saeidzebardast/java-design-patterns";
    private static String branch = "master";
    private static Analizer analizer = new Analizer();

    public static void main(String[] args) throws IOException {
        GitApi gitApi = new GitApi();
        //try {
        //    gitApi.getGitRepo(encodedUrl, branch);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

        //List<String> patterns = callPatterns();
        //executePatternDetection(patterns);
        //analizer.runStaticAnalysis();

        gitApi.getAllCommits();
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

