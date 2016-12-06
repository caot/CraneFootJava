import java.net.URL;

public class ConstantString
{
  private static ConstantString instance;;

  static {
    instance = new ConstantString();
  }

  private ConstantString() {
  }

  public static ConstantString getInstance() {
    return instance;
  }

  public String exportDir()
  {
    URL url = this.getClass().getResource("/../../export/tmp");
    return url.getPath();
  }
}
