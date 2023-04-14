import com.fasterxml.jackson.core.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CleanFiles {
  private ObjectMapper om = (new ObjectMapper()).setDefaultPrettyPrinter(new MyPrettyPrinter())
      .enable(SerializationFeature.INDENT_OUTPUT);

  private Path toRepo = Paths.get(System.getProperty("user.dir"));
  private PathMatcher jsonMatcher = FileSystems.getDefault()
      .getPathMatcher("regex:^(?!.*Colloquial|dynamic).*\\.json$");

  private List<String> dpProperties = Arrays.asList("name", "description", "tip");
  private List<String> entryProperties = Arrays.asList("prefix", "name", "completed");
  private List<String> properties = Arrays.asList("sourceString", "statName", "statNameAlt", "statNameAbbr",
      "statDescription", "itemTypeDisplayName", "displaySource", "substring", "progressDescription", "bubbles",
      "factions", "keywords");
  private List<String> inventoryProperties = Arrays.asList("tierTypeName");
  private List<String> extendedProperties = Arrays.asList("tip");
  private Map<Path, ObjectNode> jsonFiles;

  public CleanFiles(List<String> languages) {
    languages.forEach(lang -> {

      jsonFiles = getJsons(lang);
      jsonFiles.replaceAll(this::buildNodesForTranslation);

      // Sorts all files and removes objects no longer present in template
      // sortAndRemoveObj(lang);

      jsonFiles.forEach(this::saveJson);
    });
  }

  public static void main(String[] args) {
    List<String> languages = Arrays.asList("en");

    if (args[0].equals("template")) {
      System.out.println("Making template");
    }

    if (args[0].equals("clean")) {
      System.out.println("Cleaning files");
      languages = Arrays.asList("de", "en-OwO", "es", "es-MX", "fr", "it", "ja", "ko", "pl",
          "pt-BR", "ru", "zh-CHS", "zh-CHT");
    }

    new CleanFiles(languages);
    System.out.println("Done");
  }

  private Map<Path, ObjectNode> getJsons(String lang) {
    try {
      return Files.list(toRepo.resolve(lang).normalize())
          .filter(jsonMatcher::matches)
          .collect(Collectors.toMap(toFile -> Paths.get(lang, toFile.getFileName().toString()), this::loadJson));
    } catch (IOException e) {
      System.err.println("Error: couldn't list files");
      return null;
    }
  }

  private ObjectNode loadJson(Path path) {
    System.out.println("Loading: " + path);
    try {
      return Files.exists(path) ? (ObjectNode) om.readTree(Files.readAllBytes(path)) : om.createObjectNode();
    } catch (IOException e) {
      System.err.println("Error: couldn't read file: " + path.getFileName());
      return null;
    }
  };

  private ObjectNode buildNodesForTranslation(Path toFile, ObjectNode root) {
    Map<String, JsonNode> nodes = toStream(root.fields())
        .map(this::getNodeToTranslate)
        .filter(entry -> !entry.getValue().isEmpty())
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    return om.createObjectNode().setAll(nodes);
  }

  public static <T> java.util.stream.Stream<T> toStream(java.util.Iterator<T> iterator) {
    return java.util.stream.StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), true);
  }

  private Entry<String, JsonNode> getNodeToTranslate(Entry<String, JsonNode> entry) {
    JsonNode node = entry.getValue();
    ObjectNode newNode = om.createObjectNode();
    newNode.setAll(copyChildObj(node, "displayProperties", dpProperties));
    newNode.setAll(copyChildObj(node, "originalDisplayProperties", dpProperties));
    newNode.setAll(copyChildObj(node, "entry", entryProperties));
    newNode.setAll(copyObj(node, properties));
    newNode.setAll(copyChildObj(node, "inventory", inventoryProperties));
    newNode.setAll(copyChildObj(node, "extended", extendedProperties));
    return Map.entry(entry.getKey(), newNode);
  }

  private ObjectNode copyObj(JsonNode node, List<String> fieldNames) {
    ObjectNode newNode = om.createObjectNode();
    fieldNames.forEach(fieldName -> {
      JsonNode child = node.get(fieldName);
      if (child != null && !(child.isTextual() && child.textValue().equals("")))
        newNode.set(fieldName, child);
    });
    return newNode;
  }

  private ObjectNode copyChildObj(JsonNode node, String parentFieldName, List<String> fieldNames) {
    ObjectNode newNode = om.createObjectNode();
    fieldNames.forEach(fieldName -> {
      if (node.has(parentFieldName)) {
        ObjectNode newChild = copyObj(node.get(parentFieldName), fieldNames);
        if (!newChild.isEmpty())
          newNode.set(parentFieldName, newChild);
      }
    });
    return newNode;
  }

  private void sortAndRemoveObj(String lang) {
    if (!lang.equals("en")) {
      Map<Path, ObjectNode> templateFiles = getJsons("template");
      Map<Path, ObjectNode> t = new HashMap<Path, ObjectNode>();
      templateFiles.forEach((toFile, root) -> t.put(Paths.get(lang, toFile.getFileName().toString()),
          replaceNodesFromTemplate(toFile, root, lang)));
      jsonFiles = t;
    }
  }

  private ObjectNode replaceNodesFromTemplate(Path toFile, ObjectNode root, String lang) {
    Map<String, JsonNode> nodes = toStream(root.fields())
        .map(entry -> {
          ObjectNode newNode = om.createObjectNode();
          ObjectNode baseNode = jsonFiles.get(Paths.get(lang, toFile.getFileName().toString()));
          if (baseNode != null && baseNode.get(entry.getKey()) != null)
            newNode.setAll((ObjectNode) baseNode.get(entry.getKey()));
          return Map.entry(entry.getKey(), newNode);
        })
        .filter(entry -> !entry.getValue().isEmpty())
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    if (toFile.endsWith("DestinyObjectiveDefinition.json"))
      nodes.putAll(addBowNodes(toFile, lang));
    return om.createObjectNode().setAll(nodes);
  }

  private Map<String, JsonNode> addBowNodes(Path toFile, String lang) {
    Map<String, JsonNode> properties = new LinkedHashMap<String, JsonNode>();
    ObjectNode node = jsonFiles.get(Paths.get(lang, toFile.getFileName().toString()));
    String bow = new String(new byte[] { (byte) 0xEE, (byte) 0x82, (byte) 0x99 },
        java.nio.charset.Charset.forName("UTF-8"));
    if (node != null)
      properties = toStream(node.fields())
          .filter(entry -> entry.getValue().get("progressDescription").toString().contains(bow))
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    return properties;
  }

  private void saveJson(Path toFile, ObjectNode node) {
    try {
      if (node != null && !node.isEmpty()) {
        if (toFile.getParent().endsWith("en")) {
          toFile = Paths.get("template", toFile.getFileName().toString());
        }
        Files.write(toRepo.resolve(toFile).normalize(), om.writeValueAsBytes(node));
        System.out.println("Saved: " + toFile);
      } else {
        System.out.println("Empty node: " + toFile);
      }
    } catch (IOException e) {
      System.out.println("Couldn't save " + toFile);
    }
  }

  class MyPrettyPrinter extends DefaultPrettyPrinter {
    public MyPrettyPrinter() {
      Indenter indenter = new DefaultIndenter("  ", System.getProperty("line.separator"));
      indentObjectsWith(indenter);
      indentArraysWith(indenter);
      _objectFieldValueSeparatorWithSpaces = ": ";
    }

    @Override
    public DefaultPrettyPrinter createInstance() {
      return new MyPrettyPrinter();
    }

    @Override
    public void writeEndArray(com.fasterxml.jackson.core.JsonGenerator g, int nrOfValues) throws IOException {
      if (!_arrayIndenter.isInline())
        --_nesting;
      if (nrOfValues > 0)
        _arrayIndenter.writeIndentation(g, _nesting);
      g.writeRaw(']');
    }
  }
}