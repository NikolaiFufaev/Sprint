import tracker.model.*;
import tracker.controllers.*;


public class Main {
    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault(historyManager);


        Task task = new Task("First tracker.Task", "description");
        taskManager.createTask(task);

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
