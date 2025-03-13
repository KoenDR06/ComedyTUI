package com.github.lalyos.jfiglet;

import java.util.*;

enum SmushingRule implements Comparable<SmushingRule> {
  HORIZONTAL_EQUAL_CHARACTER_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 1){
    @Override
    Character smush(char char1, char char2, char hardblank) {
      return char1 != hardblank && char1 == char2 ? char1 : null;
    }
  },

  HORIZONTAL_UNDERSCORE_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 2) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      final String chars = "|/\\[]{}()<>";
      if (char1 == '_' && chars.indexOf(char2) != -1) {
        return char2;
      } else if (char2 == '_' && chars.indexOf(char1) != -1) {
        return char1;
      } else {
        return null;
      }
    }
  },

  HORIZONTAL_HIERARCHY_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 4) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      String classes = "| /\\ [] {} () <>";
      int pos1 = classes.indexOf(char1);
      int pos2 = classes.indexOf(char2);
      if (pos1 != -1 && pos2 != -1) {
        if (pos1 != pos2 && Math.abs(pos1-pos2) != 1) {
          return classes.substring(Math.max(pos1,pos2), Math.max(pos1,pos2) + 1).charAt(0);
        }
      }
      return null;
    }
  },

  HORIZONTAL_OPPOSITE_PAIR_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 8) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      String opposingBrackets = "[] {} ()";
      int pos1 = opposingBrackets.indexOf(char1);
      int pos2 = opposingBrackets.indexOf(char2);
      if (pos1 != -1 && pos2 != -1) {
        if (pos1 != pos2 && Math.abs(pos1-pos2) == 1) {
          return '|';
        }
      }
      return null;
    }
  },

  HORIZONTAL_BIG_X_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 16) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      if(char1 == '/' && char2 == '\\'){
        return '|';
      } else if (char1 == '\\' && char2 == '/') {
        return 'Y';
      } else if (char1 == '>' && char2 == '<') {
        return 'X';
      } else {
        return null;
      }
    }
  },

  HORIZONTAL_HARDBLANK_SMUSHING(Type.HORIZONTAL, Layout.CONTROLLED_SMUSHING, 32) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
       return char1 == hardblank && char2 == hardblank ? hardblank : null;
    }
  },

  HORIZONTAL_FITTING(Type.HORIZONTAL, Layout.FITTING, 64) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      return char1 == ' ' && char2 == ' ' ? ' ' : null;
    }
  },

  HORIZONTAL_SMUSHING(Type.HORIZONTAL, Layout.SMUSHING, 128) {
    @Override
    Character smush(char char1, char char2, char hardblank) {
      return char1 != hardblank && char2 != hardblank ? char2 : null;
    }
  },

  VERTICAL_EQUAL_CHARACTER_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 256),

  VERTICAL_UNDERSCORE_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 512),

  VERTICAL_HIERARCHY_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 1024),

  VERTICAL_HORIZONTAL_LINE_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 2048),

  VERTICAL_VERTICAL_LINE_SMUSHING(Type.VERTICAL, Layout.CONTROLLED_SMUSHING, 4096),

  VERTICAL_FITTING(Type.VERTICAL, Layout.FITTING, 8192),

  VERTICAL_SMUSHING(Type.VERTICAL, Layout.SMUSHING, 16384)
  ;

  private final Type type;
  private final Layout layout;
  private final int codeValue;


  enum Type {HORIZONTAL, VERTICAL}

  enum Layout{FULL_WIDTH, FITTING, SMUSHING, CONTROLLED_SMUSHING}

  private static final Map<Integer, SmushingRule> codeValueToRule;

  static {
    Map<Integer, SmushingRule> results = new HashMap<>();
    for (SmushingRule rule : SmushingRule.values()){
      results.put(rule.getCodeValue(), rule);
    }
    codeValueToRule = Collections.unmodifiableMap(results);
  }


  SmushingRule(Type type, Layout layout, int codeValue){
    this.type = type;
    this.layout = layout;
    this.codeValue = codeValue;
  }

  Character smush(char char1, char char2, char hardblank) {
    return null;
  }

  boolean smushes(char char1, char char2, char hardblank) {
    return smush(char1, char2, hardblank) != null;
  }

  int getCodeValue() {
    return codeValue;
  }

  Type getType() {
    return type;
  }

  Layout getLayout() {
    return layout;
  }

  static SmushingRule getByCodeValue(int codeValue){
    return codeValueToRule.get(codeValue);
  }

  static List<Integer> getAvailableCodeValues(){
    List<Integer> list = new ArrayList<>(codeValueToRule.keySet());
    Collections.sort(list);
    Collections.reverse(list);
    return Collections.unmodifiableList(list);
  }
}

