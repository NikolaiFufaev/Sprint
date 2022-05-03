package tracker.controllers;

import tracker.model.*;

import java.util.List;
import java.util.*;

import static tracker.model.TaskType.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    protected final HistoryManager historyList;
    protected final Map<Integer, Task> taskMap = new HashMap<>();

    public InMemoryTaskManager(HistoryManager historyList) {
        this.historyList = historyList;
    }

    @Override
    public void createTask(Task task) {
        task.setId(id);
        taskMap.put(id++, task);
        switch (task.getType()) {
            case TASK:
                break;

            case SUBTASK:
                Subtask subtask = (Subtask) task;
                ((Epic) getById(subtask.getEpicId())).addSubTask(subtask.getId());
                break;

            case EPIC:
                break;
        }

    }

    @Override
    public Collection<Task> getTasks() {

        return taskMap.values();
    }


    @Override
    public Task getById(int id) {
        historyList.add(taskMap.get(id));
        return taskMap.get(id);
    }


    @Override
    public void clearById(int id) {
        Task dell = taskMap.get(id);

        if (dell.getType() == EPIC) {
            Epic epic = (Epic) dell;
            List<Subtask> subtasks = getSubTasks(epic.getId());
            for (Subtask sub : subtasks) {
                historyList.remove(sub);
                taskMap.remove(sub.getId());
            }
            taskMap.remove(id);
            historyList.remove(dell);
        } else {
            taskMap.remove(id);
            historyList.remove(dell);
        }
    }


    @Override
    public List<Subtask> getSubTasks(int id) {
        Task task = taskMap.get(id);
        if (task.getType() == EPIC) {
            List<Integer> subTaskIds = ((Epic) task).getSubTaskIds();
            List<Subtask> subtasks = new ArrayList<>();

            for (Integer subTaskId : subTaskIds) {
                subtasks.add((Subtask) getById(subTaskId));
            }
            if (subtasks.isEmpty()) {
                return null;
            }
            return subtasks;
        }
        return null;
    }

    @Override
    public void update(Task newTask) {
        Task oldTask = getById(newTask.getId());
        if (newTask.getType() != oldTask.getType()) {
            return;
        }

        switch (newTask.getType()) {
            case TASK:
                updateTask(newTask, oldTask);
                break;

            case SUBTASK:
                updateSubtask((Subtask) newTask, (Subtask) oldTask);
                break;

            case EPIC:
                updateEpic((Epic) newTask, (Epic) oldTask);
                break;
        }

    }

    @Override
    public void updateTask(Task newTask, Task oldTask) {
        taskMap.put(oldTask.getId(), newTask);
    }

    @Override
    public void updateSubtask(Subtask newTask, Subtask oldTask) {
        taskMap.put(newTask.getId(), newTask);

        boolean allNew = true;
        boolean allDone = true;

        Epic epic = (Epic) getById(newTask.getEpicId());

        List<Subtask> subTasks = getSubTasks(epic.getId());

        for (Subtask subtask : subTasks) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void updateEpic(Epic newTask, Epic oldTask) {
        if (newTask.getStatus() != oldTask.getStatus()) {
            return;
        }
        taskMap.put(newTask.getId(), newTask);

    }

    @Override
    public HistoryManager getHistory() {
        return historyList;
    }


}


