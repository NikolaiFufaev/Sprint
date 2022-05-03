package tracker.controllers;


public class Managers {

    private static HistoryManager inMemoryHistoryManager;
    private static TaskManager inMemoryTaskManager;

    public static HistoryManager getDefaultHistory() {

        inMemoryHistoryManager = new InMemoryHistoryManager();

        return inMemoryHistoryManager;
    }

    public static TaskManager getDefault(HistoryManager history) {

        inMemoryTaskManager = new InMemoryTaskManager(history);

        return inMemoryTaskManager;
    }
}