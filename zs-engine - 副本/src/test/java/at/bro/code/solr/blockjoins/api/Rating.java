package at.bro.code.solr.blockjoins.api;


public class Rating {
    private final String location;
    private final String rating;

    public Rating(String location, String rating) {
        this.location = location;
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((rating == null) ? 0 : rating.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rating other = (Rating) obj;
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        if (rating == null) {
            if (other.rating != null) {
                return false;
            }
        } else if (!rating.equals(other.rating)) {
            return false;
        }
        return true;
    }

}