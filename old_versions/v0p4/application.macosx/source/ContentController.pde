class ContentController {
  int warningPadding = 50;
  int textPadding = 30;
  int buttonHeight = 30;
  int buttonSeperator = 0;
  int barPadding = 30;
  int barHeight = 7;
  int barWidth = 300;
  int sliderHeight = 25;
  int sliderWidth = 40;
  float sliderX;
  float sliderOX; //slider x offset
  int sliderOutput; // constatnly outputs slider value from 0 - 255

  PImage reset;
  PImage threshImg;
  PImage glitch;
  PImage strokeImg;
  PImage pixelSwitch;
  PImage up;
  PImage down;
  int textSize;
  int resetPadding;
  int resetWidth;
  int resetHeight;
  int thresholdPaddingTop;
  boolean dragging = false;
  boolean isWarning = false;
  boolean warningShown = false;
  
  Timer timer;

  ContentController(int _textSize, int cameraPadding, int cameraWidth) {
    textSize = _textSize;
    resetPadding = cameraPadding;
    resetWidth = cameraWidth;
    resetHeight = resetWidth-resetWidth/5;
    reset = loadImage("assets/reset.jpg");
    threshImg = loadImage("assets/threshold.jpg");
    glitch = loadImage("assets/glitch.jpg");
    strokeImg = loadImage("assets/stroke.jpg");
    pixelSwitch = loadImage("assets/pixelSwitch.jpg");
    up = loadImage("assets/up.jpg");
    down = loadImage("assets/down.jpg");
    timer = new Timer(1500);
    thresholdPaddingTop = height-buttonHeight-resetPadding-resetHeight;
    sliderX = barPadding + 3 + 127; //start slider in middle
  }

  void display(float mx, float my) {
    //if over a button
    if (isOver(mx, my, "new image") ||
      isOver(mx, my, "new words") ||
      cam.isOver(mx, my) ||
      isOver(mx, my, "threshold") ||
      isOver(mx, my, "stroke") ||
      isOver(mx, my, "pixel switch") ||
      isOver(mx, my, "reset words")) cursor(HAND);
    else if(isOver(mx, my, "up") && bg.s != bg.maxS) cursor(HAND);
    else if(isOver(mx, my, "down") && bg.s != bg.minS) cursor(HAND);
    else cursor(ARROW);
    stroke(255);
    newImageButton();
    newWordsButton();
    imgButtons();
    if (bg.isThreshold ||
      bg.isStroke) {
      thresholdBar();
      thresholdSlider();
    }
    stroke(0);
  }
  
  void warning(){
    fill(0, 200);
    rect(width/4, height/3, width-width/4*2, height-height/3*2); 
    fill(255);
    textSize(48);
    textAlign(CENTER);
    text("Warning", width/2, height/3.5+warningPadding*2);
    textAlign(LEFT);
    textSize(32);
    text("Decreasing pixel size can slow performance", width/4+warningPadding, height/2.5+warningPadding, width/2-warningPadding*2, height);
  }
  
  //tests if warning has already been shown
  void showWarning(){
    if (!warningShown){
      isWarning = true;
    }
    else isWarning = false;
  }
  
  void newImageButton() {
    String newImage = "new image";
    fill(0);
    rect(0, height-buttonHeight, width/2-buttonSeperator, height);
    fill(255);
    text(newImage, textPadding, height-buttonHeight+textSize);
  }

  void newWordsButton() {
    String newImage = "new words";
    fill(0);
    rect(width/2+buttonSeperator, height-buttonHeight, width, height);
    fill(255);
    text(newImage, width/2+buttonSeperator+textPadding, height-buttonHeight+textSize);
  }

  void imgButtons() {
    image(reset, width-resetWidth-resetPadding, resetPadding, resetWidth, resetHeight);
    image(threshImg, width-resetWidth-resetPadding, thresholdPaddingTop, resetWidth, resetHeight);
    image(glitch, width-resetWidth*2-resetPadding*2, thresholdPaddingTop, resetWidth, resetHeight);
    image(strokeImg, width-resetWidth*3-resetPadding*3, thresholdPaddingTop, resetWidth, resetHeight);
    image(pixelSwitch, width-resetWidth*4-resetPadding*4, thresholdPaddingTop, resetWidth, resetHeight);
    if(bg.s != bg.maxS) image(up, width-resetWidth-resetPadding, thresholdPaddingTop-resetHeight-resetPadding, resetWidth, resetHeight/2);
    if(bg.s != bg.minS) image(down, width-resetWidth-resetPadding, thresholdPaddingTop-resetPadding/1.5-resetHeight/2, resetWidth, resetHeight/2);
  }


  void thresholdBar() {
    fill(0);
    stroke(255);
    rect(barPadding, height-buttonHeight-barPadding, barWidth, barHeight);
  }

  void thresholdSlider() {
    fill(0);
    stroke(255);
    if (dragging) {
      sliderX = mouseX - sliderOX;
      bg.needsRedraw = true;
    }
    sliderX = constrain(sliderX, barPadding, barPadding+barWidth-sliderWidth);
    rect(sliderX, height-barPadding-buttonHeight-sliderHeight/2+1, sliderWidth, sliderHeight);
    sliderOutput = int(map(sliderX, barPadding, barPadding+barWidth-sliderWidth, 0, 255));
  }

  void clicked() {
    if (isOver(mouseX, mouseY, "slider")) dragging = true;
    sliderOX = mouseX - sliderX;
  }

  void stopDragging() {
    dragging = false;
  }

  boolean isOver(float mx, float my, String objective) {
    // conditional for new image icon
    if (objective == "new image") {
      if (mx >= 0 &&
        mx <= width/2-buttonSeperator &&
        my >= height-buttonHeight &&
        my <= height) {
        return true;
      }
      else return false;
    }
    //conditional for new words icon
    else if (objective == "new words") {
      if (mx >= width/2+buttonSeperator &&
        mx <= width &&
        my >= height-buttonHeight &&
        my <= height) {
        return true;
      }
      else return false;
    }
    else if (objective == "reset words") {
      if (mx >= width-resetWidth-resetPadding &&
        mx <= width-resetPadding &&
        my >= resetPadding &&
        my <= resetPadding + resetHeight) {
        return true;
      }
      else return false;
    }
    else if (objective == "up") {
      if (mx >= width-resetWidth-resetPadding &&
        mx <= width-resetPadding &&
        my >= thresholdPaddingTop-resetHeight-resetPadding &&
        my <= thresholdPaddingTop-resetPadding-resetHeight/2) {
        return true;
      }
      else return false;
    }
    else if (objective == "down") {
      if (mx >= width-resetWidth-resetPadding &&
        mx <= width-resetPadding &&
        my >= thresholdPaddingTop-resetPadding/1.5-resetHeight/2 &&
        my <= thresholdPaddingTop-resetPadding/1.5) {
        return true;
      }
      else return false;
    }
    else if (objective == "threshold") {
      if (mx >= width-resetWidth-resetPadding &&
        mx <= width-resetPadding &&
        my >= thresholdPaddingTop &&
        my <= thresholdPaddingTop+resetHeight) {
        return true;
      }
      else return false;
    }
    else if (objective == "glitch") {
      if (mx >= width-resetWidth*2-resetPadding*2 &&
        mx <= width-resetPadding*2-resetWidth &&
        my >= thresholdPaddingTop &&
        my <= thresholdPaddingTop+resetHeight) {
        return true;
      }
      else return false;
    }
    else if (objective == "stroke") {
      if (mx >= width-resetWidth*3-resetPadding*3 &&
        mx <= width-resetPadding*3-resetWidth &&
        my >= thresholdPaddingTop &&
        my <= thresholdPaddingTop+resetHeight) {
        return true;
      }
      else return false;
    }
    else if (objective == "pixel switch") {
      if (mx >= width-resetWidth*4-resetPadding*4 &&
        mx <= width-resetPadding*4-resetWidth &&
        my >= thresholdPaddingTop &&
        my <= thresholdPaddingTop+resetHeight) {
        return true;
      }
      else return false;
    }
    else if (objective == "slider") {
      if (mx >= sliderX &&
        mx <= sliderX+sliderWidth &&
        my >= height-barPadding-buttonHeight-sliderHeight/2+1 &&
        my <= height-barPadding-buttonHeight-sliderHeight/2+1+sliderHeight) {
        return true;
      }
      else return false;
    }
    else return false;
  }
}

