package ca.uqac.lif.artichoke;

import org.icepdf.ri.common.SwingViewBuilder;

import javax.swing.*;

public class ArtichokeViewBuilder extends SwingViewBuilder {


    public ArtichokeViewBuilder(ArtichokeSwingController swingController) {
        super(swingController);
    }

    @Override
    public JFrame buildViewerFrame() {
        JFrame frame = super.buildViewerFrame();

        ArtichokeSwingController viewerController = (ArtichokeSwingController) this.viewerController;
        JButton commitChangesButton = new JButton("Commit changes");
        viewerController.setCommitChangesButton(commitChangesButton);

        frame.getJMenuBar().add(commitChangesButton);
        return frame;
    }
}
