import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
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
  private Map<String, JsonNode> jsonFiles;

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
      jsonFiles.replaceAll(this::buildNodesForTranslation);
      jsonFiles.forEach((filename, node) -> {
        try {
          if (node != null && !node.isEmpty()) {
            om.writeValue(new File(outPath, filename), node);
            System.out.println("Saved: " + outPath + "/" + filename);
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

  private Map<String, JsonNode> loadJsonFiles(String lang) {
    filenames = Arrays.asList((new File(repoPath, lang))
        .list((dir, name) -> name.endsWith(".json") && !name.endsWith("Colloquial.json")
            && !name.endsWith("dynamic.json")));

    Iterator<String> iter = filenames.iterator();

    return filenames.stream().map(name -> {
      File f = new File(repoPath + "/" + lang, name);
      System.out.println("Loading: " + repoPath + "/" + lang + "/" + name);
      try {
        return f.exists() ? om.readTree(f) : om.createObjectNode();
      } catch (IOException e) {
        System.err.println("Error: couldn't read file: " + f.getName());
        return null;
      }
    }).collect(Collectors.toMap(i -> iter.next(), Function.identity()));
  }

  private ObjectNode buildNodesForTranslation(String filename, JsonNode node) {
    ObjectNode newRoot = null;
    if (root != null)
      newRoot = om.createObjectNode().setAll(toStream(root.fields())
          .map(entry -> getNodeForTranslation(entry))
          .filter(entry -> !entry.getValue().isEmpty())
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> a, LinkedHashMap::new)));
    return newRoot;
  }

  public static <T> Stream<T> toStream(Iterator<T> iterator) {
    return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), false);
  }

  private Entry<String, JsonNode> getNodeForTranslation(Entry<String, JsonNode> entry) {
    JsonNode node = entry.getValue();
    ObjectNode newNode = om.createObjectNode();
    copyChildObj(node, "displayProperties", newNode, dpProperties);
    copyChildObj(node, "originalDisplayProperties", newNode, dpProperties);
    copyChildObj(node, "entry", newNode, entryProperties);
    copyObj(node, newNode, properties);
    copyChildObj(node, "inventory", newNode, inventoryProperties);
    copyChildObj(node, "extended", newNode, extendedProperties);
    return Map.entry(entry.getKey(), newNode);
  }

  private void copyObj(JsonNode node, ObjectNode newNode, List<String> fieldNames) {
    fieldNames.forEach(fieldName -> {
      JsonNode child = node.get(fieldName);
      if (child != null && !(child.isTextual() && child.textValue().equals(""))) {
        newNode.set(fieldName, child);
      }
    });
  }

  private void copyChildObj(JsonNode node, String parentFieldName, ObjectNode newNode,
      List<String> fieldNames) {
    fieldNames.forEach(fieldName -> {
      if (node.has(parentFieldName)) {
        JsonNode child = node.get(parentFieldName);
        ObjectNode newChild = om.createObjectNode();
        copyObj(child, newChild, fieldNames);
        if (!newChild.isEmpty()) {
          newNode.set(parentFieldName, newChild);
        }
      }
    });
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
      if (!_arrayIndenter.isInline()) {
        --_nesting;
      }
      if (nrOfValues > 0) {
        _arrayIndenter.writeIndentation(g, _nesting);
      }
      g.writeRaw(']');
    }
  }
}