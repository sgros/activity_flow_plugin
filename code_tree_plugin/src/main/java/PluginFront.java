import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class PluginFront extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        PluginLogic pluginLogic = new PluginLogic();
        Project project = event.getProject();
        ProjectChooserDialog projectChooserDialog = new ProjectChooserDialog(project);

        if(projectChooserDialog.showAndGet()) {
            if(projectChooserDialog.getSelectedProject() == null)
                projectChooserDialog.setSelectedProject(project.getBasePath());
            int returnCode = pluginLogic.run(projectChooserDialog.getSelectedProject());
            switch (returnCode) {
                case 0:
                    break;
                case 1:
                    Messages.showMessageDialog(project, "AndroidManifest.xml doesn't exist in given project.", "Message", Messages.getInformationIcon());
                    break;
                case 2:
                    Messages.showMessageDialog(project, "Parsing of AndroidManifest.xml failed.\nProbable reason: main activity wasn't found.", "Message", Messages.getInformationIcon());
                    break;
                case 3:
                    Messages.showMessageDialog(project, "Creating navigation_graph.xml failed.", "Message", Messages.getInformationIcon());
                    break;
                case 4:
                    Messages.showMessageDialog(project, "Creating graphviz_graph.dot failed.", "Message", Messages.getInformationIcon());
                    break;
                default:
                    Messages.showMessageDialog(project, "Something went wrong.", "Message", Messages.getInformationIcon());
            }
        }
    }

}