package tracker.model;



public class Subtask extends Task {

    public int getEpicId() {
        return epicId;
    }

    private final int epicId;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }


    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
