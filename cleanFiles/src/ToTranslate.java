import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ToTranslate {
  private Map<String, Map<String, List<String>>> keysToTranslate;
  private Map<String, Integer> keysTotals;

  public ToTranslate(String[] langs) {
    this.keysToTranslate = new LinkedHashMap<>(langs.length);

    Arrays.asList(langs).forEach(lang -> keysToTranslate.put(lang, new LinkedHashMap<>()));

    this.keysTotals = new LinkedHashMap<>(langs.length);

    List<Definition> defs = FileUtils.getDefs(langs).toList();
    List<Definition> templateDefs = FileUtils.getDefs("template").toList();

    defs.forEach(def -> {
      Definition templateDef = def.findDefFrom(templateDefs.stream());
      List<String> missingKeys = def.getMissingKeys(templateDef);

      keysToTranslate.get(def.getLang()).put(def.getFileName(), missingKeys);
      keysTotals.compute(def.getLang(), (k, v) -> v == null ? 0 : v + def.getPropertySize());
    });

    int templateTotal = templateDefs.stream().mapToInt(Definition::getPropertySize).sum();

    Map<String, Double> percentages = keysTotals.entrySet().stream().collect(Collectors.toMap(
        Map.Entry::getKey, e -> (double) e.getValue() / templateTotal * 100, (a, b) -> a, LinkedHashMap::new));

    System.out.println("\nTotal number of translated keys: " + keysTotals);
    System.out.println("\nTotal number of keys from template: " + templateTotal);
    System.out.println("\nCoverage: " + percentages);
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