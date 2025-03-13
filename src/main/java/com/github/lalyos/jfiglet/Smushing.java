package com.github.lalyos.jfiglet;

import java.util.*;

class Smushing {
  static SmushingRulesToApply getRulesToApply(Integer oldLayout, Integer fullLayout){
    List<SmushingRule> horizontalSmushingRules = new ArrayList<>();
    List<SmushingRule> verticalSmushingRules = new ArrayList<>();
    SmushingRule.Layout horizontalLayout = null;
    SmushingRule.Layout verticalLayout = null;
    int layout = fullLayout != null ? fullLayout : oldLayout;
    for(Integer codeValue : SmushingRule.getAvailableCodeValues()){
      if(layout >= codeValue){
        layout = layout - codeValue;
        SmushingRule rule = SmushingRule.getByCodeValue(codeValue);
        if(rule.getType() == SmushingRule.Type.HORIZONTAL) {
          horizontalLayout = rule.getLayout();
          horizontalSmushingRules.add(rule);
        } else if (rule.getType() == SmushingRule.Type.VERTICAL) {
          verticalLayout = rule.getLayout();
          verticalSmushingRules.add(rule);
        }
      }
    }
    if(horizontalLayout == null){
      if (oldLayout == 0){
        horizontalLayout = SmushingRule.Layout.FITTING;
        horizontalSmushingRules.add(SmushingRule.HORIZONTAL_FITTING);
      } else if (oldLayout == -1){
        horizontalLayout = SmushingRule.Layout.FULL_WIDTH;
      }
    } else if (horizontalLayout == SmushingRule.Layout.CONTROLLED_SMUSHING){
      horizontalSmushingRules.remove(SmushingRule.HORIZONTAL_SMUSHING);
    }
    if(verticalLayout == null){
      verticalLayout = SmushingRule.Layout.FULL_WIDTH;
    } else if (verticalLayout == SmushingRule.Layout.CONTROLLED_SMUSHING){
      verticalSmushingRules.remove(SmushingRule.VERTICAL_SMUSHING);
    }
    return new SmushingRulesToApply(horizontalLayout, verticalLayout, horizontalSmushingRules, verticalSmushingRules);
  }

  static char[][] convert(FigletFont figletFont, String message){
    char[][] result = new char[figletFont.height][];
    for (int c = 0; c < message.length(); c++){
      result = Smushing.addChar(figletFont, result, figletFont.getChar(message.charAt(c)));
    }
    return result;
  }

  private static char[][] addChar(FigletFont figletFont, char[][] char1, char[][] char2) {
    char[][] result = new char[figletFont.height][];
    int overlay = calculateOverlay(figletFont, char1, char2);
    for (int l = 0; l < figletFont.height; l++) {
      if(char1[l] == null) {
        char1[l] = new char[0];
      }
      char[] cs1 = char1[l];
      char[] cs2 = char2[l];
      int char1Length = cs1.length;
      int char2Length  = cs2.length;
      int k;
      for(k = 0; k < overlay && char1Length > 0; k++){
        int col = char1Length - overlay + k;
        char c2 = cs2[k];
        char c1 = cs1[col];
        char smushed = figletFont.smushingRulesToApply.smushHorizontal(c1, c2, figletFont.hardblank);
        cs1[col] = smushed;
      }
      char[] lineResult = new char[char1Length + char2Length-k];
      System.arraycopy(char1[l], 0, lineResult, 0, char1Length);
      System.arraycopy(char2[l], k, lineResult, char1Length, char2Length-k);
      result[l] = lineResult;
    }
    return result;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  private static int calculateOverlay(FigletFont figletFont, char[][] char1, char[][] char2){
    if (figletFont.smushingRulesToApply.getHorizontalLayout() == SmushingRule.Layout.FULL_WIDTH){
      return 0;
    }
    int maxPotentialOverlay = figletFont.maxLine;
    for(int l = 0; l < figletFont.height; l++){
      if(char1[l] == null) {
        char1[l] = new char[0];
      }
      char[] c1 = char1[l];
      char[] c2 = char2[l];
      int c1Length = c1.length-1;
      int c2Length = c2.length-1;
      int c1EmptyCount;
      int c2EmptyCount;
      for(c1EmptyCount = 0; c1EmptyCount < c1Length && c1[c1Length - c1EmptyCount] == ' '; c1EmptyCount++){}
      for(c2EmptyCount = 0; c2EmptyCount < c2Length && c2[c2EmptyCount] == ' '; c2EmptyCount++){}
      int overlay = c1EmptyCount + c2EmptyCount;
      if(c1EmptyCount <= c1Length && c2EmptyCount <= c2Length){
        if(figletFont.smushingRulesToApply.getHorizontalLayout() == SmushingRule.Layout.SMUSHING &&
            SmushingRule.HORIZONTAL_SMUSHING.smushes(c1[c1Length - c1EmptyCount], c2[c2EmptyCount], figletFont.hardblank)
            || figletFont.smushingRulesToApply.smushesHorizontal(c1[c1Length - c1EmptyCount], c2[c2EmptyCount], figletFont.hardblank)){
          overlay++;
        }
      }
      if(overlay < maxPotentialOverlay){
        maxPotentialOverlay = overlay;
      }

    }
    return maxPotentialOverlay;
  }
}
