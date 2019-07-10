package jonomoneta.juno.moviecash.Model;

public class GeneralItem extends ListItem {

    private ReleasedMovie movieArrayList;

    public ReleasedMovie getMovieArrayList() {
        return movieArrayList;
    }

    public void setPojoOfJsonArray(ReleasedMovie movieArrayList) {
        this.movieArrayList = movieArrayList;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
