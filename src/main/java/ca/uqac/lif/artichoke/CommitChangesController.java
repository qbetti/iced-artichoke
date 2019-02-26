package ca.uqac.lif.artichoke;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Logger;

public class CommitChangesController implements ActionListener, WindowListener {

    private static final Logger LOGGER = Logger.getLogger(CommitChangesController.class.getCanonicalName());

    private CommitChangesModel model;
    private CommitChangesDialog view;

    private JTable changesTable;
    private JButton commitButton;
    private JButton cancelButton;

    public CommitChangesController() {

    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {
        LOGGER.info("Window is opened");
        model.evalChanges();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source == commitButton) {
            commit();
        } else if (source == cancelButton) {
            cancel();
        }
    }

    protected void commit() {
        LOGGER.info("Commit changes");
    }

    protected void cancel() {
        LOGGER.info("Canceled commit");
        view.close();
    }

    public void setModel(CommitChangesModel model) {
        this.model = model;
    }

    public void setView(CommitChangesDialog view) {
        this.view = view;
    }

    public void setChangesTable(JTable changesTable) {
        this.changesTable = changesTable;
    }

    public void setCommitButton(JButton commitButton) {
        this.commitButton = commitButton;
        commitButton.addActionListener(this);
    }

    public void setCancelButton(JButton cancelButton) {
        this.cancelButton = cancelButton;
        cancelButton.addActionListener(this);
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) { }

    @Override
    public void windowClosed(WindowEvent windowEvent) { }

    @Override
    public void windowIconified(WindowEvent windowEvent) { }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) { }

    @Override
    public void windowActivated(WindowEvent windowEvent) { }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) { }
}
