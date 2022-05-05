package tracker.model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected long endTime;
    protected List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public void addSubTask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }
    @Override
    public LocalDateTime getEndTime() {
        return LocalDateTime.from(Instant.ofEpochMilli(endTime));
    }

    public void setStartTime(Subtask subtask){
        this.startTime = Instant.from(subtask.getStartTime()).toEpochMilli();
    }

    public void setDurationEpic(){
    duration = Duration.between(getStartTime(),getEndTime()).toMinutesPart();
    }
    public void setEndTime(Subtask subtask){
    this.endTime = Instant.from(subtask.getEndTime()).toEpochMilli();
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
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
