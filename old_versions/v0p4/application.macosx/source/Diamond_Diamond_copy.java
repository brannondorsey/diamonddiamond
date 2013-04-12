import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Diamond_Diamond_copy extends PApplet {

LineCollection lines;
BackgroundImage bg;
Camera cam;
ContentController content;
int textSize;
boolean takingPicture = false;

public void setup() {
  size(1100, 728);
  frameRate(50);
  noStroke();
  smooth();
  frame.setTitle("Diamond Diamond");
  textSize = 24; 
  textFont(createFont("courier new", textSize));
  bg = new BackgroundImage();
  cam = new Camera();
  content = new ContentController(textSize, cam.cameraPadding, cam.cameraWidth);
  lines = new LineCollection(textSize, content.buttonHeight);
  noLoop();
}

// fall through drawing
public void draw() { 
  background(255);
  bg.display();
  lines.draw();
  if (takingPicture) {
    cam.captureImage();
    cam.opacity = 255;
  }
  if (cam.opacity > 0) cam.flash();
  cam.display(mouseX, mouseY);
  content.display(mouseX, mouseY);
  if (content.isWarning) { 
    content.warning();
    if (content.timer.isFinished()) {
      content.warningShown = true;
      content.isWarning = false;
    }
  }
  takingPicture = false;
}

// fall through event handling
public void mouseMoved() { 
  lines.mouseMoved(mouseX, mouseY); 
  redraw();
}
public void mousePressed() { 
  lines.mousePressed(mouseX, mouseY); 
  //if (cam.isOver(mouseX, mouseY)) cam.captureImage(mouseX, mouseY);
  if (cam.isOver(mouseX, mouseY)) {
    takingPicture = true;
  }
  else if (content.isOver(mouseX, mouseY, "new image")) {
    bg.update();
    bg.needsRedraw = true;
  }
  else if (content.isOver(mouseX, mouseY, "new words")) {
    lines.newWords = true;
    lines.newLines();
  }
  else if (content.isOver(mouseX, mouseY, "reset words")) {
    lines.newLines();
  }
  else if (content.isOver(mouseX, mouseY, "threshold")) {
    //switch threshold state
    if (bg.isThreshold == false) bg.isThreshold = true;
    else bg.isThreshold = false;
    bg.needsRedraw = true;
  }
  else if (content.isOver(mouseX, mouseY, "glitch")) {
    //switch threshold state
    if (bg.isGlitched == false) bg.isGlitched = true;
    else bg.isGlitched = false;
    bg.needsRedraw = true;
  }
  else if (content.isOver(mouseX, mouseY, "stroke")) {
    //switch stroke state
    if (bg.isStroke == false) bg.isStroke = true;
    else bg.isStroke = false;
    bg.needsRedraw = true;
  }
  else if (content.isOver(mouseX, mouseY, "pixel switch")) {
    //switch pixel state
    if (bg.isSquare == false) bg.isSquare = true;
    else bg.isSquare = false;
    bg.needsRedraw = true;
  }
  else if (content.isOver(mouseX, mouseY, "up")) {
    bg.sizeIncrease();
    bg.needsRedraw = true;
  }
  else if (content.isOver(mouseX, mouseY, "down")) {
    bg.sizeDecrease();
    bg.needsRedraw = true;
  }
  content.clicked(); 
  redraw();
}
public void mouseDragged() { 
  lines.mouseDragged(mouseX, mouseY); 
  redraw();
}
public void mouseReleased() { 
  content.stopDragging();
  lines.mouseReleased(mouseX, mouseY); 
  redraw();
}

class BackgroundImage {
  String[] imageList;
  int imageIndex = 0;
  int s = 8;
  int minS = 4;
  int maxS = 30;
  int threshold;
  PImage backgroundImage;
  PGraphics pg;
  PImage[] images;
  boolean isThreshold = false;
  boolean isGlitched = false;
  boolean isStroke = false;
  boolean isSquare = false;
  boolean needsRedraw = true;

  BackgroundImage() {
    //get image names
    imageList = loadStrings("image_list.txt");
    images = new PImage[imageList.length];
    //load all images
    for (int i = 0; i < imageList.length; i++) {
      images[i] = loadImage("images/"+imageList[i]);
    }
    update();
    pg = createGraphics(width, height);
  }

  public void update() {
    imageIndex = PApplet.parseInt(random(imageList.length));
    backgroundImage = images[imageIndex];
  }
  
  public void display(){
   if(needsRedraw) makeGraphic();
   image(pg, 0, 0, width+s, height+s); 
  }
  
  public void makeGraphic() {
    int imageWidth;
    int imageHeight;
    if (isGlitched) {
      imageWidth = width;
      imageHeight = height;
    }
    else {
      imageWidth = backgroundImage.width;
      imageHeight = backgroundImage.height;
    }
    threshold = content.sliderOutput;
    
    backgroundImage.loadPixels();
    pg.beginDraw();
    pg.background(255);
    pg.smooth();
    for (int x = 0; x < imageWidth; x += s) {
      for (int y = 0; y < imageHeight; y += s) {
        int loc = x + y*imageWidth;
        //draw an rect at the location with that color
        if (isThreshold) {
          if (brightness(backgroundImage.pixels[loc]) > threshold) pg.fill(0);
          else pg.fill(255);
        }
        else pg.fill(brightness(backgroundImage.pixels[loc]));
        if (!bg.isStroke) { 
          pg.noStroke();
        }
        else {
          if (!bg.isThreshold) pg.stroke(content.sliderOutput);
          else pg.stroke(0);
        }

        s = constrain(s, minS, maxS);
        if (isSquare) pg.rect(x, y, s, s);
        else pg.ellipse(x, y, s, s);
      }
    }
    pg.endDraw();
    needsRedraw = false;
  }
  
  
  public void sizeIncrease() {
    s++;
  }
  public void sizeDecrease() {
     s--;
    if(s == minS+1 && !content.isWarning && !content.warningShown){
      content.timer.start();
      content.showWarning();
    }
  }
}




class Camera {
  PImage cameraImage;
  String path;
  int cameraPadding = 10;
  int cameraWidth = 60;
  int opacity = 0;
  int flashDecrementer = 50; //higher number means quicker flash
  int pictureName;

  Camera() {
    path = savePath("storage");
    println(path);
    String[] pictureIndex = loadStrings(path+"/pictureNumber.txt");
    pictureName = PApplet.parseInt(pictureIndex[0]);
    cameraImage = loadImage("assets/camera.jpg");
    String _pictureName = ""+PApplet.parseChar(pictureName); 
      println(pictureName); 
  }

  public void display(float mx, float my) {
    image(cameraImage, cameraPadding, cameraPadding, cameraWidth, cameraWidth-cameraWidth/5);
  }

  public boolean isOver(float mx, float my) {
    if (mx >= cameraPadding &&
      mx <= cameraPadding+cameraWidth &&
      my >= cameraPadding &&
      my <= cameraPadding+cameraWidth-cameraWidth/5) {
      return true;
    }
    else {
      return false;
    }
  }

  public void captureImage() {
      save("pictures/"+lines.picturePrefix+"_"+pictureName+".jpg");
      pictureName++;
      String _null = "";
     // String _tempPictureName = _null.valueOf(pictureName);
      String[] _pictureName = {_null.valueOf(pictureName)}; 
      saveStrings(path+"/pictureNumber.txt", _pictureName);
      println(_pictureName);
  }
  
  public void flash(){
    fill(255, opacity);
    rect(0,0,width,height);
    opacity -= flashDecrementer;
    if(opacity <= 0) opacity = 0;
  }
}

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

  public void display(float mx, float my) {
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
  
  public void warning(){
    fill(0, 200);
    rect(width/4, height/3, width-width/4*2, height-height/3*2); 
    fill(255);
    textSize(48);
    textAlign(CENTER);
    text("Warning", width/2, height/3.5f+warningPadding*2);
    textAlign(LEFT);
    textSize(32);
    text("Decreasing pixel size can slow performance", width/4+warningPadding, height/2.5f+warningPadding, width/2-warningPadding*2, height);
  }
  
  //tests if warning has already been shown
  public void showWarning(){
    if (!warningShown){
      isWarning = true;
    }
    else isWarning = false;
  }
  
  public void newImageButton() {
    String newImage = "new image";
    fill(0);
    rect(0, height-buttonHeight, width/2-buttonSeperator, height);
    fill(255);
    text(newImage, textPadding, height-buttonHeight+textSize);
  }

  public void newWordsButton() {
    String newImage = "new words";
    fill(0);
    rect(width/2+buttonSeperator, height-buttonHeight, width, height);
    fill(255);
    text(newImage, width/2+buttonSeperator+textPadding, height-buttonHeight+textSize);
  }

  public void imgButtons() {
    image(reset, width-resetWidth-resetPadding, resetPadding, resetWidth, resetHeight);
    image(threshImg, width-resetWidth-resetPadding, thresholdPaddingTop, resetWidth, resetHeight);
    image(glitch, width-resetWidth*2-resetPadding*2, thresholdPaddingTop, resetWidth, resetHeight);
    image(strokeImg, width-resetWidth*3-resetPadding*3, thresholdPaddingTop, resetWidth, resetHeight);
    image(pixelSwitch, width-resetWidth*4-resetPadding*4, thresholdPaddingTop, resetWidth, resetHeight);
    if(bg.s != bg.maxS) image(up, width-resetWidth-resetPadding, thresholdPaddingTop-resetHeight-resetPadding, resetWidth, resetHeight/2);
    if(bg.s != bg.minS) image(down, width-resetWidth-resetPadding, thresholdPaddingTop-resetPadding/1.5f-resetHeight/2, resetWidth, resetHeight/2);
  }


  public void thresholdBar() {
    fill(0);
    stroke(255);
    rect(barPadding, height-buttonHeight-barPadding, barWidth, barHeight);
  }

  public void thresholdSlider() {
    fill(0);
    stroke(255);
    if (dragging) {
      sliderX = mouseX - sliderOX;
      bg.needsRedraw = true;
    }
    sliderX = constrain(sliderX, barPadding, barPadding+barWidth-sliderWidth);
    rect(sliderX, height-barPadding-buttonHeight-sliderHeight/2+1, sliderWidth, sliderHeight);
    sliderOutput = PApplet.parseInt(map(sliderX, barPadding, barPadding+barWidth-sliderWidth, 0, 255));
  }

  public void clicked() {
    if (isOver(mouseX, mouseY, "slider")) dragging = true;
    sliderOX = mouseX - sliderX;
  }

  public void stopDragging() {
    dragging = false;
  }

  public boolean isOver(float mx, float my, String objective) {
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
        my >= thresholdPaddingTop-resetPadding/1.5f-resetHeight/2 &&
        my <= thresholdPaddingTop-resetPadding/1.5f) {
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

/**
 * Individual lines
 */
class Line {
  String s;
  float x, y, w, h;
  boolean active;
  int cx, cy, ox=0, oy=0;
  int textPadding = 10;
  int fillColor = 0;
  

  public Line(String _s, float _x, int _y, int _textSize) {
    s = _s;
    x = _x;
    y = _y;
    w = textWidth(s);
    h = textSize;
  }

  public void draw() {
    fill(255);
    stroke(0);
    textSize(textSize);
    rect(ox+x-textPadding*1.5f, oy+y+h-textPadding*3, textWidth(s)+textPadding*3, 32+textPadding);
    fill(fillColor);
    text(s,ox+x,oy+y+h);
  }

  public boolean over(int mx, int my) {
    return (x-textPadding*1.5f <= mx && mx <= x+w+textPadding*1.5f && y-textPadding <= my && my <= y+h+textPadding*1.5f);
  }

  // Mouse moved: is the cursor over this line?
  // if so, change the fill color
  public void mouseMoved(int mx, int my) {
    active = over(mx,my);
    fillColor = (active ? color(100) : 0);
  }

  // Mouse pressed: are we active? then
  // mark where we started clicking, so 
  // we can do offset computation on
  // mouse dragging.
  public void mousePressed(int mx, int my) {
    if(active) {
      cx = mx;
      cy = my;
      ox = 0;
      oy = 0; 
    }
  }

  // Mouse click-dragged: if we're active,
  // change the draw offset, based on the
  // distance between where we initially
  // clicked, and where the mouse is now.
  public void mouseDragged(int mx, int my) {
    if(active) {
      ox = mx-cx;
      oy = my-cy;
    }
  }

  // Mouse released: if we're active,
  // commit the offset to this line's
  // position. Also, regardless of
  // whether we're active, now we're not.  
  public void mouseReleased(int mx, int my) {
    if(active) {
      x += mx-cx;
      y += my-cy;
      ox = 0;
      oy = 0;
    }
    active = false;
  }
}
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
  
  public void newLines(){
    
    if(newWords) currentStrings = updateStrings(); //if requesting "new words" update strings 
    lines = new Line[currentStrings.length];
    int x, y;
    for (int i=0, last=currentStrings.length; i<last; i++) {
      x = PApplet.parseInt(random(cam.cameraWidth+cam.cameraPadding+rectPadding, width-textWidth(currentStrings[i])-content.resetWidth-content.resetPadding-rectPadding));
      y = PApplet.parseInt(random(0, content.thresholdPaddingTop-textSize-rectPadding));
      lines[i] = new Line(currentStrings[i], x, y,textSize);
    }
    newWords = false; //reset to default "not looking for new words" state
  }
  
  public String[] updateStrings() {
    numberOfWords = PApplet.parseInt(random(minWords, maxWords+1));
    String[] currentStrings = new String[numberOfWords];
    int index;
    for (int i = 0; i < currentStrings.length; i++) {
      index = PApplet.parseInt(random(strings.length));
      currentStrings[i] = strings[index];
    }
    return currentStrings;
  }

  public String[] getStrings() {
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
  public void draw() { 
    for (Line l: lines) { 
      l.draw();
    }
  }
  // fall through event handling
  public void mouseMoved(int mx, int my) { 
    for (Line l: lines) { 
      l.mouseMoved(mx, my);
    }
  } 
  public void mousePressed(int mx, int my) { 
    for (Line l: lines) { 
      l.mousePressed(mx, my);
    }
  } 
  public void mouseDragged(int mx, int my) { 
    for (Line l: lines) { 
      l.mouseDragged(mx, my);
    }
  }
  public void mouseReleased(int mx, int my) { 
    for (Line l: lines) { 
      l.mouseReleased(mx, my);
    }
  }
}

class Timer {
 
  int savedTime; // When Timer started
  int totalTime; // How long Timer should last
  
  Timer(int tempTotalTime) {
    totalTime = tempTotalTime;
  }
  
  // Starting the timer
  public void start() {
    // When the timer starts it stores the current time in milliseconds.
    savedTime = millis(); 
  }
  
  // The function isFinished() returns true if 5,000 ms have passed. 
  // The work of the timer is farmed out to this method.
  public boolean isFinished() { 
    // Check how much time has passed
    int passedTime = millis()- savedTime;
    if (passedTime > totalTime) {
      return true;
    } else {
      return false;
    }
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Diamond_Diamond_copy" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
