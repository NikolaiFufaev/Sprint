package tracker.controllers;

import tracker.model.*;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;

import static java.lang.System.*;


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

     public void save() {
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


     String toString(Task task) {

        if (task.getType() == TaskType.TASK) {
            return String.format("%d,%s,%s,%s,%s,%d,%d",
                    task.getId(),
                    task.getType(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription(),
                    task.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    task.getDuration());
        } else if (task.getType() == TaskType.EPIC) {
            Epic epic = (Epic) task;
            return String.format("%d,%s,%s,%s,%s,%d,%d",
                    epic.getId(),
                    epic.getType(),
                    epic.getTitle(),
                    epic.getStatus(),
                    epic.getDescription(),
                    epic.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    epic.getDuration()
            );
        } else if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,%s,%s,%s,%s,%d,%d,%d",
                    subtask.getId(),
                    subtask.getType(),
                    subtask.getTitle(),
                    subtask.getStatus(),
                    subtask.getDescription(),
                    subtask.getEpicId(),
                    subtask.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()  ,
                    subtask.getDuration());
        }
        return null;
    }

     static Task fromString(String value) {
        if(value.isEmpty()){return null;}
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

     public static FileBackedTasksManager loadFromFile(Path file) {
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

     public static Path fileBack() {
        Path uniPath = Paths.get("").toAbsolutePath();
        Path fileBacked = Paths.get(uniPath + "\\save.txt");
        return fileBacked;
    }


    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTasksManager taskManager = new FileBackedTasksManager(historyManager);

        Task task = new Task("First tracker.Task", "description");
        taskManager.createTask(task);
        task.setStartTime("20.03.2100 10:00");
        taskManager.update(task);
        task.setStatus(Status.IN_PROGRESS);
        taskManager.update(task);
        Epic epic = new Epic("First tracker.Epic", "description");
        taskManager.createTask(epic);

        Subtask subtask = new Subtask("First tracker.Subtask", "description", epic.getId());
        taskManager.createTask(subtask);
        subtask.setStartTime("21.10.2000 00:00");
        taskManager.update(subtask);
        taskManager.getById(task.getId());

        taskManager.getById(epic.getId());
        out.println(taskManager.getTasks());
        out.println(historyManager.getHistory());
        out.println(taskManager.getPrioritizedTasks());
        out.println(loadFromFile(fileBack()).getTasks());


    }
}

