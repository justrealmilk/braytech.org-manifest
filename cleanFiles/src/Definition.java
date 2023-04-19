import java.nio.file.Path;
import java.util.Map;

public class Definition {
    interface JsonObject {
        public boolean isEmpty();

        public JsonObject removeEmptyFields();
    }

    static class Property implements JsonObject {
        private DisplayProperties displayProperties;
        private DisplayProperties originalDisplayProperties;
        private Entry entry;
        private String sourceString;
        private String statName;
        private String statNameAlt;
        private String statNameAbbr;
        private String statDescription;
        private String itemTypeDisplayName;
        private String displaySource;
        private String substring;
        private String progressDescription;
        private com.google.gson.JsonArray bubbles;
        private com.google.gson.JsonArray keywords;
        private Inventory inventory;
        private Extended extended;

        @Override
        public boolean isEmpty() {
            return (displayProperties == null || displayProperties.isEmpty())
                    && (originalDisplayProperties == null || originalDisplayProperties.isEmpty())
                    && (entry == null || entry.isEmpty())
                    && (sourceString == null || sourceString.isBlank()) && (statName == null || statName.isBlank())
                    && (statNameAlt == null || statNameAlt.isBlank())
                    && (statNameAbbr == null || statNameAbbr.isBlank())
                    && (statDescription == null || statDescription.isBlank())
                    && (itemTypeDisplayName == null || itemTypeDisplayName.isBlank())
                    && (displaySource == null || displaySource.isBlank()) && (substring == null || substring.isBlank())
                    && (progressDescription == null || progressDescription.isBlank())
                    && (bubbles == null || bubbles.isJsonNull())
                    && (keywords == null || keywords.isJsonNull())
                    && (inventory == null || inventory.isEmpty())
                    && (extended == null || extended.isEmpty());
        }

        @Override
        public Property removeEmptyFields() {
            displayProperties = (DisplayProperties) removeEmptyObj(displayProperties);
            originalDisplayProperties = (DisplayProperties) removeEmptyObj(originalDisplayProperties);
            entry = (Entry) removeEmptyObj(entry);
            sourceString = removeEmptyString(sourceString);
            statName = removeEmptyString(statName);
            statNameAlt = removeEmptyString(statNameAlt);
            statNameAbbr = removeEmptyString(statNameAbbr);
            statDescription = removeEmptyString(statDescription);
            itemTypeDisplayName = removeEmptyString(itemTypeDisplayName);
            displaySource = removeEmptyString(displaySource);
            substring = removeEmptyString(substring);
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

        private String removeEmptyString(String s) {
            if (s != null && s.isBlank())
                s = null;
            return s;
        }
    }

    static class DisplayProperties implements JsonObject {
        private String name;
        private String description;
        private String tip;

        @Override
        public boolean isEmpty() {
            return (name == null || name.isBlank()) &&
                    (description == null || description.isBlank()) &&
                    (tip == null || tip.isBlank());
        }

        @Override
        public DisplayProperties removeEmptyFields() {
            if (name != null && name.isBlank())
                name = null;
            if (description != null && description.isBlank())
                description = null;
            if (tip != null && tip.isBlank())
                tip = null;
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
        private String tip;

        @Override
        public boolean isEmpty() {
            return (tip == null || tip.isBlank());
        }

        @Override
        public Extended removeEmptyFields() {
            if (tip != null && tip.isBlank())
                tip = null;
            return this;
        }
    }

    public static final java.nio.file.PathMatcher DEF_MATCHER = java.nio.file.FileSystems.getDefault()
            .getPathMatcher("regex:^(?!.*Colloquial|dynamic).*\\.json$");
    public static final java.lang.reflect.Type DEF_TYPE = new com.google.gson.reflect.TypeToken<Map<String, Property>>() {
    }.getType();

    private Path toFile;
    private final Map<String, Property> properties;

    public Definition(final Path toFile, final Map<String, Property> properties) {
        this.toFile = toFile;
        this.properties = properties;
    }

    public Path getToFile() {
        return toFile;
    }

    public Definition setToFile(final Path toFile) {
        this.toFile = toFile;
        return this;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public Definition removeEmptyFields() {
        properties.values().forEach(JsonObject::removeEmptyFields);
        return this;
    }

    public boolean isEmpty() {
        properties.values().removeIf(JsonObject::isEmpty);
        return properties.isEmpty();
    }
}