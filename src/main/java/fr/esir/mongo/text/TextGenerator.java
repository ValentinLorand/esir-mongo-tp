package fr.esir.mongo.text;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author lboutros
 */
@Component
public class TextGenerator {

  @Value("classpath:common-words.txt")
  private Resource wordsResource;

  private String[] words;

  @PostConstruct
  private void initWords() throws IOException {
    List<String> heroNameList = IOUtils.readLines(wordsResource.getInputStream(), StandardCharsets.UTF_8.name());
    words = heroNameList.toArray(new String[heroNameList.size()]);
  }

  public String generateText(int sentenceCount) {
    StringBuilder builder = new StringBuilder();
    Random rand = new Random();


    for (int i = 0; i < sentenceCount; i++) {
      int n = rand.nextInt(words.length);
      builder.append(words[n]);
      if(i !=  sentenceCount -1)
        builder.append(" ");
    }
    return builder.toString();
  }


  public static String generateTag() {
    String[] listTags = new String[]{"Economics", "Politic", "Computer science", "Scientific", "Humanity", "Journalism", "Humour", "History", "Geographic", "Chime", "Physique"};
    Random r = new Random();
    return listTags[r.nextInt(listTags.length)];
  }

  public static String[] generateTags(int n) {
    Set<String> tags = new HashSet<String>();
    while(tags.size() < n) tags.add(generateTag());
    return tags.toArray(new String[tags.size()]);
  }

}
