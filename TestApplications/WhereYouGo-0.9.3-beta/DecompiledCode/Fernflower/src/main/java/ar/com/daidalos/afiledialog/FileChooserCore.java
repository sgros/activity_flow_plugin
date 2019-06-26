package ar.com.daidalos.afiledialog;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import ar.com.daidalos.afiledialog.view.FileItem;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class FileChooserCore {
   private static File defaultFolder = null;
   private OnClickListener addButtonClickListener = new OnClickListener() {
      public void onClick(View var1) {
         Context var2 = var1.getContext();
         Builder var3 = new Builder(var2);
         int var4;
         if (FileChooserCore.this.folderMode) {
            var4 = R.string.daidalos_create_folder;
         } else {
            var4 = R.string.daidalos_create_file;
         }

         String var5 = var2.getString(var4);
         String var8 = var5;
         if (FileChooserCore.this.labels != null) {
            var8 = var5;
            if (FileChooserCore.this.labels.createFileDialogTitle != null) {
               var8 = FileChooserCore.this.labels.createFileDialogTitle;
            }
         }

         if (FileChooserCore.this.folderMode) {
            var4 = R.string.daidalos_enter_folder_name;
         } else {
            var4 = R.string.daidalos_enter_file_name;
         }

         String var6 = var2.getString(var4);
         var5 = var6;
         if (FileChooserCore.this.labels != null) {
            var5 = var6;
            if (FileChooserCore.this.labels.createFileDialogMessage != null) {
               var5 = FileChooserCore.this.labels.createFileDialogMessage;
            }
         }

         if (FileChooserCore.this.labels != null && FileChooserCore.this.labels.createFileDialogAcceptButton != null) {
            var6 = FileChooserCore.this.labels.createFileDialogAcceptButton;
         } else {
            var6 = var2.getString(R.string.daidalos_accept);
         }

         String var7;
         if (FileChooserCore.this.labels != null && FileChooserCore.this.labels.createFileDialogCancelButton != null) {
            var7 = FileChooserCore.this.labels.createFileDialogCancelButton;
         } else {
            var7 = var2.getString(R.string.daidalos_cancel);
         }

         var3.setTitle(var8);
         var3.setMessage(var5);
         final EditText var9 = new EditText(var2);
         var9.setSingleLine();
         var3.setView(var9);
         var3.setPositiveButton(var6, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               String var3 = var9.getText().toString();
               if (var3 != null && var3.length() > 0) {
                  FileChooserCore.this.notifyFileListeners(FileChooserCore.this.currentFolder, var3);
               }

            }
         });
         var3.setNegativeButton(var7, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
            }
         });
         var3.show();
      }
   };
   private boolean canCreateFiles;
   private OnClickListener cancelButtonClickListener = new OnClickListener() {
      public void onClick(View var1) {
         FileChooserCore.this.notifyCancelListeners();
      }
   };
   private List cancelListeners;
   private FileChooser chooser;
   private File currentFolder;
   private FileItem.OnFileClickListener fileItemClickListener = new FileItem.OnFileClickListener() {
      public void onClick(FileItem var1) {
         File var2 = var1.getFile();
         if (var2.isDirectory()) {
            FileChooserCore.this.loadFolder(var2);
         } else {
            FileChooserCore.this.notifyFileListeners(var2, (String)null);
         }

      }
   };
   private List fileSelectedListeners;
   private String filter;
   private boolean folderMode;
   private FileChooserLabels labels;
   private OnClickListener okButtonClickListener = new OnClickListener() {
      public void onClick(View var1) {
         FileChooserCore.this.notifyFileListeners(FileChooserCore.this.currentFolder, (String)null);
      }
   };
   private boolean showCancelButton;
   private boolean showConfirmationOnCreate;
   private boolean showConfirmationOnSelect;
   private boolean showFullPathInTitle;
   private boolean showOnlySelectable;

   public FileChooserCore(FileChooser var1) {
      this.chooser = var1;
      this.fileSelectedListeners = new LinkedList();
      this.cancelListeners = new LinkedList();
      this.filter = null;
      this.showOnlySelectable = false;
      this.setCanCreateFiles(false);
      this.setFolderMode(false);
      this.currentFolder = null;
      this.labels = null;
      this.showConfirmationOnCreate = false;
      this.showConfirmationOnSelect = false;
      this.showFullPathInTitle = false;
      this.showCancelButton = false;
      LinearLayout var2 = this.chooser.getRootLayout();
      ((Button)var2.findViewById(R.id.buttonAdd)).setOnClickListener(this.addButtonClickListener);
      ((Button)var2.findViewById(R.id.buttonOk)).setOnClickListener(this.okButtonClickListener);
      ((Button)var2.findViewById(R.id.buttonCancel)).setOnClickListener(this.cancelButtonClickListener);
   }

   private void notifyCancelListeners() {
      for(int var1 = 0; var1 < this.cancelListeners.size(); ++var1) {
         ((FileChooserCore.OnCancelListener)this.cancelListeners.get(var1)).onCancel();
      }

   }

   private void notifyFileListeners(final File var1, final String var2) {
      final boolean var3;
      if (var2 != null && var2.length() > 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      int var8;
      if (var3 && this.showConfirmationOnCreate || !var3 && this.showConfirmationOnSelect) {
         Context var4 = this.chooser.getContext();
         Builder var5 = new Builder(var4);
         String var6;
         if (this.labels != null && (var3 && this.labels.messageConfirmCreation != null || !var3 && this.labels.messageConfirmSelection != null)) {
            if (var3) {
               var6 = this.labels.messageConfirmCreation;
            } else {
               var6 = this.labels.messageConfirmSelection;
            }
         } else if (this.folderMode) {
            if (var3) {
               var8 = R.string.daidalos_confirm_create_folder;
            } else {
               var8 = R.string.daidalos_confirm_select_folder;
            }

            var6 = var4.getString(var8);
         } else {
            if (var3) {
               var8 = R.string.daidalos_confirm_create_file;
            } else {
               var8 = R.string.daidalos_confirm_select_file;
            }

            var6 = var4.getString(var8);
         }

         String var7 = var6;
         if (var6 != null) {
            if (var2 != null) {
               var7 = var2;
            } else {
               var7 = var1.getName();
            }

            var7 = var6.replace("$file_name", var7);
         }

         if (this.labels != null && this.labels.labelConfirmYesButton != null) {
            var6 = this.labels.labelConfirmYesButton;
         } else {
            var6 = var4.getString(R.string.daidalos_yes);
         }

         String var9;
         if (this.labels != null && this.labels.labelConfirmNoButton != null) {
            var9 = this.labels.labelConfirmNoButton;
         } else {
            var9 = var4.getString(R.string.daidalos_no);
         }

         var5.setMessage(var7);
         var5.setPositiveButton(var6, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface var1x, int var2x) {
               for(var2x = 0; var2x < FileChooserCore.this.fileSelectedListeners.size(); ++var2x) {
                  if (var3) {
                     ((FileChooserCore.OnFileSelectedListener)FileChooserCore.this.fileSelectedListeners.get(var2x)).onFileSelected(var1, var2);
                  } else {
                     ((FileChooserCore.OnFileSelectedListener)FileChooserCore.this.fileSelectedListeners.get(var2x)).onFileSelected(var1);
                  }
               }

            }
         });
         var5.setNegativeButton(var9, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
            }
         });
         var5.show();
      } else {
         for(var8 = 0; var8 < this.fileSelectedListeners.size(); ++var8) {
            if (var3) {
               ((FileChooserCore.OnFileSelectedListener)this.fileSelectedListeners.get(var8)).onFileSelected(var1, var2);
            } else {
               ((FileChooserCore.OnFileSelectedListener)this.fileSelectedListeners.get(var8)).onFileSelected(var1);
            }
         }
      }

   }

   private void updateButtonsLayout() {
      byte var1 = 0;
      LinearLayout var2 = this.chooser.getRootLayout();
      View var3 = var2.findViewById(R.id.buttonAdd);
      byte var4;
      if (this.canCreateFiles) {
         var4 = 0;
      } else {
         var4 = 8;
      }

      var3.setVisibility(var4);
      var3 = var2.findViewById(R.id.buttonOk);
      if (this.folderMode) {
         var4 = 0;
      } else {
         var4 = 8;
      }

      var3.setVisibility(var4);
      View var5 = var2.findViewById(R.id.buttonCancel);
      if (this.showCancelButton) {
         var4 = var1;
      } else {
         var4 = 8;
      }

      var5.setVisibility(var4);
   }

   public void addListener(FileChooserCore.OnCancelListener var1) {
      this.cancelListeners.add(var1);
   }

   public void addListener(FileChooserCore.OnFileSelectedListener var1) {
      this.fileSelectedListeners.add(var1);
   }

   public File getCurrentFolder() {
      return this.currentFolder;
   }

   public void loadFolder() {
      this.loadFolder(defaultFolder);
   }

   public void loadFolder(File var1) {
      LinearLayout var2 = (LinearLayout)this.chooser.getRootLayout().findViewById(R.id.linearLayoutFiles);
      var2.removeAllViews();
      if (var1 != null && var1.exists()) {
         this.currentFolder = var1;
      } else if (defaultFolder != null) {
         this.currentFolder = defaultFolder;
      } else {
         this.currentFolder = Environment.getExternalStorageDirectory();
      }

      if (this.currentFolder.exists() && var2 != null) {
         LinkedList var3 = new LinkedList();
         if (this.currentFolder.getParent() != null) {
            var1 = new File(this.currentFolder.getParent());
            if (var1.exists()) {
               var3.add(new FileItem(this.chooser.getContext(), var1, ".."));
            }
         }

         int var4;
         if (!this.currentFolder.isDirectory()) {
            var3.add(new FileItem(this.chooser.getContext(), this.currentFolder));
         } else {
            File[] var7 = this.currentFolder.listFiles();
            if (var7 != null) {
               Arrays.sort(var7, new Comparator() {
                  public int compare(File var1, File var2) {
                     int var3;
                     if (var1 != null && var2 != null) {
                        if (var1.isDirectory() && !var2.isDirectory()) {
                           var3 = -1;
                        } else if (var2.isDirectory() && !var1.isDirectory()) {
                           var3 = 1;
                        } else {
                           var3 = var1.getName().compareTo(var2.getName());
                        }
                     } else {
                        var3 = 0;
                     }

                     return var3;
                  }
               });

               for(var4 = 0; var4 < var7.length; ++var4) {
                  boolean var5 = true;
                  if (!var7[var4].isDirectory()) {
                     if (this.folderMode || this.filter != null && !var7[var4].getName().matches(this.filter)) {
                        var5 = false;
                     } else {
                        var5 = true;
                     }
                  }

                  if (var5 || !this.showOnlySelectable) {
                     FileItem var6 = new FileItem(this.chooser.getContext(), var7[var4]);
                     var6.setSelectable(var5);
                     var3.add(var6);
                  }
               }
            }

            String var8;
            if (this.showFullPathInTitle) {
               var8 = this.currentFolder.getPath();
            } else {
               var8 = this.currentFolder.getName();
            }

            this.chooser.setCurrentFolderName(var8);
         }

         for(var4 = 0; var4 < var3.size(); ++var4) {
            ((FileItem)var3.get(var4)).addListener(this.fileItemClickListener);
            var2.addView((View)var3.get(var4));
         }

         defaultFolder = this.currentFolder;
      }

   }

   public void loadFolder(String var1) {
      Object var2 = null;
      File var3 = (File)var2;
      if (var1 != null) {
         var3 = (File)var2;
         if (var1.length() > 0) {
            var3 = new File(var1);
         }
      }

      this.loadFolder(var3);
   }

   public void removeAllListeners() {
      this.fileSelectedListeners.clear();
      this.cancelListeners.clear();
   }

   public void removeListener(FileChooserCore.OnCancelListener var1) {
      this.cancelListeners.remove(var1);
   }

   public void removeListener(FileChooserCore.OnFileSelectedListener var1) {
      this.fileSelectedListeners.remove(var1);
   }

   public void setCanCreateFiles(boolean var1) {
      this.canCreateFiles = var1;
      this.updateButtonsLayout();
   }

   public void setFilter(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.filter = var1;
      } else {
         this.filter = null;
      }

      this.loadFolder(this.currentFolder);
   }

   public void setFolderMode(boolean var1) {
      this.folderMode = var1;
      this.updateButtonsLayout();
      this.loadFolder(this.currentFolder);
   }

   public void setLabels(FileChooserLabels var1) {
      this.labels = var1;
      if (var1 != null) {
         LinearLayout var2 = this.chooser.getRootLayout();
         if (var1.labelAddButton != null) {
            ((Button)var2.findViewById(R.id.buttonAdd)).setText(var1.labelAddButton);
         }

         if (var1.labelSelectButton != null) {
            ((Button)var2.findViewById(R.id.buttonOk)).setText(var1.labelSelectButton);
         }

         if (var1.labelCancelButton != null) {
            ((Button)var2.findViewById(R.id.buttonCancel)).setText(var1.labelCancelButton);
         }
      }

   }

   public void setShowCancelButton(boolean var1) {
      this.showCancelButton = var1;
      this.updateButtonsLayout();
   }

   public void setShowConfirmationOnCreate(boolean var1) {
      this.showConfirmationOnCreate = var1;
   }

   public void setShowConfirmationOnSelect(boolean var1) {
      this.showConfirmationOnSelect = var1;
   }

   public void setShowFullPathInTitle(boolean var1) {
      this.showFullPathInTitle = var1;
   }

   public void setShowOnlySelectable(boolean var1) {
      this.showOnlySelectable = var1;
      this.loadFolder(this.currentFolder);
   }

   public interface OnCancelListener {
      void onCancel();
   }

   public interface OnFileSelectedListener {
      void onFileSelected(File var1);

      void onFileSelected(File var1, String var2);
   }
}
