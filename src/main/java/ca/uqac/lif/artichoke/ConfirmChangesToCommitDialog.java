package ca.uqac.lif.artichoke;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

public class ConfirmChangesToCommitDialog extends JDialog {


    private final static String[] groups = new String[]{"group0", "group1", "group2"};
    private final static Object[] columnNames = new Object[]{"Key", "Old value", "New value", "Group"};

    private static final Logger LOGGER = Logger.getLogger(ConfirmChangesToCommitDialog.class.getCanonicalName());

    private CommitChangesController controller;

    private JPanel dialogPanel;
    private JPanel btnPanel;
    private JScrollPane tableScrollPane;
    private JTable changesTable;
    private JComboBox<String> groupComboBox;
    private JButton commitBtn;
    private JButton cancelBtn;
    private JLabel chooseGroupsLbl;


    public ConfirmChangesToCommitDialog(Frame frame, CommitChangesController controller) {
        super(frame, "Confirm changes to commit", true);
        this.controller = controller;
        controller.setView(this);

        addWindowListener(controller);

        buildDialogPanel();
    }


    protected void buildDialogPanel() {
        dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        chooseGroupsLbl = new JLabel(
                "Choose the group you wish to perform the changement on behalf of:",
                SwingConstants.LEFT
        );
        dialogPanel.add(chooseGroupsLbl);


        // ComboxBox containg potential groups
        groupComboBox = new JComboBox<>(groups);

        // tables containing new changes, non-editable except for last column
        changesTable = new JTable(new DefaultTableModel(columnNames, 0)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        changesTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(groupComboBox));

        // Making table scrollable
        tableScrollPane = new JScrollPane(changesTable);
        changesTable.setFillsViewportHeight(true);
        this.add(tableScrollPane);

        commitBtn = new JButton("Commit");
        cancelBtn = new JButton("Cancel");

        btnPanel = new JPanel();
        btnPanel.add(commitBtn);
        btnPanel.add(cancelBtn);

        dialogPanel.add(tableScrollPane);
        dialogPanel.add(btnPanel);

        controller.setCommitButton(commitBtn);
        controller.setCancelButton(cancelBtn);
        controller.setChangesTable(changesTable);

        this.setContentPane(dialogPanel);
    }


    public void changesRetrieved(List<FormData.Change> changes) {
        if(changes == null || changes.size() == 0) {
            onNoModificationDialog();
            return;
        }

        for(FormData.Change change : changes) {
            LOGGER.info(change.toString());
            DefaultTableModel dtm = (DefaultTableModel) changesTable.getModel();

            dtm.addRow(new Object[]{
                    change.getKey(),
                    change.getOldValue(),
                    change.getNewValue()
            });
        }
    }

    public void onNoModificationDialog() {
        JOptionPane.showMessageDialog(this, "There is no modification to commit");
        changesTable.setEnabled(false);
        commitBtn.setEnabled(false);
        close();
    }

    public void close() {
        this.dispose();
    }
}
