package publications;

import java.util.*;

public class ArticleRepository {
  private final Summariser summariser;
  private final Map<Subscriber, HashSet<String>> subscribers;

  public ArticleRepository(Summariser summariser) {
    this.summariser = summariser;
    this.subscribers = new HashMap<>();
  }

  public void publish(Article article) {
    List<String> keywords = summariser.summarise(article);
    for (Subscriber s: subscribers.keySet()) {
      Set<String> subscriberWords = subscribers.get(s);
      for (String word : keywords) {
        if (subscriberWords.contains(word)) {
          s.alert();
          break;
        }
      }
    }
  }

  public void subscribeToTopic(Subscriber subscriber, String keyword) {
    if (subscribers.containsKey(subscriber)) {
      subscribers.get(subscriber).add(keyword);
    } else {
      subscribers.put(subscriber, new HashSet<>(Set.of(keyword)));
    }
  }

  public void unsubscribe(Subscriber subscriber, String keyword) {
    subscribers.get(subscriber).remove(keyword);
  }
}


