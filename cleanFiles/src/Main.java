public class Main {

  private final static String[] EN = new String[] { "en" };
  private final static String[] ALL = new String[] { "de", "es", "es-MX", "fr", "it", "ja",
      "ko", "pl", "pt-BR", "ru", "zh-CHS", "zh-CHT" };

  public static void main(final String[] args) {
    if (args.length == 1) {
      /*
       * "template":
       * with this parameter the script extrapolates the definitions' properties
       * needed for translation-only from 'en' and saves them in the template folder.
       * It preserves the original 'en' property order.
       */
      if (args[0].equals("template")) {
        System.out.println("Making template");
        FileUtils.getDefs(EN).forEach(FileUtils::save);
      }

      /*
       * "clean":
       * with this parameter the script takes each language definitions and removes
       * any unnecessary (for translation purposes) properties.
       * This does *not* remove any outdated properties nor does it confront in any
       * way the json property order with the en/template order.
       */
      if (args[0].equals("clean")) {
        System.out.println("Cleaning files");
        FileUtils.getDefs(ALL).forEach(FileUtils::save);
      }

      /*
       * "sort":
       * with this parameter the script takes all languages definitions (minus 'en').
       * It extrapolates and applies them on top of the template json property order.
       * Doing so, any outdated property will be removed as it won't be present in
       * template.
       * Note: missing properties (not translated) will still not be copied over as
       * doing this will make untraslated properties undistiguishable from translated
       * ones.
       */
      if (args[0].equals("sort")) {
        System.out.println("Sorting files");
        FileUtils.getDefs(ALL).forEach(FileUtils::saveSorted);
      }
      System.out.println("Done");
    } else
      System.out.println("Try running with an arg (template, clean, sort)");
  }
}