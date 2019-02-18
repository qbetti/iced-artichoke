package ca.uqac.lif.artichoke;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Logger;

public class ConfirmChangesToCommitDialog extends JDialog implements ActionListener {


    private final static String[] groups = new String[]{"group0", "group1", "group2"};
    private final static Object[] columnNames = new Object[]{"Key", "Old value", "New value", "Group"};

    private static final Logger LOGGER = Logger.getLogger(ConfirmChangesToCommitDialog.class.getCanonicalName());
    private CommitChangesModel model;
    private JPanel dialogPanel;
    private JTable changesTable;


    public ConfirmChangesToCommitDialog(Frame frame) {
        super(frame, "Confirm changes to commit", true);
        this.setContentPane(buildDialogPanel());
    }


    protected JPanel buildDialogPanel() {
        dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        // ComboxBox containg potential groups
        JComboBox<String> comboBox = new JComboBox<>(groups);

        // tables containing new changes
        changesTable = new JTable(new DefaultTableModel(columnNames, 0)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        changesTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox));

        // Making table scrollable
        JScrollPane scrollPane = new JScrollPane(changesTable);
        changesTable.setFillsViewportHeight(true);
        this.add(scrollPane);

        JPanel btnPanel = new JPanel();
        JButton commitBtn = new JButton("Commit");
        JButton cancelBtn = new JButton("Cancel");

        btnPanel.add(commitBtn);
        btnPanel.add(cancelBtn);

        dialogPanel.add(scrollPane);
        dialogPanel.add(btnPanel);



        return dialogPanel;
    }



    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    public void changesRetrieved(List<FormData.Change> changes) {
        for(FormData.Change change : changes) {
            LOGGER.info(change.toString());

            ((DefaultTableModel) changesTable.getModel()).addRow(new Object[]{
                    change.getKey(),
                    change.getOldValue(),
                    change.getNewValue()
            });
        }
    }

    public void setCommitChangesModel(CommitChangesModel model) {
        this.model = model;
    }
}
