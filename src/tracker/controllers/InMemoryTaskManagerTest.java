package tracker.controllers;


class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    HistoryManager createHistoryManager() {
        return Managers.getDefaultHistory();
    }

    @Override
    TaskManager createTaskManager(HistoryManager historyManager) {
        return Managers.getDefault(historyManager);
    }


}