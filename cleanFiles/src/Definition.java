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

public class Definition implements JsonObject {

  public static final PathMatcher DEF_MATCHER = FileSystems.getDefault()
      .getPathMatcher("regex:^(?!.*Colloquial|dynamic).*\\.json$");
  public static final Type DEF_TYPE = new TypeToken<Map<String, Property>>() {
  }.getType();

  private String lang;
  private String fileName;
  private final Map<String, Property> properties;

  public Definition(String lang, String fileName, final Map<String, Property> properties) {
    this.lang = lang;
    this.fileName = fileName;
    this.properties = properties;
  }

  public Definition(Path defPath, final Map<String, Property> properties) {
    this.lang = defPath.getParent().getFileName().toString();
    this.fileName = defPath.getFileName().toString();
    this.properties = properties;
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

  public void replacePropertiesFrom(final Definition def) {
    properties.entrySet().parallelStream()
        .forEach(entry -> {
          String key = entry.getKey();
          if (def.getProperties().keySet().contains(key))
            entry.setValue(def.getProperties().get(key));
          else
            entry.setValue(null);
        });
    removeEmptyFields();
  }

  public Map<String, Property> getBowProperties() {
    final String bow = new String(new byte[] { (byte) 0xEE, (byte) 0x82, (byte) 0x99 }, Charset.forName("UTF-8"));
    return properties.entrySet().stream()
        .filter(entry -> entry.getValue().progressDescription.contains(bow))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
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