package ca.uqac.lif.artichoke;

import org.icepdf.core.pobjects.Document;
import org.icepdf.ri.common.SwingController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.Normalizer;

public class ArtichokeSwingController extends SwingController {

    private JButton commitChangesButton;

    private FormData oldFormData;


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        super.actionPerformed(actionEvent);
        Object source = actionEvent.getSource();

        if(source == commitChangesButton) {
            logger.info("Clicked on Commit Change button");
            commitChanges();
        }
    }

    @Override
    public void openFile() {
        super.openFile();
        if(document != null)
            logger.info("Do things one document is ready");



        // TODO: retrieve peer-action sequence in metadata if any
        // TODO: verify peer-action , and compare it to the lifecycle, and construct form structure data from PAS
        // TODO: construct form data structure from document

        oldFormData = FormData.buildFromDocument(document);
        System.out.println("Old: " + oldFormData);
    }

    @Override
    public void saveFile() {
        super.saveFile();
        if(document != null)
            logger.info("The info is saved");

        // TODO: construct new form data structure from document
        // TODO: compare old and new form data structure
        // TODO: build my PA and add it to the sequence
    }

    public void setCommitChangesButton(JButton btn) {
        commitChangesButton = btn;
        commitChangesButton.addActionListener(this);
    }

    public void commitChanges() {
        if(oldFormData == null)
            return;

        CommitChangesController commitChangesController = new CommitChangesController();
        CommitChangesDialog dialog = new CommitChangesDialog(this.getViewerFrame(), commitChangesController);
        CommitChangesModel commitChangesModel = new CommitChangesModel(dialog, oldFormData, document);
        commitChangesController.setModel(commitChangesModel);

        dialog.pack();
        dialog.setVisible(true);

        // Must be done after all changes
//        oldFormData = newFormData;
    }
}
