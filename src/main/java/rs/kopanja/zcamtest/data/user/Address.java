package rs.kopanja.zcamtest.data.user;

import rs.kopanja.zcamtest.mvc.api.AddressModel;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String street;

    @Column
    private String suite;

    @Column
    private String city;

    @Column
    private String zipcode;

    @Embedded
    private Geo geo;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Address() {
    }

    public Address(String street, String suite, String city, String zipcode, Geo geo) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.geo = geo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Address fromModel(AddressModel model) {
        return new Address(model.getStreet(),
                model.getSuite(),
                model.getCity(),
                model.getZipcode(),
                Geo.fromModel(model.getGeo())
        );
    }

    public static AddressModel toModel(Address address) {
        if (address == null) {
            return null;
        }

        AddressModel model = new AddressModel();
        model.setCity(address.getCity());
        model.setStreet(address.getStreet());
        model.setSuite(address.getSuite());
        model.setZipcode(address.getZipcode());
        model.setGeo(Geo.toModel(address.getGeo()));
        return model;
    }
}
