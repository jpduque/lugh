package lugh.complexity_analizer;

//import edu.umd.cs.findbugs.;
import edu.umd.cs.findbugs.*;
import edu.umd.cs.findbugs.config.UserPreferences;
import lugh.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Analizer {


    public Map<String, Integer> runStaticAnalysis() {
        String projectPath = System.getProperty("user.dir")+"/temp/src";
        List<File> files = new ArrayList<>();
        files = filemgmt(projectPath, files);
        for (File file : files) {
            compiler(file.getAbsolutePath());
        }
        return analyze();
    }

    private void compiler(String file) {
        String folder = "javac " + file;
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (!System.getProperty("os.name").contains("Windows"))
            processBuilder.command("bash", "-c", folder + "/*.java");
        else processBuilder.command("cmd.exe", "/C", folder + "/*.java");
        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitVal = process.waitFor();
            if (exitVal != 0) {
                int len;
                if ((len = process.getErrorStream().available()) > 0) {
                    byte[] buf = new byte[len];
                    process.getErrorStream().read(buf);
                    System.err.println("Command error:\t\"" + new String(buf) + "\"");
                }
            }
        } catch (
                IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Map<String, Integer> analyze() {
        String templatesFolder = System.getProperty("user.dir")+"/temp/src";
        UserPreferences userPreferences = UserPreferences.createDefaultUserPreferences();
        Project project = new Project();
        project.addFile(templatesFolder);
        project.setConfiguration(userPreferences);
        project.setProjectName("Lugh Analysis");
        BugReporter bugReporter = new BugCollectionBugReporter(project);
        DetectorFactoryCollection detectorFactoryCollection = new DetectorFactoryCollection();
//        bugReporter.setIsRelaxed(false);
        bugReporter.setPriorityThreshold(4);
        IFindBugsEngine fb = new FindBugs2();
        try {
            fb.setProject(project);
            fb.setDetectorFactoryCollection(detectorFactoryCollection);
            fb.setUserPreferences(userPreferences);
//            bugReporter.setReportStackTrace(false);
//            bugReporter.setAddMessages(false);
//            bugReporter.setReportStackTrace(false);

            fb.setBugReporter(bugReporter);
            fb.getBugReporter().setErrorVerbosity(0);
            fb.execute();
            fb.getBugReporter().finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bugReporter.finish();
        Collection bugs = fb.getBugReporter().getBugCollection().getCollection();
        List temp = new ArrayList();
        temp.addAll(bugs);
        Map<String, Integer> bugClassifier = new HashMap<>();
        for(Categories categ : Categories.values()) {
            bugClassifier.put(String.valueOf(categ),0);
        }
        for(Object buggy : temp){
            BugInstance bugDetected = (BugInstance) buggy;
            String bugCategory = bugDetected.getBugPattern().getCategory();
            bugClassifier.put(bugCategory,bugClassifier.get(bugCategory) + 1);
        }
        return bugClassifier;
    }

    private List<File> filemgmt(String path, List<File> files) {
        File directory = new File(path);
        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.isFile() && file.getName().contains(".java")) {
                    if (!files.contains(file.getParentFile())) {
                        files.add(file.getParentFile());
                    }
                } else if (file.isDirectory()) {
                    filemgmt(file.getAbsolutePath(), files);
                }
            }
        return files;
    }
}


