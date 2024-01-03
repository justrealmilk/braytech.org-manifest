import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileUtils {

  public final static Path TO_REPO = Paths.get(System.getProperty("user.dir"));

  private final static Gson GSON = new GsonBuilder()
      .disableHtmlEscaping()
      .setPrettyPrinting()
      .create();

  public static Stream<Definition> getDefs(final String lang) {
    try {
      return Files.list(TO_REPO.resolve(lang).normalize()).parallel()
          .filter(Definition.DEF_MATCHER::matches)
          .map(FileUtils::loadDef)
          .map(Definition::removeEmptyFields)
          .filter(Objects::nonNull);
    } catch (final IOException e) {
      System.err.println("Error: couldn't list files");
      return null;
    }
  }

  public static Stream<Definition> getDefs(final String[] langs) {
    return Arrays.asList(langs).stream()
        .flatMap(FileUtils::getDefs)
        .filter(Objects::nonNull);
  }

  public static Definition loadDef(Path fullPath) {
    Definition def = null;
    Path defPath = shortenPath(fullPath);
    System.out.println("Loading: " + fullPath);
    try (BufferedReader reader = Files.newBufferedReader(fullPath)) {
      def = new Definition(defPath, GSON.fromJson(reader, Definition.DEF_TYPE));
    } catch (IOException e) {
      System.err.println("Error: couldn't read file: " + fullPath);
    }
    return def;
  }

  public static void saveDef(final Definition def) {
    Path toFile = def.getPath();
    Path fullPath = TO_REPO.resolve(toFile).normalize();
    if (!def.isEmpty()) {
      saveJson(fullPath, def.getProperties());
    } else {
      System.out.println("Empty node: " + toFile);
    }
  }

  public static void saveSortedDef(final Definition def) {
    // loading template to get property order
    final Definition sortedDef = def.findDefFrom(getDefs("template"));
    if (sortedDef == null)
      return;
    // change path
    sortedDef.setLang(def.getLang());
    // set untraslated properties to null
    sortedDef.replacePropertiesFrom(def);
    // add back "bow" properties (not present in template)
    if (def.getFileName().endsWith("DestinyObjectiveDefinition.json"))
      sortedDef.getProperties().putAll(def.getBowProperties());

    saveDef(sortedDef);
  }

  public static void saveMissingKeys(ToTranslate toTranslate) {
    Path toFile = TO_REPO.resolve("toTranslate.json").normalize();
    saveJson(toFile, toTranslate.getKeysToTranslate());
  }

  public static void saveJson(Path toFile, Object o) {
    try (BufferedWriter writer = Files.newBufferedWriter(toFile)) {
      GSON.toJson(o, writer);
      System.out.println("Saved: " + toFile);
    } catch (IOException e) {
      System.out.println("Couldn't save " + toFile);
    }
  }

  private static Path shortenPath(Path toFile) {
    int len = toFile.getNameCount();
    return toFile.subpath(len - 2, len);
  }
}