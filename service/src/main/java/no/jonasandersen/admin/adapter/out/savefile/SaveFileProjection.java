package no.jonasandersen.admin.adapter.out.savefile;

import no.jonasandersen.admin.domain.SaveFilesForOwner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class SaveFileProjection {

  private final MongoTemplate mongoTemplate;

  public SaveFileProjection(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public SaveFilesForOwner saveFilesForOwner(String owner) {
    Query query = new Query(Criteria.where("owner").is(owner));

    return mongoTemplate.findOne(query, SaveFilesForOwner.class, "saveFilesByOwner");
  }
}
