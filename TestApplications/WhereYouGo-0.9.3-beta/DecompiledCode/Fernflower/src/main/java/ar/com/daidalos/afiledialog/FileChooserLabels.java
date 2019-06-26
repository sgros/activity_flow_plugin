package ar.com.daidalos.afiledialog;

import java.io.Serializable;

public class FileChooserLabels implements Serializable {
   private static final long serialVersionUID = 1L;
   public String createFileDialogAcceptButton;
   public String createFileDialogCancelButton;
   public String createFileDialogMessage;
   public String createFileDialogTitle = null;
   public String labelAddButton = null;
   public String labelCancelButton = null;
   public String labelConfirmNoButton = null;
   public String labelConfirmYesButton = null;
   public String labelSelectButton = null;
   public String messageConfirmCreation = null;
   public String messageConfirmSelection = null;

   public FileChooserLabels() {
      this.createFileDialogTitle = null;
      this.createFileDialogAcceptButton = null;
      this.createFileDialogCancelButton = null;
   }
}
