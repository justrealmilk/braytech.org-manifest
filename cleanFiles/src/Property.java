public class Property implements JsonObject {

    static class DisplayProperties implements JsonObject {
        private String name;
        private String description;
        private String supplement;

        @Override
        public boolean isEmpty() {
            return (name == null || name.isBlank()) &&
                    (description == null || description.isBlank()) &&
                    (supplement == null || supplement.isBlank());
        }

        @Override
        public DisplayProperties removeEmptyFields() {
            if (name != null && name.isBlank())
                name = null;
            if (description != null && description.isBlank())
                description = null;
            if (supplement != null && supplement.isBlank())
                supplement = null;
            return this;
        }
    }

    static class Entry implements JsonObject {
        private String prefix;
        private String name;
        private String completed;

        @Override
        public boolean isEmpty() {
            return (prefix == null || prefix.isBlank()) &&
                    (name == null || name.isBlank()) &&
                    (completed == null || completed.isBlank());
        }

        @Override
        public Entry removeEmptyFields() {
            if (prefix != null && prefix.isBlank())
                prefix = null;
            if (name != null && name.isBlank())
                name = null;
            if (completed != null && completed.isBlank())
                completed = null;
            return this;
        }
    }

    static class Inventory implements JsonObject {
        private String tierTypeName;

        @Override
        public boolean isEmpty() {
            return (tierTypeName == null || tierTypeName.isBlank());
        }

        @Override
        public Inventory removeEmptyFields() {
            if (tierTypeName != null && tierTypeName.isBlank())
                tierTypeName = null;
            return this;
        }
    }

    static class Extended implements JsonObject {
        private String supplement;

        @Override
        public boolean isEmpty() {
            return (supplement == null || supplement.isBlank());
        }

        @Override
        public Extended removeEmptyFields() {
            if (supplement != null && supplement.isBlank())
                supplement = null;
            return this;
        }
    }

    private String name;
    private String description;
    private DisplayProperties displayProperties;
    private DisplayProperties originalDisplayProperties;
    private Entry entry;
    private String sourceString;
    private Number sourceSeason;
    private String statName;
    private String statNameAlt;
    private String statNameAbbr;
    private String statDescription;
    private String itemTypeDisplayName;
    private String displaySource;
    private String substring;
    private String distinguish;
    private String progressDescription;
    private com.google.gson.JsonArray bubbles;
    private com.google.gson.JsonArray keywords;
    private com.google.gson.JsonArray rotation;
    private Inventory inventory;
    private Extended extended;

    public String getProgressDescription() {
        return progressDescription;
    }

    @Override
    public boolean isEmpty() {
        return (name == null || name.isBlank())
                && (description == null || description.isBlank())
                && (displayProperties == null || displayProperties.isEmpty())
                && (originalDisplayProperties == null || originalDisplayProperties.isEmpty())
                && (entry == null || entry.isEmpty())
                && (sourceString == null || sourceString.isBlank()) && (sourceSeason == null)
                && (statName == null || statName.isBlank())
                && (statNameAlt == null || statNameAlt.isBlank())
                && (statNameAbbr == null || statNameAbbr.isBlank())
                && (statDescription == null || statDescription.isBlank())
                && (itemTypeDisplayName == null || itemTypeDisplayName.isBlank())
                && (displaySource == null || displaySource.isBlank()) && (substring == null || substring.isBlank())
                && (distinguish == null || distinguish.isBlank())
                && (progressDescription == null || progressDescription.isBlank())
                && (bubbles == null || bubbles.isJsonNull())
                && (keywords == null || keywords.isJsonNull())
                && (rotation == null || rotation.isJsonNull())
                && (inventory == null || inventory.isEmpty())
                && (extended == null || extended.isEmpty());
    }

    @Override
    public Property removeEmptyFields() {
        name = removeEmptyString(name);
        description = removeEmptyString(description);
        displayProperties = (DisplayProperties) removeEmptyObj(displayProperties);
        originalDisplayProperties = (DisplayProperties) removeEmptyObj(originalDisplayProperties);
        entry = (Entry) removeEmptyObj(entry);
        sourceString = removeEmptyString(sourceString);
        sourceSeason = removeEmptyNumber(sourceSeason);
        statName = removeEmptyString(statName);
        statNameAlt = removeEmptyString(statNameAlt);
        statNameAbbr = removeEmptyString(statNameAbbr);
        statDescription = removeEmptyString(statDescription);
        itemTypeDisplayName = removeEmptyString(itemTypeDisplayName);
        displaySource = removeEmptyString(displaySource);
        substring = removeEmptyString(substring);
        distinguish = removeEmptyString(distinguish);
        progressDescription = removeEmptyString(progressDescription);
        inventory = (Inventory) removeEmptyObj(inventory);
        extended = (Extended) removeEmptyObj(extended);
        return this;
    }

    private JsonObject removeEmptyObj(JsonObject o) {
        if (o != null) {
            o = o.removeEmptyFields();
            if (o.isEmpty())
                o = null;
        }
        return o;
    }

    private Number removeEmptyNumber(Number s) {
        if (s != null && s.intValue() != 0) // lol
            s = null;
        return s;
    }

    private String removeEmptyString(String s) {
        if (s != null && s.isBlank())
            s = null;
        return s;
    }
}