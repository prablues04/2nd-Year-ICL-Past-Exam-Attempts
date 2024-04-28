package publications;

import java.util.List;

public interface Summariser {
  List<String> summarise(Article article);
}
