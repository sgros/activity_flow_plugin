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
