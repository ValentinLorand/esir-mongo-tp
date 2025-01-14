package fr.esir.mongo.posts;

import fr.esir.mongo.text.TextGenerator;
import fr.esir.mongo.threads.Thread;
import fr.esir.mongo.threads.ThreadGenerator;
import fr.esir.mongo.users.User;
import fr.esir.mongo.users.UserGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lboutros
 */
@Component
@AllArgsConstructor
@Slf4j
public class PostGenerator implements Processor {

  // TODO initialize/read values in mongo
  // This is a dummy example, you should NEVER do that for a production app
  private final AtomicLong id = new AtomicLong(0);

  private final TextGenerator textGenerator;

  private final ThreadGenerator threadGenerator;
  private final UserGenerator userGenerator;

  @Override
  public void process(Exchange exchange) throws Exception {
    exchange.getIn().setBody(generatePost());
  }

  private Post generatePost() {
    Thread randomThread = threadGenerator.getRandomThread();
    User randomKnownUser = userGenerator.getRandomKnownUser();

    if (randomThread != null && randomKnownUser != null) {
      // we need a thread in order to add a post into
      String idString = Long.toString(id.getAndIncrement());
      Post newPost = Post.builder()
              ._id(idString)
              .title(textGenerator.generateText(10))
              .content(textGenerator.generateText(10000))
              .user_id(randomKnownUser.get_id())
              .thread_id(randomThread.get_id())
              .build();

      return newPost;
    } else {
      if (randomThread == null) {
        log.warn("Cannot create post, no thread created yet.");
      } else {
        log.warn("Cannot create post, no user created yet.");
      }
      return null;
    }
  }
}
