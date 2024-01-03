import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.reflect.TypeToken;

public class Definition implements JsonObject {

  public static final PathMatcher DEF_MATCHER = FileSystems.getDefault()
      .getPathMatcher("regex:^(?!.*Colloquial|dynamic).*\\.json$");

  public static final Type DEF_TYPE = new TypeToken<Map<String, Property>>() {
  }.getType();

  private String lang;
  private String fileName;
  private final Map<String, Property> properties;

  public Definition(String lang, String fileName, final Map<String, Property> properties) {
    this.lang = lang.equals("en") ? "template" : lang;
    this.fileName = fileName;
    this.properties = properties;
  }

  public Definition(Path path, final Map<String, Property> properties) {
    this(path.getParent().getFileName().toString(),
        path.getFileName().toString(),
        properties);
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Map<String, Property> getProperties() {
    return properties;
  }

  public Path getPath() {
    return Paths.get(lang, fileName);
  }

  public void replacePropertiesFrom(final Definition def) {
    properties.entrySet().parallelStream()
        .forEach(entry -> {
          String key = entry.getKey();
          if (def.properties.keySet().contains(key))
            entry.setValue(def.properties.get(key));
          else
            entry.setValue(null);
        });
    removeEmptyFields();
  }

  public Map<String, Property> getBowProperties() {
    return properties.entrySet().stream()
        .filter(Definition::bowProperties)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
  }

  public List<String> getMissingKeys(Definition def) {
    List<String> missingKeys = new ArrayList<>();

    if (def != null && this.fileName.equals(def.fileName)) {
      missingKeys = new ArrayList<String>(def.properties.keySet());
      missingKeys.removeAll(this.properties.keySet());
    }

    return missingKeys;
  }

  public Definition findDefFrom(Stream<Definition> defs) {
    return defs
        .filter(d -> d.fileName.equals(this.fileName))
        .findAny()
        .orElse(null);
  }

  public int getPropertySize() {
    int countBow = 0;
    if (fileName.equals("DestinyObjectiveDefinition.json"))
      countBow = (int) properties.entrySet().stream().filter(Definition::bowProperties).count();
    return properties.keySet().size() - countBow;
  }

  public static boolean bowProperties(Map.Entry<String, Property> e) {
    final String bow = new String(
        new byte[] { (byte) 0xEE, (byte) 0x82, (byte) 0x99 },
        Charset.forName("UTF-8"));

    String progressDescription = e.getValue().getProgressDescription();

    return progressDescription != null && progressDescription.contains(bow);
  }

  @Override
  public Definition removeEmptyFields() {
    properties.values().removeIf(Objects::isNull);
    properties.values().forEach(JsonObject::removeEmptyFields);
    properties.values().removeIf(JsonObject::isEmpty);
    properties.values().removeIf(Objects::isNull);
    return this;
  }

  @Override
  public boolean isEmpty() {
    return properties.isEmpty();
  }
}