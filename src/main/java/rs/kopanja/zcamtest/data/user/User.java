package rs.kopanja.zcamtest.data.user;

import rs.kopanja.zcamtest.mvc.api.UserModel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "zcm_user")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long extId; // external ID of the user (from the service)

    @Column
    private String name;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String website;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    private Address address;

    protected User() {}

    protected User(Long extId, String name, String username, String email, String phone, String website, Address address) {
        this.extId = extId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExtId() {
        return extId;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static User fromModel(UserModel model) {
        return new User(model.getId(),
                model.getName(),
                model.getUsername(),
                model.getEmail(),
                model.getPhone(),
                model.getWebsite(),
                Address.fromModel(model.getAddress())
        );
    }

    public static UserModel toModel(User user) {
        UserModel model = new UserModel();
        model.setId(user.getId());
        model.setExtId(user.getExtId());
        model.setName(user.getName());
        model.setUsername(user.getUsername());
        model.setEmail(user.getEmail());
        model.setPhone(user.getPhone());
        model.setWebsite(user.getWebsite());
        model.setAddress(Address.toModel(user.getAddress()));
        return model;
    }
}
