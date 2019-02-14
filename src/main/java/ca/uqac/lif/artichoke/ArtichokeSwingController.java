package ca.uqac.lif.artichoke;

import org.icepdf.ri.common.SwingController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ArtichokeSwingController extends SwingController {

    private JButton commitChangesButton;


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        super.actionPerformed(actionEvent);

        Object source = actionEvent.getSource();

        if(source == commitChangesButton) {
            logger.info("Hey j'ai cliqué");
        }
    }

    @Override
    public void openFile() {
        super.openFile();
        if(document != null)
            logger.info("Do things one document is ready");

        // TODO: retrieve peer-action sequence in metadata if any
        // TODO: verify peer-action sequence and construct form structure data from PAS
        // TODO: construct form data structure from document
    }

    @Override
    public void saveFile() {
        super.saveFile();
        if(document != null)
            logger.info("The info is saved");

        // TODO: construct new form data structure from document
        // TODO: compare old and new form data structure
    }

    public void setCommitChangesButton(JButton btn) {
        commitChangesButton = btn;
        commitChangesButton.addActionListener(this);
    }
}
