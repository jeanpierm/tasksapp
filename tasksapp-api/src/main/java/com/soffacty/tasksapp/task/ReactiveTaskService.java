package com.soffacty.tasksapp.task;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.List;

@ApplicationScoped
public class ReactiveTaskService {

    @Inject
    ReactiveMongoClient mongoClient;

    public Uni<List<Task>> list() {
        return getCollection().find()
                .map(doc -> {
                    Task task = new Task();
                    task.setTitle(doc.getString("title"));
                    task.setDescription(doc.getString("description"));
                    return task;
                }).collect().asList();
    }

    public Uni<Void> add(Task task) {
        Document document = new Document()
                .append("title", task.getTitle())
                .append("description", task.getDescription());
        return getCollection().insertOne(document)
                .onItem().ignore().andContinueWithNull();
    }

    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("tasksapp").getCollection("task");
    }
}
