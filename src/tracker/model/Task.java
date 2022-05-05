package tracker.model;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    protected int id;

    protected String title;

    protected String description;

    protected Status status = Status.NEW;

    protected long startTime;

    protected long duration;


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void setStartTime(String startTime) {

        LocalDateTime time = LocalDateTime.parse(startTime,formatter);
        this.startTime = time.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();}

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime(){
        return getStartTime().plus(Duration.ofMinutes(duration));
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, startTime, duration);
    }
}

