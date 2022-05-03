package tracker.controllers;

import tracker.model.*;

import java.util.*;


public interface TaskManager {
    void createTask(Task task);

    Collection<Task> getTasks();

    Task getById(int id);

    void clearById(int id);

    List<Subtask> getSubTasks(int id);

    void update(Task newTask);

    void updateTask(Task newTask, Task oldTask);

    void updateSubtask(Subtask newTask, Subtask oldTask);

    void updateEpic(Epic newTask, Epic oldTask);

    HistoryManager getHistory();
}


