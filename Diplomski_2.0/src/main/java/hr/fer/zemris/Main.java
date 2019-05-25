package main.java.hr.fer.zemris;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.*;
import javax.xml.parsers.*;

public class Main {

    private static class ActivityInfo {
        String name;
        String path;
        Set<String> actions;
    }

    //argument is path to application's source code, e.g. /home/user/AndroidApps/Application
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Wrong number of arguments.");
            System.exit(1);
        }

        String currentDirectory = args[0];
        List<ActivityInfo> activityList = new ArrayList<ActivityInfo>();

        //find AndroidManifest.xml
        String path = checkForAndroidManifest(currentDirectory);
        if (path == "") {
            System.out.println("AndroidManifest.xml doesn't exist in given directory.");
            System.exit(1);
        }

        //parse AndroidManifest.xml
        //add activities to list
        try {

            Document androidManifest = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            androidManifest.getDocumentElement().normalize();

            path = path.replace("AndroidManifest.xml", "java/");
            String packageName = androidManifest.getDocumentElement().getAttribute("package");

            //get all activities
            NodeList nodeList = androidManifest.getElementsByTagName("activity");

            int main = findMainActivity(nodeList, activityList);

            if (main == -1) {
                String mainActivity = findActivityAlias(androidManifest, activityList);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    if (element.getAttribute("android:name").equals(mainActivity))
                        main = i;
                }

                if (main == -1)
                    throw new Exception();
            }

            //fill activityList with activities and their paths
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (i != main) {
                    Element element = (Element) nodeList.item(i);
                    ActivityInfo activityInfo = new ActivityInfo();
                    activityInfo.name = element.getAttribute("android:name");
                    String checkPath = null;
                    if(activityInfo.name.startsWith(".") || !activityInfo.name.contains("."))
                        checkPath = path + packageName.replace('.', '/') + activityInfo.name.replace('.', '/');
                    else
                        checkPath = path + activityInfo.name.replace('.', '/');
                    if(new File(checkPath + ".java").exists() || new File(checkPath + ".kt").exists())
                        activityInfo.path = checkPath;
                    else
                        activityInfo.path = activityInfo.name;
                    activityList.add(activityInfo);
                } else {
                    Element element = (Element) nodeList.item(i);
                    ActivityInfo activityInfo = activityList.get(0);
                    String checkPath = null;
                    if(activityInfo.name.startsWith(".") || !activityInfo.name.contains("."))
                        checkPath = path + packageName.replace('.', '/') + activityInfo.name.replace('.', '/');
                    else
                        checkPath = path + activityInfo.name.replace('.', '/');
                    if(new File(checkPath + ".java").exists() || new File(checkPath + ".kt").exists())
                        activityInfo.path = checkPath;
                    else
                        throw new Exception();
                    activityList.set(0, activityInfo);
                }

            }
        } catch (Exception e) {
            System.out.println("XML parsing failed.");
            System.exit(1);
        }

        //parse code files
        for (int i = 0; i < activityList.size(); i++) {
            ActivityInfo activityInfo = activityList.get(i);

            //get file to search
            File file = new File(activityInfo.path + ".java");
            if(file.exists()) {
                activityInfo.actions = findCodePatterns(file, activityList.get(0).name);
                activityList.set(i, activityInfo);
            }
        }

        //check whether class is activity and correct destination names (from Activity.class to android:name)
        for (int i = 0; i < activityList.size(); i++) {
            ActivityInfo activityInfo = activityList.get(i);
            if (activityInfo.actions != null) {
                ArrayList<String> activityInfoActions = new ArrayList<>(activityInfo.actions);
                activityInfo.actions.clear();
                for (int j = 0; j < activityInfoActions.size(); j++)
                    if (activityInfoActions.get(j).contains("."))
                        activityInfo.actions.add(activityInfoActions.get(j));
                    else
                        for (int k = 0; k < activityList.size(); k++)
                            if (activityList.get(k).name.endsWith(activityInfoActions.get(j))) {
                                activityInfo.actions.add(activityList.get(k).name);
                                break;
                            }
            }
        }

        //create navigation graph
        createNavigationGraph(path, activityList);

        //create graphviz file (.dot)
        createGraphvizGraph(path, activityList);

    }


    public static String checkForAndroidManifest(String currentDirectory) {
        File checkFile = new File(currentDirectory, "AndroidManifest.xml");
        String returnValue = "";
        //check if AndroidManifest.xml is in current directory
        if (checkFile.exists())
            //recursion stops when path ends with "/src/main/AndroidManifest.xml"
            returnValue = checkFile.toString();
        //if not, list all files in directory and call yourself recursively on every child directory
        else {
            File files[] = new File(currentDirectory).listFiles();
            if (files != null)
                for (int i = 0; i < files.length; i++)
                    if (files[i].isDirectory())
                        if((returnValue = checkForAndroidManifest(files[i].getPath())).endsWith("/src/main/AndroidManifest.xml"))
                            break;
        }
        return returnValue;
    }


    //arguments: list of activity nodes and empty list of activities
    public static int findMainActivity(NodeList nodeList, List<ActivityInfo> activityList) {
        for (int i = 0; i < nodeList.getLength(); i++) {

            //get all intent-filter nodes
            Element element = (Element) nodeList.item(i);
            NodeList intentFilter = element.getElementsByTagName("intent-filter");

            if(intentFilter.getLength() > 0)
                for (int j = 0; j < intentFilter.getLength(); j++) {
                    //get child nodes of intent-filter
                    NodeList contents = intentFilter.item(j).getChildNodes();
                    for (int k = 0; k < contents.getLength(); k++) {
                        //odd nodes are meaningless text, even nodes are actual nodes
                        org.w3c.dom.Node node = contents.item(k);
                        if(node.hasAttributes()) {
                            Element elementChild = (Element) node;
                            if (elementChild.getAttribute("android:name").equals("android.intent.action.MAIN")) {
                                elementChild = (Element) contents.item(k + 2);
                                if (elementChild.getAttribute("android:name").equals("android.intent.category.LAUNCHER")) {
                                    ActivityInfo activityInfo = new ActivityInfo();
                                    activityInfo.name = element.getAttribute("android:name");
                                    activityList.add(activityInfo);
                                    return i;
                                }
                            }
                        }
                    }
                }
        }
        return -1;
    }


    public static String findActivityAlias(Document androidManifest, List<ActivityInfo> activityList) {
        NodeList nodeList = androidManifest.getElementsByTagName("activity-alias");
        for (int i = 0; i < nodeList.getLength(); i++) {

            //get all intent-filter nodes
            Element element = (Element) nodeList.item(i);
            NodeList intentFilter = element.getElementsByTagName("intent-filter");

            if(intentFilter.getLength() > 0)
                for (int j = 0; j < intentFilter.getLength(); j++) {
                    //get child nodes of intent-filter
                    NodeList contents = intentFilter.item(j).getChildNodes();
                    for (int k = 0; k < contents.getLength(); k++) {
                        //odd nodes are meaningless text, even nodes are actual nodes
                        org.w3c.dom.Node node = contents.item(k);
                        if(node.hasAttributes()) {
                            Element elementChild = (Element) node;
                            if (elementChild.getAttribute("android:name").equals("android.intent.action.MAIN")) {
                                elementChild = (Element) contents.item(k + 2);
                                if (elementChild.getAttribute("android:name").equals("android.intent.category.LAUNCHER")) {
                                    //add MainActivity to activityList through activity-alias attribute targetActivity
                                    ActivityInfo activityInfo = new ActivityInfo();
                                    activityInfo.name = element.getAttribute("android:targetActivity");
                                    activityList.add(activityInfo);
                                    return element.getAttribute("android:targetActivity");
                                }
                            }
                        }
                    }
                }
        }
        return null;
    }

    public static LinkedHashSet findCodePatterns(File file, String mainActivity) {
        LinkedHashSet activityInfoActions = new LinkedHashSet<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String unchangedLine, line, filePath = file.getPath();
            String activityName = filePath.substring((filePath.lastIndexOf('/') + 1), filePath.lastIndexOf('.'));
            boolean found = true;
            //detect activity with rules
            while ((unchangedLine = bufferedReader.readLine()) != null) {
                //detects code usage from other file (class)
                if (unchangedLine.contains(activityName + "$"))
                    if (unchangedLine.contains("new ") && unchangedLine.contains("(this")) {
                        //System.out.println(line.split("new ")[1].split("\\(this")[0]);
                        line = unchangedLine.split("new ")[1].split("\\(this")[0];

                        File newFile = new File(filePath.replace(activityName, line));
                        if(newFile.exists()) {
                            activityInfoActions.addAll(findCodePatterns(newFile, mainActivity));
                        }
                    }
                //detects activity in constructor new Intent
                if (unchangedLine.contains("new Intent(")) {
                    line = unchangedLine.trim().split("new Intent\\(")[1].trim();

                    //ignore empty constructor
                    if (line.startsWith(")"))
                        ;
                    //constant for main activity
                    else if (line.startsWith("Intent.ACTION_MAIN)") || line.startsWith("\"android.intent.action.MAIN\")")) {
                        activityInfoActions.add(mainActivity);
                    //split arguments in line
                    } else if (line.contains(",")) {
                        try {
                            line = line.split(".class")[0].trim();
                            String[] helper = line.split(",");
                            line = helper[helper.length - 1].trim();
                            //if activity name is given with full path
                            if(line.contains(".")) {
                                helper = line.split("\\.");
                                line = helper[helper.length - 1];
                            }
                            activityInfoActions.add(line);
                        //if constructor is written in multiple lines
                        } catch (ArrayIndexOutOfBoundsException e) {
                            while (true) {
                                line = bufferedReader.readLine();
                                if(line.contains(".class")) {
                                    activityInfoActions.add(line.trim().split(".class")[0].trim());
                                    break;
                                }
                            }
                        }
                    }
                }
                //detects activity in method setClassName
                if (unchangedLine.contains(".setClassName(")) {
                    found = false;
                    line = unchangedLine.trim().split(".setClassName\\(")[1];
                    try {
                        if (line.contains(".class"))
                            line = line.split(",")[1].trim().split("\\)")[0].trim().split(".class")[0];
                        else {
                            String[] helper = line.split(",")[1].trim().split("\\)")[0].trim().replaceAll("\"", "").split("\\.");
                            line = helper[helper.length - 1];
                        }
                        activityInfoActions.add(line);
                    //if method is written in multiple lines
                    } catch (ArrayIndexOutOfBoundsException e) {
                        line = bufferedReader.readLine().trim();
                        if (line.contains(".class"))
                            line = line.split("\\)")[0].trim().split(".class")[0];
                        else {
                            String[] helper = line.split("\\)")[0].trim().replaceAll("\"", "").split("\\.");
                            line = helper[helper.length - 1];
                        }
                        activityInfoActions.add(line);
                    }
                }
                //detects activity in method setClass
                if (unchangedLine.contains(".setClass(")) {
                    found = false;
                    line = unchangedLine.trim().split(".setClass\\(")[1];
                    try {
                        if (line.contains(".class"))
                            line = line.split(",")[1].trim().split("\\)")[0].trim().split(".class")[0];
                        else {
                            String[] helper = line.split(",")[1].trim().split("\\)")[0].trim().replaceAll("\"", "").split("\\.");
                            line = helper[helper.length - 1];
                        }
                        activityInfoActions.add(line);
                    //if method is written in multiple lines
                    } catch (ArrayIndexOutOfBoundsException e) {
                        line = bufferedReader.readLine().trim();
                        if (line.contains(".class"))
                            line = line.split("\\)")[0].trim().split(".class")[0];
                        else {
                            String[] helper = line.split("\\)")[0].trim().replaceAll("\"", "").split("\\.");
                            line = helper[helper.length - 1];
                        }
                        activityInfoActions.add(line);
                    }
                }
            }

            bufferedReader.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Code search for intent failed." + e.getCause());
            System.exit(1);
        } finally {
            return activityInfoActions;
        }
    }

    public static void createNavigationGraph(String path, List<ActivityInfo> activityList) {
        try {
            //graph is created in path: path_to_application/app/src/main/res/navigation/navigation_graph.xml
            String pathToNavigationGraph = path.replace("java", "res/navigation");
            File directory = new File(pathToNavigationGraph);
            if (!directory.exists())
                directory.mkdir();
            pathToNavigationGraph += "navigation_graph.xml";
            File navigationGraphFile = new File(pathToNavigationGraph);
            if (!navigationGraphFile.createNewFile())
                throw new Exception();

            FileWriter fileWriter = new FileWriter(pathToNavigationGraph);
            fileWriter.write(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "<navigation xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                            "\txmlns:app=\"http://schemas.android.com/apk/res-auto\" android:id=\"@+id/graph\"\n" +
                            "\txmlns:tools=\"http://schemas.android.com/tools\"\n" +
                            "\tapp:startDestination=\"@id/" + activityList.get(0).name + "\">\n"
            );

            for (int i = 0; i < activityList.size(); i++) {
                ActivityInfo activityInfo = activityList.get(i);
                String activityInfoName = activityInfo.name.replaceAll("\\.", "_");
                fileWriter.write(
                        "\t<fragment\n" +
                                "\t\tandroid:id=\"@+id/" + activityInfoName + "\"\n" +
                                "\t\tandroid:name=\"" + activityInfo.name + "\"\n" +
                                "\t\tandroid:label=\"" + activityInfoName + "\"\n" +
                                "\t\ttools:layout=\"@layout/" + activityInfoName + "\" >\n"
                );

                if (activityInfo.actions != null) {
                    ArrayList<String> activityInfoActions = new ArrayList<>(activityInfo.actions);
                    for (int j = 0; j < activityInfoActions.size(); j++)
                        fileWriter.write(
                                "\t\t<action\n" +
                                        "\t\t\tandroid:id=\"@+id/action_" + activityInfoName + "_to_" + activityInfoActions.get(j).replaceAll("\\.", "_") + "\"\n" +
                                        "\t\t\tapp:destination=\"@id/" + activityInfoActions.get(j).replaceAll("\\.", "_") + "\" />\n"
                        );
                }

                fileWriter.write("\t</fragment>\n");
            }

            fileWriter.write("</navigation>");
            fileWriter.close();

        } catch (Exception e) {
            System.out.println("Creating navigation_graph.xml failed.");
            //System.exit(1);
        }
    }

    public static void createGraphvizGraph(String path, List<ActivityInfo> activityList) {
        try {
            //graph is created in path: path_to_application/app/src/main/res/navigation/graphviz_graph.dot
            String pathToGraphvizGraph = path.replace("java", "res/navigation");
            File directory = new File(pathToGraphvizGraph);
            if (!directory.exists())
                directory.mkdir();
            pathToGraphvizGraph += "graphviz_graph.dot";
            File graphvizGraphFile = new File(pathToGraphvizGraph);
            if (!graphvizGraphFile.createNewFile())
                throw new Exception();

            FileWriter fileWriter = new FileWriter(pathToGraphvizGraph);
            fileWriter.write(
                    "digraph application_graph {\n" +
                            "\tsize=\"15\"\n" +
                            "\tnode [shape = doublecircle]; " + activityList.get(0).name.replaceAll("\\.", "_") + "\n" +
                            "\tnode [shape = circle];\n"
            );

            for (int i = 0; i < activityList.size(); i++) {
                ActivityInfo activityInfo = activityList.get(i);
                String activityInfoName = activityInfo.name.replaceAll("\\.", "_");
                if (activityInfo.actions != null) {
                    ArrayList<String> activityInfoActions = new ArrayList<>(activityInfo.actions);
                    for (int j = 0; j < activityInfoActions.size(); j++)
                        fileWriter.write(
                                "\t" + activityInfoName + " -> " + activityInfoActions.get(j).replaceAll("\\.", "_") + ";\n"
                        );
                }
            }

            fileWriter.write("}");
            fileWriter.close();

        } catch (Exception e) {
            System.out.println("Creating graphviz_graph.dot failed.");
            //System.exit(1);
        }
    }

}