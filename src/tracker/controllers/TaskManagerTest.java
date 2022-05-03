package tracker.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {
    private HistoryManager historyManager;
    private TaskManager taskManager;

    @BeforeEach
    public void beforeTest() {
        historyManager = createHistoryManager();
        taskManager = createTaskManager(historyManager);
    }

    abstract HistoryManager createHistoryManager();

    abstract TaskManager createTaskManager(HistoryManager historyManager);


    @Test
    void createTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final Collection<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertTrue(tasks.contains(task), "Задачи не совпадают.");

    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic);
        final int epicId = epic.getId();
        final Task savedEpic = taskManager.getById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void createEpicAndSubtask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId());
        taskManager.createTask(subtask);
        final int subtaskId = subtask.getId();
        final Task savedSubtask = taskManager.getById(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        final Collection<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertTrue(tasks.contains(subtask), "Задачи не совпадают.");
        assertTrue(epic.getSubTaskIds().contains(subtask.getId()), "Неверная привязка subtask к epic.");

    }

    @Test
    void clearByIdTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        final int taskId = task.getId();
        final Collection<Task> tasks = taskManager.getTasks();
        taskManager.clearById(taskId);
        assertEquals(tasks, taskManager.getTasks(), "Задача не удалена.");
    }

    @Test
    void clearByIdEpicAndSubtask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId());
        taskManager.createTask(subtask);
        final int epicId = epic.getId();
        final Collection<Task> tasks = taskManager.getTasks();
        taskManager.clearById(epicId);
        assertFalse(tasks.contains(subtask), "Subtask не удалена.");
        assertNotEquals(epic, taskManager.getTasks(), "Задача не удалена.");

    }

    @Test
    void getSubTasks() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic);
        final int epicId = epic.getId();
        assertNull(taskManager.getSubTasks(epicId), "Список подзадач не пуст.");
    }

    @Test
    void update() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        final Status taskStatus = task.getStatus();
        taskManager.update(task);
        assertEquals(taskStatus, task.getStatus(), "Статус изменился");
    }


    @Test
    void updateTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        final Status taskStatus = task.getStatus();
        task.setStatus(Status.IN_PROGRESS);
        taskManager.update(task);
        assertNotEquals(taskStatus, task.getStatus(), "Статус не изменился");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId());
        taskManager.createTask(subtask);
        final Status taskStatus = subtask.getStatus();
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.update(subtask);
        assertNotEquals(taskStatus, subtask.getStatus(), "Статус не изменился");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic);
        final Status taskStatus = epic.getStatus();
        epic.setStatus(Status.IN_PROGRESS);
        taskManager.update(epic);
        assertNotEquals(taskStatus, epic.getStatus(), "Статус не изменился");
    }

    @Test
    void updateEpicAndSubtaskNew() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic);
        Subtask subtask1 = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", epic.getId());
        taskManager.createTask(subtask1);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description", epic.getId());
        taskManager.createTask(subtask2);
        final Status taskStatus = epic.getStatus();
        taskManager.update(epic);
        assertEquals(taskStatus, epic.getStatus(), "Статус изменился");
    }

    @Test
    void updateEpicAndSubtaskDone() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic);
        Subtask subtask1 = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", epic.getId());
        taskManager.createTask(subtask1);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description", epic.getId());
        taskManager.createTask(subtask2);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.update(subtask1);
        taskManager.update(subtask2);
        taskManager.update(epic);
        assertEquals(Status.DONE, epic.getStatus(), "Статус не изменился");
    }

    @Test
    void updateEpicAndSubtaskNewAndDone() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createTask(epic);
        Subtask subtask1 = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", epic.getId());
        taskManager.createTask(subtask1);
        Subtask subtask2 = new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description", epic.getId());
        taskManager.createTask(subtask2);
        subtask1.setStatus(Status.DONE);
        taskManager.update(subtask1);
        taskManager.update(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус не коректно изменился");
    }

    @Test
    void getHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        taskManager.getById(task.getId());
        final List<Task> history = taskManager.getHistory().getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

    }

    @Test
    void shouldThrowException(){
        assertThrows(NullPointerException.class, () -> taskManager.clearById(150));
        Task taskk = null;
        assertThrows(NullPointerException.class, () -> taskManager.update(null));
        assertThrows(NullPointerException.class, () -> taskManager.getSubTasks(100));
    }
}
