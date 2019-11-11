package jonomoneta.juno.moviecash.Model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceResponse implements Serializable {

    @SerializedName("results")
    @Expose
    private ArrayList<results> resultsArrayList;

    public ArrayList<results> getResultsArrayList() {
        return resultsArrayList;
    }

    public void setResultsArrayList(ArrayList<results> resultsArrayList) {
        this.resultsArrayList = resultsArrayList;
    }

    public class results implements Serializable {

        @SerializedName("geometry")
        @Expose
        private Geometry geometry;

        @SerializedName("icon")
        @Expose
        private String icon;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("place_id")
        @Expose
        private String place_id;

        @SerializedName("types")
        @Expose
        private ArrayList<String> typesArrayList;

        @SerializedName("vicinity")
        @Expose
        private String vicinity;

        private String distance;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public ArrayList<String> getTypesArrayList() {
            return typesArrayList;
        }

        public void setTypesArrayList(ArrayList<String> typesArrayList) {
            this.typesArrayList = typesArrayList;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public class Geometry implements Serializable{

            @SerializedName("location")
            @Expose
            private Location location;

            public Location getLocation() {
                return location;
            }

            public void setLocation(Location location) {
                this.location = location;
            }

            public class Location implements Serializable{

                @SerializedName("lat")
                @Expose
                private double lat;

                @SerializedName("lng")
                @Expose
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }
        }
    }
}
