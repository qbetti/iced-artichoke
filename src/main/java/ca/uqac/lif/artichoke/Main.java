package ca.uqac.lif.artichoke;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        ArtichokeSwingController controller = new ArtichokeSwingController();
        SwingViewBuilder factory = new ArtichokeViewBuilder(controller);
        JFrame window = factory.buildViewerFrame();

        controller.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                        controller.getDocumentViewController()));

        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.addWindowListener(controller);

        window.pack();
        window.setVisible(true);
    }

}
