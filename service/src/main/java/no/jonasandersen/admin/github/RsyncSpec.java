package no.jonasandersen.admin.github;

public class RsyncSpec {
    private String source;
    private String destination;
    private RsyncOptions options;

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public RsyncOptions getOptions() {
    return options;
  }

  public void setOptions(RsyncOptions options) {
    this.options = options;
  }

  private class RsyncOptions {

  }

  // getters and setters
}
