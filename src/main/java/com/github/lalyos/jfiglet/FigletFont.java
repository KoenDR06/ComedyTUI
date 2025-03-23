package com.github.lalyos.jfiglet;
import java.util.*;
import java.io.*;

public class FigletFont {
  public char hardblank;
  public int height;
  public int heightWithoutDescenders;
  public int maxLine;
  public int smushMode;
  public Integer printDirection = null;
  public Integer fullLayout = null;
  public Integer codetagCount = null;
  public char[][][] font;
  public String fontName = "";

  SmushingRulesToApply smushingRulesToApply;

  final public static int MAX_CHARS = 1024;
  final public static int REGULAR_CHARS = 102;

  public char[][] getChar(int c) {
    return font[c];
  }

  public FigletFont(InputStream stream) throws IOException {
      font = new char[MAX_CHARS][][];

      String dummyS;
      int commentLines;
      int charCode;
      try (BufferedReader data = new BufferedReader(
      new InputStreamReader(new BufferedInputStream(stream), "UTF-8"))) {

          dummyS = data.readLine();
          StringTokenizer st = new StringTokenizer(dummyS, " ");
          String s = st.nextToken();
          hardblank = s.charAt(s.length() - 1);
          height = Integer.parseInt(st.nextToken());
          heightWithoutDescenders = Integer.parseInt(st.nextToken());
          maxLine = Integer.parseInt(st.nextToken());
          smushMode = Integer.parseInt(st.nextToken());
          commentLines = Integer.parseInt(st.nextToken());
          if (st.hasMoreTokens()) {
              printDirection = Integer.parseInt(st.nextToken());
          }
          if (st.hasMoreTokens()) {
              fullLayout = Integer.parseInt(st.nextToken());
          }
          if (st.hasMoreTokens()) {
              codetagCount = Integer.parseInt(st.nextToken());
          }

          smushingRulesToApply = Smushing.getRulesToApply(smushMode, fullLayout);

          if (commentLines > 0) {
              st = new StringTokenizer(data.readLine(), " ");
              if (st.hasMoreElements())
                  fontName = st.nextToken();
          }

          int[] charsTo = new int[REGULAR_CHARS];

          int j = 0;
          for (int c = 32; c <= 126; ++c) {
              charsTo[j++] = c;
          }
          for (int additional : new int[]{196, 214, 220, 228, 246, 252, 223}) {
              charsTo[j++] = additional;
          }

          for (int i = 0; i < commentLines - 1; i++) // skip the comments
              dummyS = data.readLine();
          int charPos = 0;
          while (dummyS != null) {  // for all the characters
              //System.out.print(i+":");
              if (charPos < REGULAR_CHARS) {
                  charCode = charsTo[charPos++];
              } else {
                  dummyS = data.readLine();
                  if (dummyS == null) {
                      continue;
                  }
                  charCode = convertCharCode(dummyS);
              }
              for (int h = 0; h < height; h++) {
                  dummyS = data.readLine();
                  if (dummyS != null) {
                      if (h == 0)
                          font[charCode] = new char[height][];
                      int t = dummyS.length() - 1 - ((h == height - 1) ? 1 : 0);
                      if (height == 1)
                          t++;
                      font[charCode][h] = new char[t];
                      for (int l = 0; l < t; l++) {
                          char a = dummyS.charAt(l);
                          font[charCode][h][l] = a;
                      }
                  }
              }
          }
      }
  }

  int convertCharCode(String input){
    String codeTag = input.concat(" ").split(" ")[0];
    if (codeTag.matches("^0[xX][0-9a-fA-F]+$")) {
      return Integer.parseInt(codeTag.substring(2), 16);
    } else if (codeTag.matches("^0[0-7]+$")) {
      return Integer.parseInt(codeTag.substring(1), 8);
    } else {
      return Integer.parseInt(codeTag);
    }
  }

  public String convert(String message) {
    char[][] convertedMessage = Smushing.convert(this, message);
    StringBuilder result = new StringBuilder();
    for(int l = 0; l < this.height; l++){
      result.append(convertedMessage[l]);
      result.append('\n');
    }
    return result.toString().replace(hardblank, ' ');
  }
}
