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
          .map(FileUtils::load)
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
        .filter(Objects::nonNull)
        .parallel();
  }

  public static Definition load(Path fullPath) {
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

  public static void save(final Definition def) {
    Path toFile = Paths.get(def.getLang(), def.getFileName());
    Path fullPath = TO_REPO.resolve(toFile).normalize();
    if (!def.isEmpty()) {
      try (BufferedWriter writer = Files.newBufferedWriter(fullPath)) {
        GSON.toJson(def.getProperties(), writer);
        System.out.println("Saved: " + toFile);
      } catch (IOException e) {
        System.out.println("Couldn't save " + toFile);
      }
    } else {
      System.out.println("Empty node: " + toFile);
    }
  }

  public static void saveSorted(final Definition def) {
    // loading template to get property order
    final Definition sortedDef = getDefs("template")
        .filter(d -> d.getFileName().equals(def.getFileName()))
        .findAny().orElse(null);
    if (sortedDef == null)
      return;
    // change path
    sortedDef.setLang(def.getLang());
    // set untraslated properties to null
    sortedDef.replacePropertiesFrom(def);
    // add back "bow" properties (not present in template)
    if (def.getFileName().endsWith("DestinyObjectiveDefinition.json"))
      sortedDef.getProperties().putAll(def.getBowProperties());

    save(sortedDef);
  }

  private static Path shortenPath(Path toFile) {
    int len = toFile.getNameCount();
    Path toFileShort = toFile.subpath(len - 2, len);
    if (toFileShort.getParent().endsWith("en"))
      toFileShort = Paths.get("template", toFileShort.getFileName().toString());
    return toFileShort;
  }

}
