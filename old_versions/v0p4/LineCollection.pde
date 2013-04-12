/**
 * A collection of lines. This is *only* a collecton,
 * it is simply responsible for passing along events.
 */
class LineCollection {
  Line[] lines;
  String[] strings;
  String[] currentStrings;
  String picturePrefix;
  int textSize;
  boolean newWords = true;

  int minWords = 5;
  int maxWords = 10;
  int rectPadding = 15; //accounts for estimated padding between text and its assosciated rectangle
  int numberOfWords;
  int buttonSize;

  // construct
  LineCollection(int _textSize, int _buttonSize) {
    textSize = _textSize;
    buttonSize = _buttonSize;
    strings = getStrings();
    newLines();
  }
  
  void newLines(){
    
    if(newWords) currentStrings = updateStrings(); //if requesting "new words" update strings 
    lines = new Line[currentStrings.length];
    int x, y;
    for (int i=0, last=currentStrings.length; i<last; i++) {
      x = int(random(cam.cameraWidth+cam.cameraPadding+rectPadding, width-textWidth(currentStrings[i])-content.resetWidth-content.resetPadding-rectPadding));
      y = int(random(0, content.thresholdPaddingTop-textSize-rectPadding));
      lines[i] = new Line(currentStrings[i], x, y,textSize);
    }
    newWords = false; //reset to default "not looking for new words" state
  }
  
  String[] updateStrings() {
    numberOfWords = int(random(minWords, maxWords+1));
    String[] currentStrings = new String[numberOfWords];
    int index;
    for (int i = 0; i < currentStrings.length; i++) {
      index = int(random(strings.length));
      currentStrings[i] = strings[index];
    }
    return currentStrings;
  }

  String[] getStrings() {
    String[] controlText = loadStrings("edit_this.txt");
    int textStart = controlText[0].indexOf(":");
    int pictureStart = controlText[1].indexOf(":");
    picturePrefix = controlText[1].substring(pictureStart+2);
    String textFileName = controlText[0].substring(textStart+2);
    String[] rawText = loadStrings("writing/"+textFileName);
    String joinedText = join(rawText, " ");
    String lowerText = joinedText.toLowerCase();
    String[] strings = splitTokens(lowerText, " .,()-\"/");
    return strings;
  }
  // fall through drawing   
  void draw() { 
    for (Line l: lines) { 
      l.draw();
    }
  }
  // fall through event handling
  void mouseMoved(int mx, int my) { 
    for (Line l: lines) { 
      l.mouseMoved(mx, my);
    }
  } 
  void mousePressed(int mx, int my) { 
    for (Line l: lines) { 
      l.mousePressed(mx, my);
    }
  } 
  void mouseDragged(int mx, int my) { 
    for (Line l: lines) { 
      l.mouseDragged(mx, my);
    }
  }
  void mouseReleased(int mx, int my) { 
    for (Line l: lines) { 
      l.mouseReleased(mx, my);
    }
  }
}

