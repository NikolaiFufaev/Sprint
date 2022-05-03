package tracker.controllers;

import tracker.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileBackedTasksManager extends InMemoryTaskManager {


    public FileBackedTasksManager(HistoryManager historyList) {
        super(historyList);
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();

    }

    @Override
    public void update(Task newTask) {
        super.update(newTask);
        save();

    }

    @Override
    public void clearById(int id) {
        super.clearById(id);
        save();
    }

    @Override
    public Task getById(int id) {
        Task byId = super.getById(id);
        save();
        return byId;
    }

    private void save() {
        try {
            Writer fileWriter = new FileWriter("save.txt");
            for (Task task : taskMap.values()) {
                fileWriter.write(toString(task) + "\n");
            }
            fileWriter.write("\n ");
            fileWriter.write(historyList.getNodeId());


            fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }


    static String toString(Task task) {

        if (task.getType() == TaskType.TASK) {
            return String.format("%d,%s,%s,%s,%s",
                    task.getId(),
                    task.getType(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription());
        } else if (task.getType() == TaskType.EPIC) {
            Epic epic = (Epic) task;
            return String.format("%d,%s,%s,%s,%s",
                    epic.getId(),
                    epic.getType(),
                    epic.getTitle(),
                    epic.getStatus(),
                    epic.getDescription());
        } else if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,%s,%s,%s,%s,%d",
                    subtask.getId(),
                    subtask.getType(),
                    subtask.getTitle(),
                    subtask.getStatus(),
                    subtask.getDescription(),
                    subtask.getEpicId());
        }
        return null;
    }

    static Task fromString(String value) {

        String[] item = value.split(",");
        TaskType type = TaskType.valueOf(item[1]);
        switch (type) {
            case TASK:
                Task task = new Task(item[2], item[4]);
                task.setId(Integer.parseInt(item[0]));
                task.setStatus(Status.valueOf(item[3]));
                return task;
            case SUBTASK:
                Subtask subtask = new Subtask(item[2], item[4], Integer.parseInt(item[5]));
                subtask.setId(Integer.parseInt(item[0]));
                subtask.setStatus(Status.valueOf(item[3]));
                return subtask;
            case EPIC:
                Epic epic = new Epic(item[2], item[4]);
                epic.setId(Integer.parseInt(item[0]));
                epic.setStatus(Status.valueOf(item[3]));
                return epic;
        }
        return null;
    }

    static FileBackedTasksManager loadFromFile(Path file) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(historyManager);
        try {
            String readString = Files.readString(file);
            String[] split = readString.split("\n ");
            String[] strings = split[0].split("\n");
            for (String line : strings) {
                Task task = fromString(line);
                fileBackedTasksManager.createTask(task);
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        return fileBackedTasksManager;
    }

    static Path fileBack() {
        Path uniPath = Paths.get("").toAbsolutePath();
        Path fileBacked = Paths.get(uniPath + "\\save.txt");
        return fileBacked;
    }


    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTasksManager taskManager = new FileBackedTasksManager(historyManager);

        Task task = new Task("First tracker.Task", "description");
        taskManager.createTask(task);

        Epic epic = new Epic("First tracker.Epic", "description");
        taskManager.createTask(epic);

        Subtask subtask = new Subtask("First tracker.Subtask", "description", epic.getId());
        taskManager.createTask(subtask);


        taskManager.getById(task.getId());

        taskManager.getById(epic.getId());
        System.out.println(taskManager.getTasks());
        System.out.println(historyManager.getHistory());

        System.out.println(loadFromFile(fileBack()).getTasks());
    }
}

