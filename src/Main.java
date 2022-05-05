import tracker.model.*;
import tracker.controllers.*;


public class Main {
    public static void main(String[] args) {
        InMemoryHistoryManager historyManager =(InMemoryHistoryManager) Managers.getDefaultHistory();
        InMemoryTaskManager taskManager =(InMemoryTaskManager) Managers.getDefault(historyManager);


        Task task = new Task("First tracker.Task", "description");
        taskManager.createTask(task);
        task.setStartTime("21.10.2021 00:00");
        taskManager.update(task);
        Task secondTask = new Task("Second tracker.Task", "description");
        taskManager.createTask(secondTask);

        Epic epic = new Epic("First tracker.Epic", "description");
        taskManager.createTask(epic);

        Subtask subtask = new Subtask("First tracker.Subtask", "description", epic.getId());
        taskManager.createTask(subtask);

        Subtask secondSubtask = new Subtask("Second tracker.Subtask", "description", epic.getId());
        taskManager.createTask(secondSubtask);

        Epic secondEpic = new Epic("Second tracker.Epic", "description");
        taskManager.createTask(secondEpic);

        Subtask thirdSubtask = new Subtask("Third tracker.Subtask", "description", epic.getId());
        taskManager.createTask(thirdSubtask);
        System.out.println(taskManager.getTasks());

        taskManager.getById(task.getId());
        System.out.println(historyManager.getHistory());

        taskManager.getById(secondTask.getId());
        System.out.println(historyManager.getHistory());

        taskManager.getById(secondEpic.getId());
        System.out.println(historyManager.getHistory());

        taskManager.clearById(task.getId());
        System.out.println(historyManager.getHistory());

        taskManager.clearById(epic.getId());
        System.out.println(taskManager.getTasks());
        System.out.println(historyManager.getHistory());



    }
}
