package ca.uqac.lif.artichoke;

import ca.uqac.lif.artichoke.keyring.KeyRing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class KeyringChooser extends JComponent {

    private JDialog dialog;

    private JLabel lblExplanation;
    private JButton btnChooseKeyringFile;

    public KeyringChooser() {
        super();

        lblExplanation = new JLabel("<html>Before committing any changes, you must first indicate your keyring file, which holds needed information like your keys and groups you belong to.</html>");
        btnChooseKeyringFile = new JButton("Choose file...");

        this.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.setBorder(new EmptyBorder(10,10,10,10));

        panel.add(lblExplanation);
        panel.add(btnChooseKeyringFile);

        this.add(panel);



    }

    public KeyRing showDialog(JFrame parent) {
        if(this.dialog != null) {
            return null;
        }


        dialog = new JDialog(parent, true);
        dialog.setTitle("Open your keyring file");

        Container container = dialog.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(this, BorderLayout.CENTER);

        dialog.setPreferredSize(new Dimension(500, 300));
        dialog.setResizable(false);

        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        dialog.setVisible(true);

        return null;
    }
}
