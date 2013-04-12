import java.awt.Color;

LineCollection lines;
BackgroundImage bg;
Camera cam;
ContentController content;
int textSize;
boolean takingPicture = false;

void setup() {
  size(1100, 728);
  frame.setBackground(new Color(0));
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
void draw() { 
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
void mouseMoved() { 
  lines.mouseMoved(mouseX, mouseY); 
  redraw();
}
void mousePressed() { 
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
void mouseDragged() { 
  lines.mouseDragged(mouseX, mouseY); 
  redraw();
}
void mouseReleased() { 
  content.stopDragging();
  lines.mouseReleased(mouseX, mouseY); 
  redraw();
}

void keyPressed(){
 if(key == 27) key = 0;
}

