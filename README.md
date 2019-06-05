### IntelliJ IDEA Installation And Steps For Importing Gradle (Plugin) Projects

0. Prerequisites: Installed JRE and JDK
1. Run idea.sh
2. Click on "Import Project"
3. Find folder containing your gradle project, navigate to file build.gradle, select it and click OK
4. In window "Import Project from Gradle" leave everything as is and click OK

### Create plugin jar file

1. Go to: Build > Build Project
2. Go to: View > Tool Windows > Gradle
3. In opened Gradle Tool go to: Tasks > intellij > buildPlugin
4. After completion jar file is located in: "path_to_project"/build/libs/

### Plugin installation

To install plugin, download plugin jar file located in release tab of this project (https://github.com/Crash10/code_tree_plugin/releases). After downloading, in Android Studio go to: File > Settings > Plugins > Install Plugin from Disk and then find and select downloaded jar file. Restart Android Studio to apply changes.

### Using plugin

Go to: Plugin > Create code tree. In opened dialog select project folder (default folder is current project) you want to process. After clicking OK, plugin will start to work. If successful, plugin will create folder "navigation" with files "navigation_graph.xml" and "graphviz_graph.dot" in application's folder "res".

### Applications for testing

In folder "TestApplications" of this project are three applications for plugin testing. For each application, source code and decompiled code (three versions, decompiled with Fernflower, JADX and Procyon) are uploaded.
To test plugin on these applications, download them and follow the instructions in section "Using plugin".
