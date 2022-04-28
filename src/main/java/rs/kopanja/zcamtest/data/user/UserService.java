package rs.kopanja.zcamtest.data.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import rs.kopanja.zcamtest.mvc.api.UserModel;
import rs.kopanja.zcamtest.mvc.forms.UserForm;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final RestTemplate restTemplate;
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.restTemplate = new RestTemplate();
        this.repository = repository;
    }

    /**
     * Makes an external API call over HTTP, to the dummy REST service located at
     * https://jsonplaceholder.typicode.com/users/
     *
     * @return Returns a list of user models
     */
    public List<UserModel> fetchUsersFromExternal() {
        logger.info("Starting to fetch users");
        URI requestUri = URI.create(BASE_URL).resolve("users");
        ParameterizedTypeReference<List<UserModel>> returnType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<UserModel>> userResponse = restTemplate.exchange(requestUri, HttpMethod.GET,
                null, returnType);

        if (userResponse.getStatusCode().is2xxSuccessful()) {
            if (!CollectionUtils.isEmpty(userResponse.getBody())) {
                logger.info("Finished fetching users with HTTP 200, number of users fetched {}",
                        userResponse.getBody().size());
                return insertOrUpdateUsers(userResponse.getBody());
            }
        } else {
            logger.warn("Failed getting users from external API, error code is {}", userResponse.getStatusCode());
        }

        logger.info("Fetched 0 users, returning empty array");
        return new ArrayList<>();
    }

    /**
     * Returns a {@link UserModel} for the requested {@literal id}, or {@literal null} if user not found
     *
     * @param id ID of the user
     * @return User model
     */
    public UserModel findUserById(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.map(User::toModel).orElse(null);
    }

    /**
     * Loads the user entity with {@literal id} and tries to update it with new data
     * from {@literal form}, returns null if user not found
     *
     * @param id   ID of the user
     * @param form Form with new user data
     * @return User model
     */
    public UserModel updateUserData(Long id, UserForm form) {
        logger.info("Trying to update user with ID {}", id);

        Optional<User> userEntity = repository.findById(id);
        if (userEntity.isPresent()) {
            logger.info("User found, updating with new data {}", form);
            User user = userEntity.get();
            user.setName(form.getName());
            user.setUsername(form.getUsername());
            user.setEmail(form.getEmail());
            user = repository.save(user);
            logger.info("Successfully updated, returning updated user model");
            return User.toModel(user);
        }

        logger.warn("User with ID {} not found, returning null", id);
        return null;
    }

    /**
     * Deletes a user with the {@literal id}
     *
     * @param id ID of the user
     */
    public void deleteUserForId(Long id) {
        repository.deleteById(id);
    }

    public UserModel createUser(UserForm form) {
        User user = new User();
        user.setName(form.getName());
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        return User.toModel(repository.save(user));
    }

    /**
     * Returns all users from the DB and converts them to {@link UserModel}
     *
     * @return List of user models
     */
    public List<UserModel> allUsers() {
        List<User> users = repository.findAll();
        return users.stream().map(User::toModel).collect(Collectors.toList());
    }

    /**
     * Utility method that will basically synchronize our data (i.e. insert, update or delete the user data, depending
     * on what is needed to be done)
     *
     * @param userModels List of {@link UserModel} that we receive from the external model
     * @return Return a list of updated/inserted users
     */
    private List<UserModel> insertOrUpdateUsers(List<UserModel> userModels) {
        List<Long> userIds = userModels.stream().map(UserModel::getId).collect(Collectors.toList());
        List<User> allUserForIds = repository.findAllByExtId(userIds);
        List<UserModel> models = new ArrayList<>();

        Map<Long, User> userMap = allUserForIds.stream().collect(Collectors.toMap(User::getExtId, user -> user));
        userModels.forEach(um -> {
            User existing = userMap.remove(um.getId());
            if (existing != null) {
                existing.setExtId(um.getId());
                existing.setName(um.getName());
                existing.setUsername(um.getUsername());
                existing.setEmail(um.getEmail());
                existing.setPhone(um.getPhone());
                existing.setWebsite(um.getWebsite());
                existing.getAddress().setCity(um.getAddress().getCity());
                existing.getAddress().setSuite(um.getAddress().getSuite());
                existing.getAddress().setStreet(um.getAddress().getStreet());
                existing.getAddress().setZipcode(um.getAddress().getZipcode());
                existing.getAddress().setGeo(Geo.fromModel(um.getAddress().getGeo()));
                repository.save(existing);
                models.add(User.toModel(existing));
            } else {
                User user = new User();
                user.setExtId(um.getId());
                user.setName(um.getName());
                user.setUsername(um.getUsername());
                user.setEmail(um.getEmail());
                user.setPhone(um.getPhone());
                user.setWebsite(um.getWebsite());
                Address address = Address.fromModel(um.getAddress());
                address.setUser(user);
                user.setAddress(address);
                repository.save(user);
                models.add(User.toModel(user));
            }
        });

        List<User> usersToDelete = new ArrayList<>();
        userMap.forEach((id, user) -> {
            usersToDelete.add(user);
        });
        repository.deleteAll(usersToDelete);
        return models;
    }
}
