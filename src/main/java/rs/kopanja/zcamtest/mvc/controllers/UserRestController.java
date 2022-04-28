package rs.kopanja.zcamtest.mvc.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.kopanja.zcamtest.data.user.UserService;
import rs.kopanja.zcamtest.mvc.api.UserModel;
import rs.kopanja.zcamtest.mvc.forms.UserForm;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "user-rest-controller", description = "REST API for user related CRUD operations")
@RestController
@RequestMapping(value = "/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Fetches all users from an external dummy API on https://jsonplaceholder.typicode.com/users/")
    @GetMapping("/all")
    public ResponseEntity<List<UserModel>> fetchAllUsers() {
        return new ResponseEntity<>(userService.fetchUsersFromExternal(), HttpStatus.OK);
    }

    @ApiOperation(value = "Fetches one user with our internal ID, or returns empty body if user not found")
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> fetchUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Updates the user with our internal ID with the new data from the form")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatedUserData(@PathVariable("id") Long id,
                                             @RequestBody @Valid UserForm form,
                                             BindingResult bindingResult) {
        List<String> errors = handleBindingErrors(bindingResult);
        if (!CollectionUtils.isEmpty(errors)) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.updateUserData(id, form), HttpStatus.OK);
    }

    @ApiOperation(value = "Deletes the user with ID from the URL")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserForId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Creates a new user with data from the form")
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserForm form,
                                                BindingResult bindingResult) {
        List<String> errors = handleBindingErrors(bindingResult);
        if (!CollectionUtils.isEmpty(errors)) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.createUser(form), HttpStatus.OK);
    }

    /**
     * Handles and returns any binding errors
     *
     * @param bindingResult Binding result containing all validation data
     * @return List of errors
     */
    private List<String> handleBindingErrors(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fe -> errors.add(String.format("%s: %s", fe.getField(),
                    fe.getDefaultMessage())));
        }

        return errors;
    }
}
