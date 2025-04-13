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
  SpringMongoEventStore springMongoEventStore(
      MongoDatabaseFactory dbFactory, MongoTemplate mongoTemplate) {
    EventStoreConfig eventStoreConfig =
        new EventStoreConfig.Builder()
            // The collection where all events will be stored
            .eventStoreCollectionName("events")
            .transactionConfig(new MongoTransactionManager(dbFactory))
            // How the CloudEvent "time" property will be serialized in MongoDB! !!Important!!
            .timeRepresentation(TimeRepresentation.RFC_3339_STRING)
            .build();

    return new SpringMongoEventStore(mongoTemplate, eventStoreConfig);
  }
}
