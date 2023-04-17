import java.nio.file.Path;
import java.util.Map;

public class Definition {
    static class Property {
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

        private void removeEmptyFields() {
            if (displayProperties != null) {
                displayProperties.removeEmptyFields();
                if (displayProperties.isEmpty())
                    displayProperties = null;
            }
            if (originalDisplayProperties != null) {
                originalDisplayProperties.removeEmptyFields();
                if (originalDisplayProperties.isEmpty())
                    originalDisplayProperties = null;
            }
            if (entry != null) {
                entry.removeEmptyFields();
                if (entry.isEmpty())
                    entry = null;
            }
            if (inventory != null) {
                inventory.removeEmptyFields();
                if (inventory.isEmpty())
                    inventory = null;
            }
            if (extended != null) {
                extended.removeEmptyFields();
                if (extended.isEmpty())
                    extended = null;
            }
        }
    }

    static class DisplayProperties {
        private String name;
        private String description;
        private String tip;

        public boolean isEmpty() {
            return (name == null || name.isBlank()) &&
                    (description == null || description.isBlank()) &&
                    (tip == null || tip.isBlank());
        }

        public void removeEmptyFields() {
            if (name != null && name.isBlank())
                name = null;
            if (description != null && description.isBlank())
                description = null;
            if (tip != null && tip.isBlank())
                tip = null;
        }
    }

    static class Entry {
        private String prefix;
        private String name;
        private String completed;

        public boolean isEmpty() {
            return (prefix == null || prefix.isBlank()) &&
                    (name == null || name.isBlank()) &&
                    (completed == null || completed.isBlank());
        }

        public void removeEmptyFields() {
            if (prefix != null && prefix.isBlank())
                prefix = null;
            if (name != null && name.isBlank())
                name = null;
            if (completed != null && completed.isBlank())
                completed = null;
        }
    }

    static class Inventory {
        private String tierTypeName;

        public boolean isEmpty() {
            return (tierTypeName == null || tierTypeName.isBlank());
        }

        public void removeEmptyFields() {
            if (tierTypeName != null && tierTypeName.isBlank())
                tierTypeName = null;
        }
    }

    static class Extended {
        private String tip;

        public boolean isEmpty() {
            return (tip == null || tip.isBlank());
        }

        public void removeEmptyFields() {
            if (tip != null && tip.isBlank())
                tip = null;
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

    public void removeEmptyFields() {
        properties.values().parallelStream().forEach(Property::removeEmptyFields);
    }

    public boolean isEmpty() {
        properties.values().removeIf(Property::isEmpty);
        return properties.isEmpty();
    }
}