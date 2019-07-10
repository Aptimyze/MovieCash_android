package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MovieDetailsResponse implements Serializable {

    @SerializedName("ResponseID")
    @Expose
    private int ResponseID;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("ResponseData")
    @Expose
    private ResponseData responseData;

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    @SerializedName("ResponseJSON")
    @Expose
    private String ResponseJSON;

    public int getResponseID() {
        return ResponseID;
    }

    public void setResponseID(int responseID) {
        ResponseID = responseID;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getResponseJSON() {
        return ResponseJSON;
    }

    public void setResponseJSON(String responseJSON) {
        ResponseJSON = responseJSON;
    }

    public class ResponseData implements Serializable {

        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("Name")
        @Expose
        private String Name;

        @SerializedName("Description")
        @Expose
        private String Description;

        @SerializedName("MovieCategoryName")
        @Expose
        private String MovieCategoryName;

        @SerializedName("MovieTypeName")
        @Expose
        private String MovieTypeName;

        @SerializedName("MovieLanguageName")
        @Expose
        private String MovieLanguageName;

        @SerializedName("Poster")
        @Expose
        private String Poster;

        @SerializedName("ReleaseDate")
        @Expose
        private String ReleaseDate;

        @SerializedName("CountryName")
        @Expose
        private String CountryName;

        @SerializedName("Studio")
        @Expose
        private String Studio;

        @SerializedName("Directors")
        @Expose
        private String Directors;

        @SerializedName("Actors")
        @Expose
        private String Actors;

        @SerializedName("Rating")
        @Expose
        private float Rating;

        @SerializedName("Reviews")
        @Expose
        private String Reviews;

        @SerializedName("MovieTrailersList")
        @Expose
        private ArrayList<Trailers> trailersArrayList;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getMovieCategoryName() {
            return MovieCategoryName;
        }

        public void setMovieCategoryName(String movieCategoryName) {
            MovieCategoryName = movieCategoryName;
        }

        public String getMovieTypeName() {
            return MovieTypeName;
        }

        public void setMovieTypeName(String movieTypeName) {
            MovieTypeName = movieTypeName;
        }

        public String getMovieLanguageName() {
            return MovieLanguageName;
        }

        public void setMovieLanguageName(String movieLanguageName) {
            MovieLanguageName = movieLanguageName;
        }

        public String getPoster() {
            return Poster;
        }

        public void setPoster(String poster) {
            Poster = poster;
        }

        public String getReleaseDate() {
            return ReleaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            ReleaseDate = releaseDate;
        }

        public String getCountryName() {
            return CountryName;
        }

        public void setCountryName(String countryName) {
            CountryName = countryName;
        }

        public String getStudio() {
            return Studio;
        }

        public void setStudio(String studio) {
            Studio = studio;
        }

        public String getDirectors() {
            return Directors;
        }

        public void setDirectors(String directors) {
            Directors = directors;
        }

        public String getActors() {
            return Actors;
        }

        public void setActors(String actors) {
            Actors = actors;
        }

        public float getRating() {
            return Rating;
        }

        public void setRating(float rating) {
            Rating = rating;
        }

        public String getReviews() {
            return Reviews;
        }

        public void setReviews(String reviews) {
            Reviews = reviews;
        }

        public ArrayList<Trailers> getTrailersArrayList() {
            return trailersArrayList;
        }

        public void setTrailersArrayList(ArrayList<Trailers> trailersArrayList) {
            this.trailersArrayList = trailersArrayList;
        }

        public class Trailers implements Serializable {

            @SerializedName("ID")
            @Expose
            private int ID;

            @SerializedName("MovieID")
            @Expose
            private int MovieID;

            @SerializedName("VideoUrl")
            @Expose
            private String VideoUrl;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public int getMovieID() {
                return MovieID;
            }

            public void setMovieID(int movieID) {
                MovieID = movieID;
            }

            public String getVideoUrl() {
                return VideoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                VideoUrl = videoUrl;
            }
        }

    }
}
