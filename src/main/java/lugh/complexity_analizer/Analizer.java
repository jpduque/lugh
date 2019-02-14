package lugh.complexity_analizer;

import edu.umd.cs.findbugs.*;
import edu.umd.cs.findbugs.config.UserPreferences;
import lugh.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Analizer {


    public void runStaticAnalysis() {
        String projectPath = Main.class.getClassLoader().getResource("temp/src").getPath();
        List<File> files = new ArrayList<>();
        files = filemgmt(projectPath, files);
        for (File file : files) {
            compiler(file.getAbsolutePath());
        }
        analyze();
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

    private void analyze() {
        String templatesFolder = Main.class.getClassLoader().getResource("temp/src").getPath();
        UserPreferences userPreferences = UserPreferences.createDefaultUserPreferences();
        Project project = new Project();
        project.addFile(templatesFolder);
        project.setConfiguration(userPreferences);
        project.setProjectName("Lugh Analysis");
        XMLBugReporter bugReporter = new XMLBugReporter(project);
        DetectorFactoryCollection detectorFactoryCollection = new DetectorFactoryCollection();
        bugReporter.setPriorityThreshold(1);
        IFindBugsEngine fb = new FindBugs2();
        try {
            fb.setProject(project);
            fb.setDetectorFactoryCollection(detectorFactoryCollection);
            fb.setUserPreferences(userPreferences);
            fb.setBugReporter(bugReporter);
            fb.execute();
            fb.getBugReporter().getProjectStats().getTotalBugs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FindBugs2 fb2 = (FindBugs2) fb;
        Collection bugs = fb.getBugReporter().getBugCollection().getCollection();
        List temp = new ArrayList();
        temp.addAll(bugs);
        System.out.println("hello world");
//        fb
        System.out.println("Bug count" + fb.getBugCount());
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


