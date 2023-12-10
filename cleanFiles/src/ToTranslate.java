import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ToTranslate {
  private Map<String, Map<String, List<String>>> keysToTranslate;

  public ToTranslate(String[] langs) {
    this.keysToTranslate = new LinkedHashMap<>();
    for (String lang : langs)
      keysToTranslate.put(lang, new LinkedHashMap<>());

    FileUtils.getDefs(langs).forEach(def -> {
      Definition templateDef = FileUtils.findDefFromTemplate(def);
      List<String> missingKeys = new ArrayList<>();
      if (templateDef != null)
        missingKeys = def.getMissingKeys(templateDef);
      keysToTranslate.get(def.getLang()).put(def.getFileName(), missingKeys);
    });
  }

  public Map<String, Map<String, List<String>>> getKeysToTranslate() {
    this.removeEmpty();
    return keysToTranslate;
  }

  private void removeEmpty() {
    keysToTranslate.forEach((k, v) -> v.entrySet().removeIf(entry -> entry.getValue().isEmpty()));
    keysToTranslate.entrySet().removeIf(entry -> entry.getValue().isEmpty());
  }
}
