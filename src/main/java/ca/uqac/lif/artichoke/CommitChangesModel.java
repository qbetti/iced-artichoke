package ca.uqac.lif.artichoke;

import org.icepdf.core.pobjects.Document;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

public class CommitChangesModel {

    private static final Logger LOGGER = Logger.getLogger(CommitChangesModel.class.getCanonicalName());

    private CommitChangesDialog view;

    private FormData oldFormData;
    private FormData newFormData;


    public CommitChangesModel (CommitChangesDialog view, FormData oldFormData, Document document) {
        this.view = view;
        this.oldFormData = oldFormData;
        this.newFormData = FormData.buildFromDocument(document);
    }


    public void evalChanges() {
        LOGGER.info("Evaluating changes");
        List<FormData.Change> changes = newFormData.getChanges(this.oldFormData);
        view.changesRetrieved(changes);
    }


    public void commit(Vector changes) {
       if(changes == null || changes.size() == 0) {
           LOGGER.info("No changes to commit");
           view.onNoChangesDetected();
           return;
       }

       Iterator iterator = changes.iterator();
       for(int i = 0; iterator.hasNext(); i++) {
           Vector change = (Vector) iterator.next();

           Object key = change.elementAt(0);
           Object oldValue = change.elementAt(1);
           Object newValue = change.elementAt(2);
           Object group = change.elementAt(3);

           if(isEmpty(key)) {
               view.onFieldKeyNotProvided(i+1);
               break;
           }

           if(isEmpty(group)) {
               view.onGroupNotProvided(key.toString());
               break;
           }

           LOGGER.info(key + " " + oldValue + " " + newValue + " " + group);
       }
    }

    public static boolean isEmpty(Object o) {
        return o == null || o.toString().replaceAll("\\s", "").isEmpty();
    }
}
