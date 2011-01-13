package com.paulbutcher.scalakey;

import android.app.Activity;
import android.os.Bundle;

class FooBar extends Activity
{
    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
    }
}
