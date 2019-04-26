package lugh.patternDetection;

import lugh.Main;
import lugh.patternDetection.parser.ClassObject.Abstraction;
import lugh.patternDetection.parser.Connection;
import lugh.patternDetection.parser.ProjectASTParser;
import lugh.patternDetection.patterns.Pattern;
import lugh.patternDetection.patterns.PatternDetectionAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@SuppressWarnings("serial")
public class Detector {

    public static String patternfolder, projectfolder, exportfolder;
    private File folder;
    public static Pattern p = new Pattern("");
    public static boolean parseoccured = false;

    public static int MemberNum, ConnNum;
    public static String PatternName;

    /**
     * Prints the help message of the command line interface.
     */
    private static void printHelpMessage() {
        System.out.println("DP-CORE: Design Pattern Detection Tool for Code Reuse\n"
                + "Run without arguments for GUI mode.\n" + "");
        System.out.println("For batch mode run as\njava -jar DP-CORE.jar -project=\"path/to/project\" "
                + "-pattern=\"path/to/pattern\" -group=true|false where the last argument allows "
                + "grouping in hypercandidates (default is true)");
    }

    /**
     * Parses the command line arguments.
     *
     * @param args the arguments to be parsed.
     * @return a string with the values of the arguments.
     */
    public static String[] parseArgs(String[] args) {
        List<String> col = new ArrayList<String>();
        for (String arg : args) {
            String narg = arg.trim();
            if (narg.contains("=")) {
                for (String n : narg.split("=")) {
                    col.add(n);
                }
            } else
                col.add(arg.trim());
        }
        boolean sproject = false;
        boolean spattern = false;
        boolean sgroup = false;
        String project = "";
        String pattern = "";
        String stringGroup = "";
        for (String c : col) {
            if (c.startsWith("-project")) {
                sproject = true;
                spattern = false;
                sgroup = false;
            } else if (c.startsWith("-pattern")) {
                sproject = false;
                spattern = true;
                sgroup = false;
            } else if (c.startsWith("-group")) {
                sproject = false;
                spattern = false;
                sgroup = true;
            } else {
                if (sproject)
                    project += c + " ";
                else if (spattern)
                    pattern += c + " ";
                else if (sgroup)
                    stringGroup += c + " ";
            }
        }
        project = project.trim();
        pattern = pattern.trim();
        return new String[]{project.trim(), pattern.trim(), stringGroup.trim().toLowerCase()};
    }


    private int dpcount = 0;

    public int getDpcount() {
        return dpcount;
    }

    public void runAnalysis(String project, String pattern) {
        boolean group = false;
        ProjectASTParser.parse(project);
        Pattern pat = Detector.extractPattern(new File(pattern));
        String s = PatternDetectionAlgorithm.DetectPattern_Results(pat, group);
        createClassesFile(PatternDetectionAlgorithm.DetectPattern_File(pat, group));
//        System.out.println(s);
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        m.find();
        dpcount = Integer.parseInt(m.group(0));
    }



    /**
     * Parses a .pattern file to create a Pattern Object.
     *
     * @param file file to be parsed
     * @return Returns a Pattern Object created through parsing
     */
    public static Pattern extractPattern(File file) {
        String name = file.getName();
        Pattern p;
        int phase = 1;
        if (name.substring(name.lastIndexOf(".")).equalsIgnoreCase(".pattern")) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                p = new Pattern(br.readLine());
                while ((line = br.readLine()) != null) {
                    // process the line.
                    if (phase == 1 && !(line.equals("End_Members"))) {
                        String[] parts = line.split(" ");
                        String s = "";
                        for (int i = 2; i < parts.length; i++) {
                            s += parts[i];
                            if (!(i == parts.length - 1)) {
                                s += " ";
                            }
                        }
                        p.insert_member(parts[0], StringtoAbstraction(parts[1]), s);
                    } else if (line.equals("End_Members")) {
                        phase = 2;
                    } else if (phase == 2 && !(line.equals("End_Connections"))) {
                        String[] parts = line.split(" ");
                        p.insert_connection(parts[0], StringtoConnectionType(parts[1]), parts[2]);
                    } else {
                    }
                }
                return p;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    /**
     * Converts the input string to the equivalent Connection Type
     *
     * @param string String representing a Connection Type
     * @return Returns a Connection Type after processing input String
     */
    public static Connection.Type StringtoConnectionType(String string) {
        Connection.Type t;
        switch (string) {
            case "uses":
                t = Connection.Type.uses;
                break;
            case "inherits":
                t = Connection.Type.inherits;
                break;
            case "creates":
                t = Connection.Type.creates;
                break;
            // Either interface or abstract
            case "calls":
                t = Connection.Type.calls;
                break;
            case "references":
                t = Connection.Type.references;
                break;
            case "has":
                t = Connection.Type.has;
                break;
            default:
                t = null;
        }
        return t;
    }

    /**
     * Converts the input string to the equivalent Abstraction Type
     *
     * @param string String representing an Abstraction Type
     * @return Returns an Abstraction Type after processing the input String
     */
    public static Abstraction StringtoAbstraction(String string) {
        Abstraction abs;
        switch (string) {
            case "Normal":
                abs = Abstraction.Normal;
                break;
            case "Interface":
                abs = Abstraction.Interface;
                break;
            case "Abstract":
                abs = Abstraction.Abstract;
                break;
            // Either interface or abstract
            case "Abstracted":
                abs = Abstraction.Abstracted;
                break;
            case "Any":
                abs = Abstraction.Any;
                break;
            default:
                abs = null;
        }
        return abs;
    }

    private void createClassesFile(List<String> clazzDetected) {
        String data = StringUtils.join(clazzDetected);
        data = data.replaceAll("\\[","");
        data = data.replaceAll("\\]","");
        String path = System.getProperty("user.dir")+"/temp/files.txt";
        try {
            Files.write(Paths.get(path), data.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
