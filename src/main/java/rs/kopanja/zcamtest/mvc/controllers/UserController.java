package rs.kopanja.zcamtest.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rs.kopanja.zcamtest.data.user.UserService;
import rs.kopanja.zcamtest.mvc.api.UserModel;
import rs.kopanja.zcamtest.mvc.forms.UserForm;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String listUsers(@RequestParam(name = "email", required = false) String email,
                            Model model) {
        List<UserModel> userModels = new ArrayList<>();
        if (StringUtils.hasText(email)) {
            // fetch by email
        } else {
            userModels = userService.allUsers();
        }

        model.addAttribute("users", userModels);
        return "list-users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id,
                           UserForm userForm) {
        UserModel user = userService.findUserById(id);
        if (user != null) {
            userForm.setId(user.getId());
            userForm.setName(user.getName());
            userForm.setEmail(user.getEmail());
            userForm.setUsername(user.getUsername());
        }
        return "edit-user";
    }

    @PostMapping("/edit/{id}")
    public String editUserSubmit(@PathVariable("id") Long id,
                                 @Valid UserForm userForm,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-user";
        }

        userService.updateUserData(id, userForm);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserForId(id);
        return "redirect:/";
    }
}
