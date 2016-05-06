package at.bro.code.solr.blockjoins.api;

import java.util.Arrays;

public class MilagePackageReceived {
    private final String location;
    private final Integer[] milages;

    public MilagePackageReceived(String location, Integer... milages) {
        this.location = location;
        this.milages = milages;
    }

    public String getLocation() {
        return location;
    }

    public Integer[] getMilages() {
        return milages;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + Arrays.hashCode(milages);
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
        final MilagePackageReceived other = (MilagePackageReceived) obj;
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        if (!Arrays.equals(milages, other.milages)) {
            return false;
        }
        return true;
    }

}
