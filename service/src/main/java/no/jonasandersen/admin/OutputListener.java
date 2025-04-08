// Copyright 2022 Titanium I.T. LLC and Ted M. Young. MIT License.

package no.jonasandersen.admin;

import java.util.ArrayList;
import java.util.List;

public class OutputListener<T> {

  private final List<OutputTracker<T>> listeners = new ArrayList<>();

  public void track(T data) {
    listeners.forEach(tracker -> tracker.add(data));
  }

  public OutputTracker<T> createTracker() {
    OutputTracker<T> tracker = new OutputTracker<>(this);
    listeners.add(tracker);
    return tracker;
  }

  void remove(OutputTracker<T> outputTracker) {
    listeners.remove(outputTracker);
  }
}
