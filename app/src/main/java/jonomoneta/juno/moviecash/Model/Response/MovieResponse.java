package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MovieResponse implements Serializable {


    @SerializedName("ResponseID")
    @Expose
    private int ResponseID;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("ResponseData")
    @Expose
    private ArrayList<ResponseData> responseDataArrayList;

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

    public ArrayList<ResponseData> getResponseDataArrayList() {
        return responseDataArrayList;
    }

    public void setResponseDataArrayList(ArrayList<ResponseData> responseDataArrayList) {
        this.responseDataArrayList = responseDataArrayList;
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

        @SerializedName("RecordTypes")
        @Expose
        private String RecordTypes;

        @SerializedName("MovieID")
        @Expose
        private int MovieID;

        @SerializedName("MovieName")
        @Expose
        private String MovieName;

        @SerializedName("MovieDescription")
        @Expose
        private String MovieDescription;

        @SerializedName("MovieCategoryName")
        @Expose
        private String MovieCategoryName;

        @SerializedName("MovieTypeName")
        @Expose
        private String MovieTypeName;

        @SerializedName("MovieLanguageName")
        @Expose
        private String MovieLanguageName;

        @SerializedName("MoviePoster")
        @Expose
        private String MoviePoster;

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

        @SerializedName("MovieTotalRecords")
        @Expose
        private String MovieTotalRecords;

        @SerializedName("VideoID")
        @Expose
        private String AdvertisementID;

        @SerializedName("VideoName")
        @Expose
        private String AdvertisementName;

        @SerializedName("VideoPoster")
        @Expose
        private String AdvertisementPoster;

        @SerializedName("Video")
        @Expose
        private String AdvertisementVideo;



        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getRecordTypes() {
            return RecordTypes;
        }

        public void setRecordTypes(String recordTypes) {
            RecordTypes = recordTypes;
        }

        public int getMovieID() {
            return MovieID;
        }

        public void setMovieID(int movieID) {
            MovieID = movieID;
        }

        public String getMovieName() {
            return MovieName;
        }

        public void setMovieName(String movieName) {
            MovieName = movieName;
        }

        public String getMovieDescription() {
            return MovieDescription;
        }

        public void setMovieDescription(String movieDescription) {
            MovieDescription = movieDescription;
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

        public String getMoviePoster() {
            return MoviePoster;
        }

        public void setMoviePoster(String moviePoster) {
            MoviePoster = moviePoster;
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

        public String getMovieTotalRecords() {
            return MovieTotalRecords;
        }

        public void setMovieTotalRecords(String movieTotalRecords) {
            MovieTotalRecords = movieTotalRecords;
        }

        public String getAdvertisementID() {
            return AdvertisementID;
        }

        public void setAdvertisementID(String advertisementID) {
            AdvertisementID = advertisementID;
        }

        public String getAdvertisementName() {
            return AdvertisementName;
        }

        public void setAdvertisementName(String advertisementName) {
            AdvertisementName = advertisementName;
        }

        public String getAdvertisementPoster() {
            return AdvertisementPoster;
        }

        public void setAdvertisementPoster(String advertisementPoster) {
            AdvertisementPoster = advertisementPoster;
        }

        public String getAdvertisementVideo() {
            return AdvertisementVideo;
        }

        public void setAdvertisementVideo(String advertisementVideo) {
            AdvertisementVideo = advertisementVideo;
        }
    }


}
