package ca.uqac.lif.artichoke;

import ca.uqac.lif.artichoke.keyring.KeyRing;
import org.icepdf.ri.common.SwingController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ArtichokeSwingController extends SwingController {

    private JButton commitChangesButton;

    private FormData oldFormData;

    private KeyRing keyRing;

    public ArtichokeSwingController() {
        super();
        keyRing = null;
    }


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
        if(oldFormData == null) {
            JOptionPane.showMessageDialog(getViewerFrame(), "No form was found in this document", "No form", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // retrieving keyring
        if(keyRing == null) {
            keyRing = browseForKeyRing();
            if(keyRing == null) {
                JOptionPane.showMessageDialog(getViewerFrame(), "Cannot commit changes without specifying a keyring file first", "No keyring file", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        CommitChangesController commitChangesController = new CommitChangesController();
        CommitChangesDialog dialog = new CommitChangesDialog(this.getViewerFrame(), commitChangesController, keyRing.getGroupIds());
        CommitChangesModel commitChangesModel = new CommitChangesModel(dialog, oldFormData, document);
        commitChangesController.setModel(commitChangesModel);

        dialog.pack();
        dialog.setVisible(true);
    }

    private KeyRing browseForKeyRing() {
        KeyringChooser kc = new KeyringChooser();
        int result = kc.showDialog(getViewerFrame());

        if (result == KeyringChooser.SUCCEED_OPTION) {
            logger.info(kc.getChosenKeyRing().toJson().toString());
            return kc.getChosenKeyRing();
        }
        return null;
    }
}
