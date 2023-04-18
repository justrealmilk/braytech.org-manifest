import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CleanFiles {
  private final static com.google.gson.Gson GSON = new com.google.gson.GsonBuilder()
      .disableHtmlEscaping()
      .setPrettyPrinting()
      .create();

  private final static Path TO_REPO = Paths.get(System.getProperty("user.dir"));

  public static void main(final String[] args) {
    String[] langs = new String[] { "en" };

    if (args[0].equals("template")) {
      System.out.println("Making template");
    }

    if (args[0].equals("clean")) {
      System.out.println("Cleaning files");
      langs = new String[] { "de", "es", "es-MX", "fr", "it", "ja", "ko", "pl", "pt-BR", "ru", "zh-CHS", "zh-CHT" };
    }

    getDefs(langs).forEach(CleanFiles::save);

    System.out.println("Done");
  }

  private static Stream<Definition> getDefs(final String lang) {
    try {
      return Files.list(TO_REPO.resolve(lang).normalize()).parallel()
          .filter(Definition.DEF_MATCHER::matches)
          .map(CleanFiles::load)
          .map(Definition::removeEmptyFields)
          .filter(java.util.Objects::nonNull);
    } catch (final IOException e) {
      System.err.println("Error: couldn't list files");
      return null;
    }
  }

  private static Definition load(final Path fullPath) {
    Definition def = null;
    System.out.println("Loading: " + fullPath);
    try (java.io.Reader r = Files.newBufferedReader(fullPath)) {
      def = new Definition(shortenPath(fullPath), GSON.fromJson(r, Definition.DEF_TYPE));
    } catch (final IOException e) {
      System.err.println("Error: couldn't read file: " + fullPath);
    }
    return def;
  }

  private static Stream<Definition> getDefs(final String[] langs) {
    return java.util.Arrays.asList(langs)
        .parallelStream()
        .flatMap(CleanFiles::getDefs)
        .filter(java.util.Objects::nonNull)
        .parallel();
  }

  private static void save(final Definition def) {
    final Path toFile = def.getToFile();
    if (!def.isEmpty()) {
      try (java.io.Writer w = Files.newBufferedWriter(TO_REPO.resolve(toFile).normalize())) {
        GSON.toJson(def.getProperties(), w);
        System.out.println("Saved: " + toFile);
      } catch (final IOException e) {
        System.out.println("Couldn't save " + toFile);
      }
    } else {
      System.out.println("Empty node: " + toFile);
    }
  }

  private static Path shortenPath(final Path toFile) {
    Path toFileRelative = toFile.subpath(toFile.getNameCount() - 2, toFile.getNameCount());
    if (toFileRelative.getParent().endsWith("en"))
      toFileRelative = Paths.get("template", toFileRelative.getFileName().toString());
    return toFileRelative;
  }
}