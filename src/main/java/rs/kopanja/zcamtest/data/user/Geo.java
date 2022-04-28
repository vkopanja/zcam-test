package rs.kopanja.zcamtest.data.user;

import rs.kopanja.zcamtest.mvc.api.GeoModel;

import javax.persistence.Embeddable;

@Embeddable
class Geo {

    private Double lat;
    private Double lng;

    protected Geo() {
    }

    protected Geo(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public static Geo fromModel(GeoModel model) {
        return new Geo(model.getLat(), model.getLng());
    }

    public static GeoModel toModel(Geo geo) {
        GeoModel model = new GeoModel();
        model.setLat(geo.getLat());
        model.setLng(geo.getLng());
        return model;
    }
}
