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
        String templatesFolder = System.getProperty("user.dir")+"/patterns";
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
        String sourceFolder = System.getProperty("user.dir")+"/temp";
        String templatesFolder = System.getProperty("user.dir")+"/patterns";
        for (String pattern : patterns) {
            detector.runAnalysis(sourceFolder, templatesFolder + "/" + pattern);
        }
    }

    //TODO fix this

//    try (ProgressBar pb = new ProgressBar("Test", 100)) { // name, initial max
//        // Use ProgressBar("Test", 100, ProgressBarStyle.ASCII) if you want ASCII output style
//        for (T x : collection) {
//    ...
//            pb.step(); // step by 1
//            pb.stepBy(n); // step by n
//    ...
//            pb.stepTo(n); // step directly to n
//    ...
//            pb.maxHint(n);
//            // reset the max of this progress bar as n. This may be useful when the program
//            // gets new information about the current progress.
//            // Can set n to be less than zero: this means that this progress bar would become
//            // indefinite: the max would be unknown.
//    ...
//            pb.setExtraMessage("Reading..."); // Set extra message to display at the end of the bar
//        }
//    }
}

