package publications;

import org.checkerframework.checker.units.qual.A;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Flow;

public class ArticleRepositoryTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  private final Summariser summariser = context.mock(Summariser.class);
  private final Subscriber alice = context.mock(Subscriber.class, "alice");
  private final Subscriber bob = context.mock(Subscriber.class, "bob");
  private final Article article = new Article("testing supremacy");
  private final Article article2 = new Article("testing supremacy 2");

  private final ArticleRepository repo = new ArticleRepository(summariser);

  @Test
  public void onPublicationArticlesAreSummarisedToExtractKeywords() {
    context.checking(new Expectations() {{
      exactly(1).of(summariser).summarise(article);
      will(returnValue(List.of("testing", "Java")));
    }});
    repo.publish(article);
  }

  @Test
  public void subscribersWithMatchingInterestsAreNotifiedOnPublicationOfANewArticle() {
    context.checking(new Expectations() {{
      ignoring(summariser).summarise(article);
      will(returnValue(List.of("testing", "Java")));
      exactly(1).of(alice).alert();
      exactly(1).of(bob).alert();
    }});
    repo.subscribeToTopic(alice, "Java");
    repo.subscribeToTopic(bob, "testing");
    repo.publish(article);
  }

  @Test
  public void subscribersWithOutMatchingInterestsAreNotNotifiedOnPublicationOfANewArticle() {
    context.checking(new Expectations() {{
      ignoring(summariser).summarise(article);
      will(returnValue(List.of("testing", "Python")));
      exactly(0).of(alice).alert();
      exactly(1).of(bob).alert();
    }});
    repo.subscribeToTopic(alice, "Java");
    repo.subscribeToTopic(bob, "testing");
    repo.publish(article);
  }


  @Test
  public void subscribersWithMultipleMatchingInterestsAreOnlyNotifiedOncePerArticle() {
    context.checking(new Expectations() {{
      ignoring(summariser).summarise(article);
      will(returnValue(List.of("testing", "Java")));
      exactly(1).of(alice).alert();
      exactly(1).of(bob).alert();
    }});
    repo.subscribeToTopic(alice, "Java");
    repo.subscribeToTopic(alice, "testing");
    repo.subscribeToTopic(bob, "testing");
    repo.publish(article);
  }

  @Test
  public void removingAnInterestNoLongerTriggersAnAlertForThatWord() {
    context.checking(new Expectations() {{
      ignoring(summariser).summarise(article);
      will(returnValue(List.of("testing", "Java")));
      exactly(1).of(alice).alert();
      exactly(1).of(summariser).summarise(article2);
      will(returnValue(List.of("testing", "Python")));
      exactly(0).of(alice).alert();
    }});
    repo.subscribeToTopic(alice, "testing");
    repo.publish(article);
    repo.unsubscribe(alice, "testing");
    repo.publish(article2);
  }
}
