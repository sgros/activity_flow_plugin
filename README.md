### Steps For Importing Gradle (Plugin) Projects Into IntelliJ IDEA

1. Start IntelliJ IDEA
2. Click on "Import Project"
3. Find folder containing your gradle project, navigate to file build.gradle, select it and click OK
4. In window "Import Project from Gradle" leave everything as is and click OK

### Create plugin jar file

1. Go to: Build > Build Project
2. Go to: View > Tool Windows > Gradle
3. In opened Gradle Tool go to: Tasks > intellij > buildPlugin
4. After completion jar file is located in: "path_to_project"/build/libs/

### Plugin installation

To install plugin, download plugin jar file located in release tab of this project (https://github.com/Crash10/activity_flow_plugin/releases). After downloading, in Android Studio go to: File > Settings > Plugins > Install Plugin from Disk and then find and select downloaded jar file. Restart Android Studio to apply changes.

### How to use plugin

