package com.soffacty.tasksapp.task;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskService taskService;

    @Inject
    ReactiveTaskService reactiveTaskService;

    @GET
    public List<Task> list() {
        return taskService.list();
    }

    @GET
    @Path("reactive")
    public Uni<List<Task>> reactiveList() {
        return reactiveTaskService.list();
    }

    @POST
    public void add(Task task) {
        taskService.add(task);
    }

    @POST
    @Path("reactive")
    public Uni<List<Task>> reactiveAdd(Task task) {
        return reactiveTaskService.add(task)
                .onItem().ignore().andSwitchTo(this::reactiveList);
    }
}
