package hr.fer.zemris;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.*;
import javax.xml.parsers.*;

//jar file: https://mvnrepository.com/artifact/com.github.javaparser
//javadoc: https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/3.13.9
import com.github.javaparser.ast.expr.MethodCallExpr;
import static com.github.javaparser.StaticJavaParser.parse;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.Node;

public class Main {

    private static ArrayList<MethodCallExpr> methodList2 = new ArrayList<MethodCallExpr>();

    private static class ActivityInfo {
        String name;
        String path;
        Set<String> actions;
    }

    //override GenericListVisitorAdapter method visit to return list of startActivity calls in java file
    private static class MethodVisitor extends GenericListVisitorAdapter {
        @Override
        public List<MethodCallExpr> visit(MethodCallExpr n, Object arg) {
            ArrayList<MethodCallExpr> methodList = new ArrayList<MethodCallExpr>();
            super.visit(n, arg);
            if (n.getName().asString().equals("startActivity")) {
                //System.out.println("[" + n.getName() + "] " + n + " (ARG " + n.getArguments() + ")");
                methodList.add(n);
                methodList2.add(n);
            }
            return methodList;
        }
    }

    //override GenericListVisitorAdapter method visit to return list of startActivity calls in java file
    private static class VariableVisitor extends GenericListVisitorAdapter {
        @Override
        public List<VariableDeclarationExpr> visit(VariableDeclarationExpr n, Object arg) {
            ArrayList<VariableDeclarationExpr> variableList = new ArrayList<VariableDeclarationExpr>();
            super.visit(n, arg);
            //System.out.println(n.getVariables());
            variableList.add(n);
            return variableList;
        }
    }

    //argument is path to application's source code, e.g. /home/user/AndroidApps/Application
    public static void main(String[] args) {
        //String currentDirectory = System.getProperty("user.dir");
        String currentDirectory = args[0];
        List<ActivityInfo> activityList = new ArrayList<ActivityInfo>();

        //print current directory
        //System.out.println("Current working directory: " + currentDirectory);

        //find AndroidManifest.xml
        String path = checkForAndroidManifest(currentDirectory);
        if (path == "") {
            System.out.println("AndroidManifest.xml doesn't exist in given directory.");
            System.exit(1);
        }

        //System.out.println(path.replace("AndroidManifest.xml", "java/"));

        //parse AndroidManifest.xml
        //add activities to list
        try {

            Document androidManifest = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
            androidManifest.getDocumentElement().normalize();

            path = path.replace("AndroidManifest.xml", "java/");
            String packageName = androidManifest.getDocumentElement().getAttribute("package");
            //System.out.println("Package: " + packageName);

            //get all activities
            NodeList nodeList = androidManifest.getElementsByTagName("activity");

            //System.out.println("----------------------------");

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
                    if(new File(checkPath + ".java").exists())
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
                    if(new File(checkPath + ".java").exists())
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
            //System.out.println((i + 1) + " Id: " + activityInfo.path);
            //System.out.println("    " + activityInfo.name);

            //get file to parse
            File file = new File(activityInfo.path + ".java");
            if(file.exists()) {
                activityInfo.actions = new LinkedHashSet<>();
                try {
                    //get all method calls
                    List<MethodCallExpr> methodCallList = new MethodVisitor().visit(parse(file), null);
                    //get all variable declarations
                    List<VariableDeclarationExpr> variableList = new VariableVisitor().visit(parse(file), null);

                    //detect what activity is being started with current rules
                    for (int j = 0; j < methodList2.size(); j++) {
                        Node node = methodList2.get(j).getArguments().get(0);
                        if (node.toString().contains("new Intent()")) {
                            //System.out.println("setClassName");
                            activityInfo.actions.add("setClassName");
                        } else if (node.toString().startsWith("new Intent(Intent.ACTION_MAIN)")) {
                            activityInfo.actions.add(activityList.get(0).name);
                            //System.out.println("ACTION_MAIN");
                        } else if(node.toString().contains("new Intent(")) {
                            activityInfo.actions.add(node.getChildNodes().get(node.getChildNodes().size() - 1).toString());
                            //System.out.println(node.getChildNodes().get(node.getChildNodes().size() - 1).toString());
                        } else {
                            for (int k = 0; k < variableList.size(); k++)
                                if(variableList.get(k).getVariables().get(0).getName().asString().equals(node.toString())) {
                                    List<Node> node2 = variableList.get(k).getVariables().get(0).getChildNodes();
                                    if (node2.get(node2.size()-1).toString().contains("new Intent()")) {
                                        //System.out.println("setClassName");
                                        continue;
                                    } else if(node2.get(node2.size()-1).toString().contains("new Intent(")) {
                                        List<Node> node3 = node2.get(node2.size() - 1).getChildNodes();
                                        //System.out.println(node3.get(node3.size() - 1));
                                        activityInfo.actions.add(node3.get(node3.size() - 1).toString());
                                        variableList.remove(k);
                                        break;
                                    }
                                }
                        }
                    }
                    methodList2.clear();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Source code parsing failed.");
                    System.exit(1);
                }
                activityList.set(i, activityInfo);
            }
        }

        //correct destination names (from Activity.class to android:name)
        for (int i = 0; i < activityList.size(); i++) {
            ActivityInfo activityInfo = activityList.get(i);
            if (activityInfo.actions != null) {
                ArrayList<String> activityInfoActions = new ArrayList<>(activityInfo.actions);
                activityInfo.actions.clear();
                for (int j = 0; j < activityInfoActions.size(); j++)
                    if (activityInfoActions.get(j).contains(".class")) {
                        for (int k = 0; k < activityList.size(); k++)
                            if (activityList.get(k).name.endsWith(activityInfoActions.get(j).replace(".class", "")))
                                activityInfo.actions.add(activityList.get(k).name);
                    } else
                        activityInfo.actions.add(activityInfoActions.get(j));
            }
        }

        //create navigation graph
        try {
            //graph is created in path: path_to_application/app/src/main/res/navigation/navigation_graph.xml
            /*String pathToNavigationGraph = "navigation_graph.xml";*/
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
            System.exit(1);
        }
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
}