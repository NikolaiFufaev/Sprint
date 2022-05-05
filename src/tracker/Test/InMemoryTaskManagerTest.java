package tracker.Test;


import tracker.controllers.HistoryManager;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;

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