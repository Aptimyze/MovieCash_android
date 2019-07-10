package jonomoneta.juno.moviecash.Model;

public class ReleasedMovie {

    String name, date;
    int img;

    public ReleasedMovie(String name, int img, String date) {
        this.name = name;
        this.date = date;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
