package tracker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public void addSubTask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public List<Integer> getSubTaskIds() {
        return subtaskIds;
    }

    @Override
    public String toString() {
        return "tracker.model.Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
