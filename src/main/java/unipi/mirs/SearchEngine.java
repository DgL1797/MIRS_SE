package unipi.mirs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import unipi.mirs.components.IndexBuilder;
import unipi.mirs.graphics.ConsoleUX;
import unipi.mirs.graphics.Menu;
import unipi.mirs.utilities.Constants;

public class SearchEngine {

  private static String inputFile = Paths.get(Constants.INPUT_DIR.toString(), "collection.tsv").toString();
  private static boolean readCompressed = false;
  private static final Scanner stdin = new Scanner(System.in);

  /**
   * Allows the user to select a new file as collection of the search engine, the collection has to be in .tsv format
   * with 'docno\tdocbody\n' format, the tsv file can also be passed with gzip commpression and the function will
   * automatically select if the file needs to be parsed with decompressor or not It will modify inputFile and
   * readCompressed class' static parameters in-place.
   * 
   * @throws IOException
   */
  private static void changeInputFile() throws IOException {
    // while inserted file not found keep saying: "Error ${filename} not in input folder"
    File inputDir = new File(Constants.INPUT_DIR.toString());

    String[] files = Arrays.asList(inputDir.listFiles()).stream().filter((f) -> f.isFile())
        .filter((f) -> f.toString().matches(".*\\.gz$") || f.toString().matches(".*\\.tsv$")).map(f -> f.toString())
        .toArray(String[]::new);

    if (Arrays.asList(files).size() == 0) {
      System.out
          .println(ConsoleUX.FG_RED + ConsoleUX.BOLD + "No files found, make sure to import a .tsv or .gz file inside "
              + Constants.INPUT_DIR + " folder" + ConsoleUX.RESET);
      inputFile = "";
    }
    Menu filesMenu = new Menu(stdin, files);
    inputFile = files[filesMenu.printMenu()];
    if (inputFile.matches(".*\\.gz$"))
      readCompressed = true;
  }

  private static void buildIndex() {
    if (readCompressed) {

    } else {
      try (BufferedReader inreader = Files.newBufferedReader(Path.of(inputFile), StandardCharsets.UTF_8)) {
        String document;
        IndexBuilder ib = new IndexBuilder(5000);
        while ((document = inreader.readLine()) != null) {
          // addDocument will automatically save as it reaches its limit and reset itself
          ib.addDocument(document);
        }
        // ib.write();
      } catch (IOException e) {
        System.out.println(ConsoleUX.FG_RED + ConsoleUX.BOLD + "Unable to initialize buffer for " + inputFile + ":\n"
            + e.getMessage() + ConsoleUX.RESET);
        ConsoleUX.pause(false, stdin);
      }
    }
    // open inputfile using BufferedReader or ZipBufferedReader based on compressedRead
    // if compressed file, decompress it and move it to a normal Buffered stream
    // read up to 5000 lines
    //   create IndexBuilder instance
    //   normilize docbodies
    //   update IndexBuilder instance
    //   write IndexBuilder to file
    //   reset IndexBuilder instance
  }

  public static void main(String[] args) throws IOException {
    // Save index in a file, compressed index in another one to avoid re-building index every time
    Menu menu = new Menu(stdin, "Change Input File", "Build Index", "Compress Inverted Index", "Search", "Exit");
    int opt = 0;
    while ((opt = menu
        .printMenu(ConsoleUX.FG_BLUE + ConsoleUX.BOLD + "Selected File: " + inputFile + ConsoleUX.RESET)) != 4) {
      if (opt == 0) {
        changeInputFile();
      } else if (opt == 1) {

      } else if (opt == 2) {

      } else if (opt == 3) {

      }
    }
    System.out.println(ConsoleUX.CLS + ConsoleUX.FG_YELLOW + ConsoleUX.BOLD + "UOT MATTEMATTICALLI DU UI DU!");
    System.out.println("DU UI LAIK TU UEIST TIME? 0,0001 milliseconds to print" + ConsoleUX.RESET);
    System.exit(0);
  }
}
