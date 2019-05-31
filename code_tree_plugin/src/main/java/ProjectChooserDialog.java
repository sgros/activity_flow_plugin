import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectChooserDialog extends DialogWrapper {

    private Project project;
    private JPanel jPanel;
    private FileChooserDescriptor fileChooserDescriptor;
    private TextFieldWithBrowseButton textFieldWithBrowseButton;
    //private JFileChooser jFileChooser;
    private String SelectedProject;

    public ProjectChooserDialog(Project project) {
        super(true); // use current window as parent
        this.project = project;
        init();
        setTitle("Choose Project");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        jPanel = new JPanel(new BorderLayout());
        jPanel.setSize(new Dimension(400, 30));
        jPanel.setMaximumSize(new Dimension(400, 30));
        jPanel.setMinimumSize(new Dimension(400, 30));

        fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);

        textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        textFieldWithBrowseButton.setText(project.getBasePath());
        textFieldWithBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSelectedProject(textFieldWithBrowseButton.getText());
            }
        });

        textFieldWithBrowseButton.addBrowseFolderListener("Choose Location Folder", null, null, fileChooserDescriptor);
        jPanel.add(textFieldWithBrowseButton);
        return jPanel;
    }

    public String getSelectedProject() {
        return SelectedProject;
    }

    public void setSelectedProject(String selectedProject) {
        this.SelectedProject = selectedProject;
    }

}