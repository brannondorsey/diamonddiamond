/**
 * Individual lines
 */
class Line {
  String s;
  float x, y, w, h;
  boolean active;
  int cx, cy, ox=0, oy=0;
  int textPadding = 10;
  color fillColor = 0;
  

  public Line(String _s, float _x, int _y, int _textSize) {
    s = _s;
    x = _x;
    y = _y;
    w = textWidth(s);
    h = textSize;
  }

  void draw() {
    fill(255);
    stroke(0);
    textSize(textSize);
    rect(ox+x-textPadding*1.5, oy+y+h-textPadding*3, textWidth(s)+textPadding*3, 32+textPadding);
    fill(fillColor);
    text(s,ox+x,oy+y+h);
  }

  boolean over(int mx, int my) {
    return (x-textPadding*1.5 <= mx && mx <= x+w+textPadding*1.5 && y-textPadding <= my && my <= y+h+textPadding*1.5);
  }

  // Mouse moved: is the cursor over this line?
  // if so, change the fill color
  void mouseMoved(int mx, int my) {
    active = over(mx,my);
    fillColor = (active ? color(100) : 0);
  }

  // Mouse pressed: are we active? then
  // mark where we started clicking, so 
  // we can do offset computation on
  // mouse dragging.
  void mousePressed(int mx, int my) {
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
  void mouseDragged(int mx, int my) {
    if(active) {
      ox = mx-cx;
      oy = my-cy;
    }
  }

  // Mouse released: if we're active,
  // commit the offset to this line's
  // position. Also, regardless of
  // whether we're active, now we're not.  
  void mouseReleased(int mx, int my) {
    if(active) {
      x += mx-cx;
      y += my-cy;
      ox = 0;
      oy = 0;
    }
    active = false;
  }
}
