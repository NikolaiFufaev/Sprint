package tracker.controllers;

class FileBackedTasksManagerTest extends TaskManagerTest {

    @Override
    HistoryManager createHistoryManager() {
        return Managers.getDefaultHistory();
    }

    @Override
    TaskManager createTaskManager(HistoryManager historyManager) {
        return new FileBackedTasksManager(historyManager);
    }
}