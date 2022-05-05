package tracker.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.controllers.HistoryManager;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private TaskManager taskManager;

    @BeforeEach
    public void beforeTest() {
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault(historyManager);

    }

    @Test
    void add() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        historyManager.add(task);
        final List<Task> history1 = historyManager.getHistory();
        assertNotNull(history1, "История не пустая.");
        assertEquals(1, history1.size(), "История не пустая.");
        historyManager.add(task);
        final List<Task> history2 = historyManager.getHistory();
        assertEquals(1, history2.size(), "История продублировалась");
        assertEquals(history1, history2, "История не одинаковая");
    }

    @Test
    void getHistory() {
        final List<Task> history1 = historyManager.getHistory();
        assertTrue(history1.isEmpty(), "История не пустая");
        assertEquals(0, history1.size(), "История не пустая.");
    }

    @Test
    void removeFirstAndLast() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        Task task1 = new Task("Test addNewTask1", "Test addNewTask1 description");
        taskManager.createTask(task1);
        historyManager.add(task1);
        historyManager.remove(task);
        assertFalse(historyManager.getHistory().contains(task), "Первый элемент не удален");
        historyManager.add(task);
        historyManager.remove(task);
        assertFalse(historyManager.getHistory().contains(task), "Последний элемент не удален");
    }


    @Test
    void removeMiddle() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        Task task1 = new Task("Test addNewTask1", "Test addNewTask1 description");
        taskManager.createTask(task1);
        Task task2 = new Task("Test addNewTask2", "Test addNewTask2 description");
        taskManager.createTask(task2);
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task2);
        final List<Task> history = historyManager.getHistory();
        historyManager.remove(task1);
        assertNotEquals(history, historyManager.getHistory(), "Элемент не удалился из середины");
    }
    @Test
    void shouldThrowException(){
        assertThrows(NullPointerException.class, () -> historyManager.add(null));
        assertThrows(NullPointerException.class, () -> historyManager.remove(null));
    }

}