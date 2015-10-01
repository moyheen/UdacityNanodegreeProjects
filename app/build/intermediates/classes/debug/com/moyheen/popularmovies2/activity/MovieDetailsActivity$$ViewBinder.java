// Generated code from Butter Knife. Do not modify!
package com.moyheen.popularmovies2.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MovieDetailsActivity$$ViewBinder<T extends com.moyheen.popularmovies2.activity.MovieDetailsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558478, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131558478, "field 'mToolbar'");
  }

  @Override public void unbind(T target) {
    target.mToolbar = null;
  }
}
