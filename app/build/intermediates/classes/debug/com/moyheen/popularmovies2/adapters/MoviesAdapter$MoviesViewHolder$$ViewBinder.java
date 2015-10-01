// Generated code from Butter Knife. Do not modify!
package com.moyheen.popularmovies2.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MoviesAdapter$MoviesViewHolder$$ViewBinder<T extends com.moyheen.popularmovies2.adapters.MoviesAdapter.MoviesViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558494, "field 'imageView'");
    target.imageView = finder.castView(view, 2131558494, "field 'imageView'");
  }

  @Override public void unbind(T target) {
    target.imageView = null;
  }
}
