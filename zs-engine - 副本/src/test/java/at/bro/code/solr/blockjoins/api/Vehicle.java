package at.bro.code.solr.blockjoins.api;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

public class Vehicle {
    private final Long id;
    private final List<Price> prices;
    private final List<Rating> ratings;
    private final List<MilagePackageReceived> milagePackages;
    private final String model;
    private final String brand;

    @SuppressWarnings("unchecked")
    public Vehicle(Long id, String brand, String model, List<Price> prices, List<Rating> ratings,
            List<MilagePackageReceived> mileagePackages) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.prices = (List<Price>) ObjectUtils.defaultIfNull(prices, Collections.emptyList());
        this.ratings = (List<Rating>) ObjectUtils.defaultIfNull(ratings, Collections.emptyList());
        this.milagePackages = (List<MilagePackageReceived>) ObjectUtils.defaultIfNull(mileagePackages,
                Collections.emptyList());
    }

    public Long getId() {
        return id;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public List<MilagePackageReceived> getMilagePackages() {
        return milagePackages;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((brand == null) ? 0 : brand.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        result = prime * result + ((prices == null) ? 0 : prices.hashCode());
        result = prime * result + ((ratings == null) ? 0 : ratings.hashCode());
        result = prime * result + ((milagePackages == null) ? 0 : milagePackages.hashCode());
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
        final Vehicle other = (Vehicle) obj;
        if (brand == null) {
            if (other.brand != null) {
                return false;
            }
        } else if (!brand.equals(other.brand)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (model == null) {
            if (other.model != null) {
                return false;
            }
        } else if (!model.equals(other.model)) {
            return false;
        }
        if (prices == null) {
            if (other.prices != null) {
                return false;
            }
        } else if (!prices.equals(other.prices)) {
            return false;
        }
        if (ratings == null) {
            if (other.ratings != null) {
                return false;
            }
        } else if (!ratings.equals(other.ratings)) {
            return false;
        }
        if (milagePackages == null) {
            if (other.milagePackages != null) {
                return false;
            }
        } else if (!milagePackages.equals(other.milagePackages)) {
            return false;
        }
        return true;
    }

}