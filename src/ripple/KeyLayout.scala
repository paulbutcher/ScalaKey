package com.paulbutcher.scalakey

object KeyLayout {
  
  class Key(char: Char, xCentre: Float, yCentre: Float) {
    val character = char
    
    def distanceSquared(x: Float, y: Float) = {
      val xDelta = xCentre - x
      val yDelta = yCentre - y
      xDelta * xDelta + yDelta * yDelta
    }
  }
  
  val keys = Array(
      new Key('q', -1.26f,  0.75f),
      new Key('w', -0.98f,  0.75f),
      new Key('e', -0.70f,  0.75f),
      new Key('r', -0.42f,  0.75f),
      new Key('t', -0.14f,  0.75f),
      new Key('y',  0.14f,  0.75f),
      new Key('u',  0.42f,  0.75f),
      new Key('i',  0.70f,  0.75f),
      new Key('o',  0.98f,  0.75f),
      new Key('p',  1.26f,  0.75f),

      new Key('a', -1.12f,  0.25f),
      new Key('s', -0.84f,  0.25f),
      new Key('d', -0.56f,  0.25f),
      new Key('f', -0.28f,  0.25f),
      new Key('g',  0.00f,  0.25f),
      new Key('h',  0.28f,  0.25f),
      new Key('j',  0.56f,  0.25f),
      new Key('k',  0.84f,  0.25f),
      new Key('l',  1.12f,  0.25f),

      new Key('z', -0.88f, -0.25f),
      new Key('x', -0.60f, -0.25f),
      new Key('c', -0.32f, -0.25f),
      new Key('v', -0.04f, -0.25f),
      new Key('b',  0.24f, -0.25f),
      new Key('n',  0.52f, -0.25f),
      new Key('m',  0.80f, -0.25f),
      
      new Key(' ',  0.00f, -0.75f)
    )
    
  def closest(x: Float, y: Float) = {
    var character: Char = '_'
    var closest = Float.MaxValue
    
    for (key <- keys) {
      val distance = key.distanceSquared(x, y)
      if (distance < closest) {
        closest = distance
        character = key.character
      }
    }
    character
  }
}
