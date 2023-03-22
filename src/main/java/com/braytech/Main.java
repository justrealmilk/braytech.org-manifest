package com.braytech;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
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

public class Main {
  private ObjectMapper om;
  private String repoPath;
  private String outPath;
  private List<String> filenames;
  private List<String> languages;
  private Map<String, JsonNode> jsonFiles;
  
  public Main() {
    repoPath = System.getProperty("user.dir");
    om = (new ObjectMapper()).setDefaultPrettyPrinter(new MyPrettyPrinter())
        .enable(SerializationFeature.INDENT_OUTPUT);

    languages = Arrays.asList("en", "de", "en-OwO", "es", "es-MX", "fr", "it", "ja", "ko", "pl",
        "pt-BR", "ru", "zh-CHS", "zh-CHT");
    
    Instant start = Instant.now();

    languages.forEach(lang -> {
      jsonFiles = loadJsonFiles(lang);
      jsonFiles.replaceAll(this::buildNodesForTranslation);
      jsonFiles.forEach((filename, node) -> {
        try {
          if (node != null && !node.isEmpty()) {
            om.writeValue(new File(outPath, filename), node);
          }
        } catch (IOException e) {
          System.out.println("Couldn't save " + filename);
        }
      });
    });

    Instant finish = Instant.now();
    System.out.println("Done in " + Duration.between(start, finish).toMillis() + "ms.");
  }

  public static void main(String[] args) {
    new Main();
  }

  private Map<String, JsonNode> loadJsonFiles(String lang) {
    outPath = repoPath + "\\" + (lang.equals("en") ? "template" : lang);

    new File(outPath).mkdir();

    filenames = Arrays.asList((new File(repoPath, lang))
        .list((dir, name) -> name.endsWith(".json") && !name.endsWith("Colloquial.json")
            && !name.endsWith("dynamic.json")));

    Iterator<String> iter = filenames.iterator();

    return filenames.stream().map(name -> {
      File f = new File(repoPath + "\\" + lang, name);
      try {
        return f.exists() ? om.readTree(f) : om.createObjectNode();
      } catch (IOException e) {
        System.err.println("Error: couldn't read file: " + f.getName());
        return null;
      }
    }).collect(Collectors.toMap(i -> iter.next(), Function.identity()));
  }

  private ObjectNode buildNodesForTranslation(String filename, JsonNode node) {
    return (node != null ? om.createObjectNode().setAll(filterNodes(node)) : null);
  }

  private LinkedHashMap<String, JsonNode> filterNodes(JsonNode root) {
    return toStream(root.fields())
        .map(entry -> removeUselessNodes(entry))
        .filter(entry -> isNodeToTranslate(entry))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> a, LinkedHashMap::new));
  }

  public static <T> Stream<T> toStream(Iterator<T> iterator) {
    return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), false);
  }

  private boolean isNodeToTranslate(Entry<String, JsonNode> entry) {
    JsonNode node = entry.getValue();
    JsonNode dp = node.get("displayProperties");
    JsonNode odp = node.get("originalDisplayProperties");
    JsonNode e = node.get("entry");
    JsonNode e1 = node.get("extended");
    return (dp != null && (dp.has("name")
        || dp.has("description")
        || dp.has("tip")))
        || (odp != null && (odp.has("name")
            || odp.has("description")))
        || (e != null && (e.has("prefix")
            || e.has("name")
            || e.has("completed")))
        || (e1 != null && (e1.has("tip")))
        || ((node.has("sourceString")
            || node.has("statName")
            || node.has("statNameAlt")
            || node.has("statNameAbbr")
            || node.has("statDescription")
            || node.has("itemTypeDisplayName")
            || node.has("displaySource")
            || node.has("substring")
            || node.has("progressDescription")
            || node.has("bubbles")
            || node.has("factions")
            || node.has("keywords")));
  }

  private Entry<String, JsonNode> removeUselessNodes(Entry<String, JsonNode> entry) {
    JsonNode node = entry.getValue();
    ObjectNode newNode = om.createObjectNode();
    copyChildObj(node, "displayProperties", newNode, Arrays.asList("name", "description", "tip"));
    copyChildObj(node, "originalDisplayProperties", newNode, Arrays.asList("name", "description"));
    copyChildObj(node, "entry", newNode, Arrays.asList("prefix", "name", "completed"));
    copyObj(node, newNode, Arrays.asList(
        "sourceString", "statName", "statNameAlt", "statNameAbbr", "statDescription",
        "itemTypeDisplayName", "displaySource", "substring", "progressDescription", "bubbles",
        "factions", "keywords"));
    copyChildObj(node, "inventory", newNode, Arrays.asList("tierTypeName"));
    copyChildObj(node, "extended", newNode, Arrays.asList("tip"));
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