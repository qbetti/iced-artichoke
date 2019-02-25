package ca.uqac.lif.artichoke;

import org.icepdf.core.pobjects.Document;

import java.util.List;
import java.util.logging.Logger;

public class CommitChangesModel {

    private static final Logger LOGGER = Logger.getLogger(CommitChangesModel.class.getCanonicalName());

    private ConfirmChangesToCommitDialog view;

    private FormData oldFormData;
    private FormData newFormData;

    private List<FormData.Change> changes;

    public CommitChangesModel (ConfirmChangesToCommitDialog view, FormData oldFormData, Document document) {
        this.view = view;
        this.oldFormData = oldFormData;
        this.newFormData = FormData.buildFromDocument(document);
    }


    public void evalChanges() {
        LOGGER.info("Evaluating changes");
        this.changes = newFormData.getChanges(this.oldFormData);
        view.changesRetrieved(changes);
    }


    public void commit() {
       if(changes == null || changes.size() == 0) {
           LOGGER.info("No changes to commit");
           view.onNoModificationDialog();
       }
    }
}
