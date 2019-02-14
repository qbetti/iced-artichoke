package ca.uqac.lif.artichoke;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        ArtichokeSwingController controller = new ArtichokeSwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        JFrame window = factory.buildViewerFrame();

        controller.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                        controller.getDocumentViewController()));

        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.addWindowListener(controller);


        JButton commitChangesButton = new JButton("Commit changes");
        window.getJMenuBar().add(commitChangesButton);
        controller.setCommitChangesButton(commitChangesButton);

        window.pack();
        window.setVisible(true);
    }

}
