// Generated code from Butter Knife. Do not modify!
package com.moyheen.popularmovies2.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.moyheen.popularmovies2.activity.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558478, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131558478, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131558479, "field 'mSpinner'");
    target.mSpinner = finder.castView(view, 2131558479, "field 'mSpinner'");
  }

  @Override public void unbind(T target) {
    target.mToolBar = null;
    target.mSpinner = null;
  }
}
