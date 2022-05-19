package com.example.testrestfulwebservice.controller;

import com.example.testrestfulwebservice.model.binding.UserLoginBindingModel;
import com.example.testrestfulwebservice.model.entity.UserEntity;
import com.example.testrestfulwebservice.model.service.UserServiceModel;
import com.example.testrestfulwebservice.model.view.UserViewModel;
import com.example.testrestfulwebservice.service.UserService;
import com.example.testrestfulwebservice.model.binding.UserRegisterBindingModel;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<UserEntity> save(@RequestBody UserEntity userEntity) {
        return new ResponseEntity<>(this.userService.save(userEntity), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UserEntity> getAll() {
        return this.userService.getAll();
    }

    @GetMapping("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<UserEntity> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.userService.getById(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<UserEntity> update(@PathVariable("id") Long id, @RequestBody UserEntity userEntity) {
        return new ResponseEntity<>(this.userService.update(userEntity, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        this.userService.delete(id);
        return new ResponseEntity<>("Employee deleted successfully!", HttpStatus.OK);
    }

    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel(){
        return new UserRegisterBindingModel();
    }

    @ModelAttribute
    public UserLoginBindingModel userLoginBindingModel(){
        return  new UserLoginBindingModel();
    }

    @GetMapping("/register")
    public String register(Model model){
        return "register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid UserRegisterBindingModel userRegisterBindingModel,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()||!userRegisterBindingModel
                .getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel",bindingResult);

            return "redirect:register";
        }

        boolean isNameExists=userService.isNameExists(userRegisterBindingModel.getUsername());

        if(isNameExists){
            //TODO redirect with message
        }

        userService.registerUser(modelMapper
                .map(userRegisterBindingModel, UserServiceModel.class));

        return "redirect:login";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("isExists",true);
        return "login";
    }

    @PostMapping("/login")
    public String loginConfirm(@Valid UserLoginBindingModel userLoginBindingModel,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel",bindingResult);

            return "redirect:login";
        }

        UserServiceModel user = userService
                .findUserByUsernameAndPassword(userLoginBindingModel.getUsername(), userLoginBindingModel.getPassword());

        if(user==null){
            redirectAttributes.addFlashAttribute("isExists", false);
            redirectAttributes.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel",bindingResult);

            return "redirect:login";
        }

        userService.loginUser(user.getId(),user.getUsername());

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(){
        userService.logout();
        return "redirect:/";
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable Long id, Model model){

        model.addAttribute("user", modelMapper
                .map(userService.findById(id), UserViewModel.class));
        return "profile";
    }
}
