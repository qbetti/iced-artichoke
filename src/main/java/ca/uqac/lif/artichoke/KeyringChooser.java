package ca.uqac.lif.artichoke;

import ca.uqac.lif.artichoke.keyring.KeyRing;
import ca.uqac.lif.artichoke.keyring.exceptions.BadPassphraseException;
import ca.uqac.lif.artichoke.keyring.exceptions.PrivateKeyDecryptionException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Displays a dialog to choose a keyring file and load it with password or not.
 * The method {@link #showDialog(JFrame)} opens the dialog. Then, the {@link KeyRing} object can be
 * retrieved with {@link #getChosenKeyRing()} if the returned value from {@link #showDialog(JFrame)}
 * is equaled to {@link #SUCCEED_OPTION} only.
 * Handles file reading, parsing and bad passphrase problems by showing corresponding error/warning messages
 */
public class KeyringChooser extends JComponent implements ActionListener{

    /**
     * Logger of the class
     */
    private final static Logger logger = Logger.getLogger(KeyringChooser.class.getCanonicalName());

    /**
     * Returned value when keyring file was loaded successfully
     */
    public final static int SUCCEED_OPTION = 0;

    /**
     * Returned value when action is canceled
     */
    public final static int CANCEL_OPTION = -1;

    /**
     * Returned value when the specified file is not found or is not readable
     */
    public final static int FILE_UNAVAILABLE_OPTION = -2;

    /**
     * Returned value when the file does not have a correct JSON format
     */
    public final static int INCORRECT_FILE_OPTION = -3;

    /**
     * Returned value when an decryption error occurred
     */
    public final static int INTERNAL_ERROR_OPTION = -4;

    /**
     * Returned value when an incorrect password is used for the keyring file
     */
    public final static int INCORRECT_PASSWORD_OPTION = -5;

    private JDialog dialog;

    private JPanel panelButtons;
    private JPanel panelChooseFile;
    private JPanel panelRoot;

    private JLabel lblExplanation;
    private JLabel lblFilePath;
    private JLabel lblPassword;
    private JTextField txtKeyringFilePath;
    private JTextField txtKeyringPassword;
    private JButton btnChooseKeyringFile;
    private JButton btnLoad;
    private JButton btnCancel;
    private JCheckBox cbStayUnlock;

    private int returnVal;
    private File keyringFile;
    private KeyRing keyRing;

    /**
     * Constructs the components and all its subcomponents
     */
    public KeyringChooser() {
        super();

        lblExplanation = new JLabel("<html>Before committing any changes, you must first indicate your keyring file, which holds needed information like your keys and groups you belong to.</html>");
        lblExplanation.setPreferredSize(new Dimension(200, 50));
        lblFilePath = new JLabel("Keyring file path:");
        lblPassword = new JLabel("Keyring password:");

        txtKeyringFilePath = new JTextField(15);
        btnChooseKeyringFile = new JButton("Choose file...");
        btnLoad = new JButton("Load keyring");
        btnCancel = new JButton("Cancel");
        cbStayUnlock = new JCheckBox("Unlock for this session");
        txtKeyringPassword = new JPasswordField();

        // by default we hide and disable components about the keyring password
        enablePasswordComponents(false);

        panelChooseFile = new JPanel();
        GridLayout layoutChooseFile = new GridLayout(3,3, 5, 10);
        panelChooseFile.setLayout(layoutChooseFile);
        panelChooseFile.add(lblFilePath);
        panelChooseFile.add(txtKeyringFilePath);
        panelChooseFile.add(btnChooseKeyringFile);
        panelChooseFile.add(cbStayUnlock);
        panelChooseFile.add(new JLabel(""));
        panelChooseFile.add(new JLabel(""));
        panelChooseFile.add(new JLabel(""));
        panelChooseFile.add(lblPassword);
        panelChooseFile.add(txtKeyringPassword);

        LayoutManager layoutButtons = new GridLayout(1,2, 5, 10);
        panelButtons = new JPanel();
        panelButtons.setLayout(layoutButtons);
        panelButtons.add(btnLoad);
        panelButtons.add(btnCancel);

        BorderLayout layoutRoot = new BorderLayout(5, 10);
        panelRoot = new JPanel();
        panelRoot.setLayout(layoutRoot);
        panelRoot.add(lblExplanation, BorderLayout.PAGE_START);
        panelRoot.add(panelChooseFile, BorderLayout.CENTER);
        panelRoot.add(panelButtons, BorderLayout.PAGE_END);

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.add(panelRoot);

        btnChooseKeyringFile.addActionListener(this);
        btnLoad.addActionListener(this);
        btnCancel.addActionListener(this);
        cbStayUnlock.addActionListener(this);
    }

    /**
     * Shows a dialog to choose the keyring file and to load it
     * @param parent the parent frame for the dialog
     * @return {@link #SUCCEED_OPTION} if the keyring was successfully loaded,
     *          see other option values for errors
     */
    public int showDialog(JFrame parent) {
        if(this.dialog != null) {
            return CANCEL_OPTION;
        }

        dialog = new JDialog(parent, true);
        dialog.setTitle("Open your keyring file");

        Container container = dialog.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(this, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        dialog.setVisible(true);

        return returnVal;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if(source == btnCancel) {
            cancel();
        } else if(source == btnLoad) {
            fireLoadKeyringFile();
        } else if(source == btnChooseKeyringFile) {
            chooseKeyringFile();
        } else if(source == cbStayUnlock) {
            if(cbStayUnlock.isSelected()) {
                enablePasswordComponents(true);
            } else {
                enablePasswordComponents(false);
            }
        }
    }

    /**
     * Enables/disbales and hides/reveals the components about the password unlocking of the keyring file
     * @param b true if the components should be visible and enabled, false otherwise
     */
    private void enablePasswordComponents(boolean b) {
        txtKeyringPassword.setText("");
        lblPassword.setVisible(b);
        txtKeyringPassword.setVisible(b);
        txtKeyringPassword.setEnabled(b);
    }

    /**
     * Handles the cancel button. Closes the dialog.
     */
    private void cancel() {
        returnVal = CANCEL_OPTION;
        if(dialog != null) {
            dialog.setVisible(false);
        }
    }

    /**
     * Handles the load button. Closes the dialog if loading is successful only.
     */
    private void fireLoadKeyringFile() {
        File fromTxtFilePath = new File(txtKeyringFilePath.getText());

        if(keyringFile == null && (!fromTxtFilePath.exists() || !fromTxtFilePath.isFile())) {
            returnVal = CANCEL_OPTION;
            JOptionPane.showMessageDialog(dialog, "You must specify a keyring file", "No file specified", JOptionPane.WARNING_MESSAGE);
            return;
        } else {
            keyringFile = fromTxtFilePath;
        }

        btnLoad.setText("Loading...");
        enableRelevantComponents(false);

        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                loadKeyringFile();
                return null;
            }

            @Override
            protected void done() {
                btnLoad.setText("Load keyring file");
                enableRelevantComponents(true);

                if(keyRing != null)
                    dialog.setVisible(false);
            }
        };
        worker.execute();
    }

    /**
     * Loads the keyring file and handles loading exceptions
     */
    private void loadKeyringFile() {
        try {
            if(!cbStayUnlock.isSelected())
                keyRing = KeyRing.loadFromFile(keyringFile);
            else
                keyRing = KeyRing.loadFromFile(keyringFile, txtKeyringPassword.getText());
            returnVal = SUCCEED_OPTION;
        }
        catch (IOException | JsonIOException e) {
            logger.severe(e.getMessage());
            returnVal = FILE_UNAVAILABLE_OPTION;
            JOptionPane.showMessageDialog(dialog, "The file could not be found/read.", "File unavailable", JOptionPane.ERROR_MESSAGE);
        }
        catch (JsonParseException | IllegalStateException e) {
            logger.severe(e.getMessage());
            returnVal = INCORRECT_FILE_OPTION;
            JOptionPane.showMessageDialog(dialog, "The file is not a JSON file or is malformed.", "Incorrect format", JOptionPane.ERROR_MESSAGE);
        }
        catch (PrivateKeyDecryptionException e) {
            logger.severe(e.getMessage());
            returnVal = INTERNAL_ERROR_OPTION;
            JOptionPane.showMessageDialog(dialog, "An internal error has occurred during decryption.", "Internal error", JOptionPane.ERROR_MESSAGE);
        }
        catch (BadPassphraseException e) {
            logger.severe(e.getMessage());
            returnVal = INCORRECT_PASSWORD_OPTION;
            JOptionPane.showMessageDialog(dialog, "Incorrect password for this keyring file.", "Password incorrect", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the choose keyring file button.
     */
    private void chooseKeyringFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(dialog);

        if(result == JFileChooser.APPROVE_OPTION) {
            keyringFile = fileChooser.getSelectedFile();
            txtKeyringFilePath.setText(keyringFile.getAbsolutePath());
        } else {
            keyringFile = null;
            txtKeyringFilePath.setText("");
        }
    }

    /**
     * Returns the loading keyring object corresponding to the chosen file
     * @return the loading keyring object corresponding to the chosen file
     */
    public KeyRing getChosenKeyRing() {
        return keyRing;
    }

    /**
     * Enables/disables the components of the dialog which can be modified and the dialog's close button
     * @param b true if the components should be enabled, false otherwise
     */
    private void enableRelevantComponents(boolean b) {
        txtKeyringFilePath.setEnabled(b);
        btnChooseKeyringFile.setEnabled(b);

        cbStayUnlock.setEnabled(b);
        txtKeyringPassword.setEnabled(b && cbStayUnlock.isSelected());

        btnLoad.setEnabled(b);
        btnCancel.setEnabled(b);

        dialog.setDefaultCloseOperation(b ? WindowConstants.DISPOSE_ON_CLOSE : WindowConstants.DO_NOTHING_ON_CLOSE);
    }
}
