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
    pictureName = int(pictureIndex[0]);
    cameraImage = loadImage("assets/camera.jpg");
    String _pictureName = ""+char(pictureName); 
      println(pictureName); 
  }

  void display(float mx, float my) {
    image(cameraImage, cameraPadding, cameraPadding, cameraWidth, cameraWidth-cameraWidth/5);
  }

  boolean isOver(float mx, float my) {
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

  void captureImage() {
      save("pictures/"+lines.picturePrefix+"_"+pictureName+".jpg");
      pictureName++;
      String _null = "";
     // String _tempPictureName = _null.valueOf(pictureName);
      String[] _pictureName = {_null.valueOf(pictureName)}; 
      saveStrings(path+"/pictureNumber.txt", _pictureName);
      println(_pictureName);
  }
  
  void flash(){
    fill(255, opacity);
    rect(0,0,width,height);
    opacity -= flashDecrementer;
    if(opacity <= 0) opacity = 0;
  }
}

