package ca.uqac.lif.artichoke;

import org.icepdf.core.pobjects.Document;

public class CommitChangesModel {

    private ConfirmChangesToCommitDialog view;

    private FormData oldFormData;
    private FormData newFormData;


    public CommitChangesModel (ConfirmChangesToCommitDialog view, FormData oldFormData, Document document) {
        this.view = view;
        view.setCommitChangesModel(this);

        this.oldFormData = oldFormData;

        newFormData = FormData.buildFromDocument(document);
        view.changesRetrieved(newFormData.getChanges(this.oldFormData));
    }
}
