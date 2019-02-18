package ca.uqac.lif.artichoke;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.acroform.FieldDictionary;
import org.icepdf.core.pobjects.annotations.AbstractWidgetAnnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FormData {

    private final static Logger LOGGER = Logger.getLogger(FormData.class.getCanonicalName());

    protected Map<String, Object> fields;

    protected FormData() {
        fields = new HashMap<>();
    }


    public static FormData buildFromDocument(Document document) {
        if(document == null
                || document.getCatalog().getInteractiveForm() == null
                || document.getCatalog().getInteractiveForm().getFields().size() == 0) {
            return null;
        }

        FormData formData = new FormData();
        ArrayList dFields = document.getCatalog().getInteractiveForm().getFields();

        for(Object dField : dFields) {
            FieldDictionary fieldDict = null;

            if (dField instanceof AbstractWidgetAnnotation) {
                fieldDict = ((AbstractWidgetAnnotation) dField).getFieldDictionary();
            } else if (dField instanceof FieldDictionary) {
                fieldDict = (FieldDictionary) dField;
            }

            if (fieldDict != null
                    && fieldDict.getPartialFieldName() != null
                    && !fieldDict.getPartialFieldName().isEmpty()) {
                formData.fields.put(fieldDict.getPartialFieldName(), fieldDict.getFieldValue());
            }
        }

        return formData;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Form[");

        for (Map.Entry<String, Object> field : fields.entrySet()) {
            sb.append(field.getKey())
                    .append("=")
                    .append(field.getValue().toString())
                    .append(";");
        }
        sb.append("]");

        return sb.toString();
    }


    public List<Change> getChanges(FormData old) {
        List<Change> changes = new ArrayList<>();

        for(Map.Entry<String, Object> newField : this.fields.entrySet()) {
            String fieldKey = newField.getKey();
            Object newFieldValue = newField.getValue();
            Object oldFieldValue = old.fields.get(fieldKey);

            if(!oldFieldValue.equals(newFieldValue)) {
                changes.add(new Change(fieldKey, oldFieldValue, newFieldValue));
            }
        }

        return changes;
    }


    public class Change {

        private String key;
        private Object oldValue;
        private Object newValue;

        public Change(String key, Object oldValue, Object newValue) {
            this.key = key;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public String getKey() {
            return key;
        }

        public Object getNewValue() {
            return newValue;
        }

        public Object getOldValue() {
            return oldValue;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(key);
            sb.append(": ").append(oldValue)
                    .append(" -> ").append(newValue);

            return sb.toString();
        }
    }
}
