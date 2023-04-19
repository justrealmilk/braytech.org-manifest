import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;

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

    public static final PathMatcher DEF_MATCHER = FileSystems.getDefault()
            .getPathMatcher("regex:^(?!.*Colloquial|dynamic).*\\.json$");
    public static final Type DEF_TYPE = new TypeToken<Map<String, Property>>() {
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

    public String getFilename() {
        return toFile.getFileName().toString();
    }

    public void replacePropertiesFrom(final Definition def) {
        properties.entrySet().parallelStream()
                .forEach(entry -> {
                    if (def.getProperties().keySet().contains(entry.getKey()))
                        entry.setValue(def.getProperties().get(entry.getKey()));
                    else
                        entry.setValue(null);
                });
        removeEmptyFields();
    }

    public Map<String, Property> getBowProperties() {
        final String bow = new String(new byte[] { (byte) 0xEE, (byte) 0x82, (byte) 0x99 }, Charset.forName("UTF-8"));
        return properties.entrySet().stream().filter(entry -> entry.getValue().progressDescription.contains(bow))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    public Definition removeEmptyFields() {
        properties.values().removeIf(Objects::isNull);
        properties.values().forEach(JsonObject::removeEmptyFields);
        properties.values().removeIf(JsonObject::isEmpty);
        properties.values().removeIf(Objects::isNull);
        return this;
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }
}