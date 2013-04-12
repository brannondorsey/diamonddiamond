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

  void update() {
    imageIndex = int(random(imageList.length));
    backgroundImage = images[imageIndex];
  }
  
  void display(){
   if(needsRedraw) makeGraphic();
   image(pg, 0, 0, width+s, height+s); 
  }
  
  void makeGraphic() {
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
  
  
  void sizeIncrease() {
    s++;
  }
  void sizeDecrease() {
     s--;
    if(s == minS+1 && !content.isWarning && !content.warningShown){
      content.timer.start();
      content.showWarning();
    }
  }
}




