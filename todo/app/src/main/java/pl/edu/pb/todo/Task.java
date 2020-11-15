package pl.edu.pb.todo;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;

    public Task() {
        id = UUID.randomUUID();
        date = new Date();
    }

    public Task(String name, boolean done) {
        id = UUID.randomUUID();
        date = new Date();
        this.name = name;
        this.done = done;
    }

    public UUID getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public Date getDate() {
        return date;
    }

    public boolean isDone()
    {
        return done;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
