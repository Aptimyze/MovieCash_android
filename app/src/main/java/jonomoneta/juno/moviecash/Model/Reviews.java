package jonomoneta.juno.moviecash.Model;

public class Reviews {

    String name, time, review;

    public Reviews(String name, String time, String review) {
        this.name = name;
        this.time = time;
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
