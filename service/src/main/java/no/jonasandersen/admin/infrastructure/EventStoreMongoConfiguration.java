package no.jonasandersen.admin.infrastructure;

import org.occurrent.eventstore.mongodb.spring.blocking.EventStoreConfig;
import org.occurrent.eventstore.mongodb.spring.blocking.SpringMongoEventStore;
import org.occurrent.mongodb.timerepresentation.TimeRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class EventStoreMongoConfiguration {

  @Bean
  MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  @Bean
  SpringMongoEventStore springMongoEventStore(
      MongoTransactionManager mongoTransactionManager, MongoTemplate mongoTemplate) {
    EventStoreConfig eventStoreConfig =
        new EventStoreConfig.Builder()
            // The collection where all events will be stored
            .eventStoreCollectionName("events")
            .transactionConfig(mongoTransactionManager)
            // How the CloudEvent "time" property will be serialized in MongoDB! !!Important!!
            .timeRepresentation(TimeRepresentation.RFC_3339_STRING)
            .build();

    return new SpringMongoEventStore(mongoTemplate, eventStoreConfig);
  }

  //  MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
  //  MongoTransactionManager mongoTransactionManager = new MongoTransactionManager(new
  // SimpleMongoClientDatabaseFactory(mongoClient, "database"));
  //
  //  MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "database");
  //  EventStoreConfig eventStoreConfig = new EventStoreConfig.Builder()
  //      // The collection where all events will be stored
  //      .eventStoreCollectionName("events")
  //      .transactionConfig(mongoTransactionManager)
  //      // How the CloudEvent "time" property will be serialized in MongoDB! !!Important!!
  //      .timeRepresentation(TimeRepresentation.RFC_3339_STRING)
  //      .build();
  //
  //  SpringMongoEventStore eventStore = new SpringMongoEventStore(mongoTemplate, eventStoreConfig);

}
