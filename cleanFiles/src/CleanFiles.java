import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CleanFiles {
  public static void main(final String[] args) {
    String[] langs = new String[] { "en" };

    if (args[0].equals("template")) {
      System.out.println("Making template");
    }

    if (args[0].equals("clean")) {
      System.out.println("Cleaning files");
      langs = new String[] { "de", "es", "es-MX", "fr", "it", "ja", "ko", "pl", "pt-BR", "ru", "zh-CHS", "zh-CHT" };
    }

    new CleanFiles(langs);
    System.out.println("Done");
  }

  private final com.google.gson.Gson gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
      .create();

  private final Path toRepo = Paths.get(System.getProperty("user.dir"));

  public CleanFiles(final String... langs) {
    final List<Definition> defs = getDefs(java.util.Arrays.asList(langs));
    defs.parallelStream().forEach(Definition::removeEmptyFields);
    defs.parallelStream().forEach(this::save);
  }

  private java.util.stream.Stream<Definition> getDefs(final String lang) {
    try {
      return Files.list(toRepo.resolve(lang).normalize()).parallel()
          .filter(Definition.DEF_MATCHER::matches)
          .map(this::load)
          .filter(java.util.Objects::nonNull);
    } catch (final IOException e) {
      System.err.println("Error: couldn't list files");
      return null;
    }
  }

  private Definition load(final Path fullPath) {
    Definition def = null;
    System.out.println("Loading: " + fullPath);
    try (java.io.Reader r = Files.newBufferedReader(fullPath)) {
      def = new Definition(shortenPath(fullPath), gson.fromJson(r, Definition.DEF_TYPE));
    } catch (final IOException e) {
      System.err.println("Error: couldn't read file: " + fullPath);
    }
    return def;
  }

  private List<Definition> getDefs(final List<String> langs) {
    return langs.parallelStream()
        .flatMap(this::getDefs)
        .collect(java.util.stream.Collectors.toList());
  }

  private void save(final Definition def) {
    final Path toFile = def.getToFile();
    if (!def.isEmpty()) {
      try (java.io.Writer w = Files.newBufferedWriter(toRepo.resolve(toFile).normalize())) {
        gson.toJson(def.getProperties(), w);
        System.out.println("Saved: " + toFile);
      } catch (final IOException e) {
        System.out.println("Couldn't save " + toFile);
      }
    } else {
      System.out.println("Empty node: " + toFile);
    }
  }

  private Path shortenPath(final Path toFile) {
    final int len = toFile.getNameCount();
    Path toFileRelative = toFile.subpath(len - 2, len);
    if (toFile.getParent().endsWith("en"))
      toFileRelative = Paths.get("template", toFileRelative.getFileName().toString());
    return toFileRelative;
  }
}