package com.soffacty.tasksapp.task;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TaskService {

    @Inject
    MongoClient mongoClient;

    public List<Task> list() {
        List<Task> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Task task = new Task();
                task.setTitle(document.getString("title"));
                task.setDescription(document.getString("description"));
                list.add(task);
            }
        }

        return list;
    }

    public void add(Task task) {
        Document document = new Document()
                .append("title", task.getTitle())
                .append("description", task.getDescription());
        getCollection().insertOne(document);
    }


    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("tasksapp").getCollection("task");
    }
}
