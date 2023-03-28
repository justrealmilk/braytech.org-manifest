import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CleanFiles {
  private ObjectMapper om;
  private String repoPath;
  private List<String> filenames;
  private List<String> dpProperties;
  private List<String> entryProperties;
  private List<String> properties;
  private List<String> inventoryProperties;
  private List<String> extendedProperties;
  private Map<String, ObjectNode> jsonFiles;

  public CleanFiles(List<String> languages) {
    repoPath = System.getProperty("user.dir");

    dpProperties = Arrays.asList("name", "description", "tip");
    entryProperties = Arrays.asList("prefix", "name", "completed");
    properties = Arrays.asList("sourceString", "statName", "statNameAlt", "statNameAbbr", "statDescription",
        "itemTypeDisplayName", "displaySource", "substring", "progressDescription", "bubbles", "factions",
        "keywords");
    inventoryProperties = Arrays.asList("tierTypeName");
    extendedProperties = Arrays.asList("tip");

    om = (new ObjectMapper()).setDefaultPrettyPrinter(new MyPrettyPrinter())
        .enable(SerializationFeature.INDENT_OUTPUT);

    languages.forEach(lang -> {
      String outPath = repoPath + "/" + (lang.equals("en") ? "template" : lang);

      jsonFiles = loadJsonFiles(lang);
      jsonFiles.replaceAll((filename, entry) -> buildNodesForTranslation(filename, entry, false));

      // Sorts all files and removes objects no longer present in template
      // sortAndRemoveObj(lang);

      jsonFiles.forEach((filename, node) -> {
        try {
          if (node != null && !node.isEmpty()) {
            om.writeValue(new File(outPath, filename), node);
            System.out.println("Saved: " + outPath + "/" + filename);
          } else {
            System.out.println("Empty node: " + filename);
          }
        } catch (IOException e) {
          System.out.println("Couldn't save " + filename);
        }
      });
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

  private Map<String, ObjectNode> loadJsonFiles(String lang) {
    filenames = Arrays.asList((new File(repoPath, lang))
        .list((dir, name) -> name.endsWith(".json") && !name.endsWith("Colloquial.json")
            && !name.endsWith("dynamic.json")));

    Iterator<String> iter = filenames.iterator();

    return filenames.stream().map(name -> {
      File f = new File(repoPath + "/" + lang, name);
      System.out.println("Loading: " + repoPath + "/" + lang + "/" + name);
      try {
        return f.exists() ? (ObjectNode) om.readTree(f) : om.createObjectNode();
      } catch (IOException e) {
        System.err.println("Error: couldn't read file: " + f.getName());
        return null;
      }
    }).collect(Collectors.toMap(i -> iter.next(), Function.identity()));
  }

  private ObjectNode buildNodesForTranslation(String filename, ObjectNode root, boolean sortAndRemove) {
    Map<String, JsonNode> nodes = toStream(root.fields())
        .map(entry -> getNodeToTranslate(filename, entry, sortAndRemove))
        .filter(entry -> !entry.getValue().isEmpty())
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    if (sortAndRemove && filename.equals("DestinyObjectiveDefinition.json"))
      nodes.putAll(addBowNodes(filename));
    return om.createObjectNode().setAll(nodes);
  }

  public static <T> Stream<T> toStream(Iterator<T> iterator) {
    return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), true);
  }

  private Entry<String, JsonNode> getNodeToTranslate(String filename, Entry<String, JsonNode> entry,
      boolean sortAndRemove) {
    JsonNode node = entry.getValue();
    ObjectNode newNode = om.createObjectNode();
    if (sortAndRemove) {
      ObjectNode boop = jsonFiles.get(filename);
      if (boop != null && boop.get(entry.getKey()) != null)
        newNode.setAll((ObjectNode) boop.get(entry.getKey()));
    } else {
      newNode.setAll(copyChildObj(node, "displayProperties", dpProperties));
      newNode.setAll(copyChildObj(node, "originalDisplayProperties", dpProperties));
      newNode.setAll(copyChildObj(node, "entry", entryProperties));
      newNode.setAll(copyObj(node, properties));
      newNode.setAll(copyChildObj(node, "inventory", inventoryProperties));
      newNode.setAll(copyChildObj(node, "extended", extendedProperties));
    }
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

  private Map<String, JsonNode> addBowNodes(String filename) {
    Map<String, JsonNode> properties = new LinkedHashMap<String, JsonNode>();
    ObjectNode node = jsonFiles.get(filename);
    String bow = new String(new byte[] { (byte) 0xEE, (byte) 0x82, (byte) 0x99 }, Charset.forName("UTF-8"));
    if (node != null)
      properties = toStream(node.fields())
          .filter(entry -> entry.getValue().get("progressDescription").toString().contains(bow))
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    return properties;
  }

  private void sortAndRemoveObj(String lang) {
    if (!lang.equals("en")) {
      Map<String, ObjectNode> t = loadJsonFiles("template");
      t.replaceAll((filename, entry) -> buildNodesForTranslation(filename, entry, true));
      jsonFiles = t;
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
    public void writeEndArray(JsonGenerator g, int nrOfValues) throws IOException {
      if (!_arrayIndenter.isInline())
        --_nesting;
      if (nrOfValues > 0)
        _arrayIndenter.writeIndentation(g, _nesting);
      g.writeRaw(']');
    }
  }
}