package lugh.complexity_analizer;

import edu.umd.cs.findbugs.*;
import edu.umd.cs.findbugs.config.UserPreferences;
import lugh.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Analizer {


    public static void main(String[] args) throws Exception {
        String projectPath = Main.class.getClassLoader().getResource("temp/src").getPath();
        List<File> files = new ArrayList<>();
        files = filemgmt(projectPath, files);
        for (File file : files) {
            compiler(file.getAbsolutePath());
        }

        analyze();


    }

    public static void compiler(String file) {
        String folder = "javac " + file;
        ProcessBuilder processBuilder = new ProcessBuilder();
        if(!System.getProperty("os.name").contains("Windows"))
            processBuilder.command("bash", "-c", folder + "/*.java");
        else processBuilder.command("cmd.exe", "/C", folder + "/*.java");
        try {

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));


            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();

            if (exitVal == 0) {
                System.out.println("Success!");
//                System.out.println(output);
            }

             else {
                int len;
                if ((len = process.getErrorStream().available()) > 0) {
                    byte[] buf = new byte[len];
                    process.getErrorStream().read(buf);
                    System.err.println("Command error:\t\"" + new String(buf) + "\"");
                }
            }
    } catch(
    IOException e)

    {
        e.printStackTrace();
    } catch(
    InterruptedException e)

    {
        e.printStackTrace();
    }

}

    public static void analyze() throws Exception {

        String templatesFolder = Main.class.getClassLoader().getResource("temp/src").getPath();
        List<File> classes = new ArrayList<>();
        classes = filemgmt(templatesFolder, classes);


        UserPreferences userPreferences = UserPreferences.createDefaultUserPreferences();

        Project project = new Project();
        project.addFile(templatesFolder);
        project.setConfiguration(userPreferences);
        project.setProjectName("Lugh Analysis");

        XMLBugReporter bugReporter = new XMLBugReporter(project);
        DetectorFactoryCollection detectorFactoryCollection = new DetectorFactoryCollection();
        bugReporter.setPriorityThreshold(3);

        try {
            IFindBugsEngine fb = new FindBugs2();
            fb.setProject(project);
            fb.setDetectorFactoryCollection(detectorFactoryCollection);
            fb.setUserPreferences(userPreferences);
            fb.setBugReporter(bugReporter);
            fb.execute();
            fb.getBugReporter().getProjectStats().getTotalBugs();
            System.out.println("Bug count" + fb.getBugCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<File> filemgmt(String path, List<File> files) throws Exception {
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


