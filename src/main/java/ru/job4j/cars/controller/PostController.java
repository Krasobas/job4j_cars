package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cars.dto.post.PostCreateDto;
import ru.job4j.cars.dto.post.PostEditDto;
import ru.job4j.cars.dto.post.PostListingDto;
import ru.job4j.cars.dto.post.PostViewDto;
import ru.job4j.cars.dto.user.UserCreateDto;
import ru.job4j.cars.dto.user.UserLoginDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.model.Photo;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.bodytype.BodyTypeService;
import ru.job4j.cars.service.brand.BrandService;
import ru.job4j.cars.service.color.ColorService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.photo.PhotoService;
import ru.job4j.cars.service.post.PostService;
import ru.job4j.cars.service.storage.FileStorageService;
import ru.job4j.cars.service.user.UserService;

import java.util.*;

@Log4j2
@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostService service;
    private final BrandService brandService;
    private final BodyTypeService bodyTypeService;
    private final EngineService engineService;
    private final ColorService colorService;
    private final PhotoService photoService;
    private final FileStorageService storage;

    @GetMapping
    public String getAll(@RequestParam(name = "filter", required = false) String filter, @SessionAttribute UserSessionDto user, Model model) {
        Collection<PostListingDto> list = Objects.isNull(filter) || filter.isBlank()
                ? service.findAll(user)
                : service.findByAvailability(user, "available".equals(filter));
        model.addAttribute("posts", list)
                .addAttribute("page", "posts")
                .addAttribute("filter", filter);
        return "posts/post-list";
    }

    @GetMapping("/my")
    public String getMy(@SessionAttribute UserSessionDto user, Model model) {
        model.addAttribute("posts", service.findByOwnerId(user))
                .addAttribute("page", "my");
        return "posts/post-list";
    }

    @GetMapping("/subscriptions")
    public String getFavorites(@SessionAttribute UserSessionDto user, Model model) {
        model.addAttribute("posts", service.findBySubscriberId(user))
                .addAttribute("page", "subscriptions");
        return "posts/post-list";
    }

    @GetMapping("/search")
    public String searchByName(@RequestParam(name = "title") String title, @SessionAttribute UserSessionDto user, Model model) {
        model.addAttribute("posts",
                        Objects.isNull(title) || title.isBlank()
                                ? service.findAll(user)
                                : service.findByTitleLike(user, title)
                )
                .addAttribute("page", "search");
        return "posts/post-list";
    }

    @GetMapping("/new")
    public String getCreateForm(Model model) {
        model.addAttribute("brands", brandService.findAll())
                .addAttribute("bodyTypes", bodyTypeService.findAll())
                .addAttribute("engines", engineService.findAll())
                .addAttribute("colors", colorService.findAll());
        return "posts/post-form";
    }

    @PostMapping
    public String save(
            @ModelAttribute PostCreateDto post,
            @RequestParam("photo-files") MultipartFile[] photosFiles,
            RedirectAttributes redirectAttributes,
            @SessionAttribute UserSessionDto user,
            Model model) {
        if (!service.persist(post, user, photosFiles)) {
            model.addAttribute("error", "Something went wrong. Please try again.");
            return "redirect:/error/500";
        }
        redirectAttributes.addFlashAttribute("success", "Post created successfully!");
        return "redirect:/posts";

    }

    @GetMapping("/{id}")
    public String getById(@PathVariable(name = "id") Long id, @SessionAttribute UserSessionDto user, Model model) {
        Optional<PostViewDto> found = service.findById(id, user);
        if (found.isEmpty()) {
            model.addAttribute("error", "Post not found.");
            return "redirect:/error/404";
        }
        model.addAttribute("post", found.get());
        return "posts/post-one";
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(@PathVariable(name = "id") Long id, @SessionAttribute UserSessionDto user, Model model) {
        Optional<PostEditDto> found = service.findByIdOnEdit(id, user);
        if (found.isEmpty()) {
            model.addAttribute("error", "Post not found.");
            return "redirect:/error/404";
        }
        model.addAttribute("post", found.get())
                .addAttribute("brands", brandService.findAll())
                .addAttribute("bodyTypes", bodyTypeService.findAll())
                .addAttribute("engines", engineService.findAll())
                .addAttribute("colors", colorService.findAll());
        return "posts/post-form";
    }

    @PutMapping("/{id}")
    public String edit(
            @ModelAttribute PostEditDto post,
            @RequestParam("photo-files") MultipartFile[] photosFiles,
            RedirectAttributes redirectAttributes,
            @SessionAttribute UserSessionDto user,
            Model model) {
        if (!service.update(post, user, photosFiles)) {
            model.addAttribute("error", "Something went wrong. Please try again.");
            return "redirect:/error/500";
        }
        redirectAttributes.addFlashAttribute("success", "Post updated successfully!");
        return "redirect:/posts";

    }

    @PostMapping("/{id}/subscribe")
    public String toggleSubscribe(@PathVariable(name = "id") Long id, @SessionAttribute UserSessionDto user, Model model) {
        if (!service.toggleSubscribe(id, user)) {
            model.addAttribute("error", "Cannot update subscriptions.");
            return "redirect:/error/error";
        }
        return String.format("redirect:/posts/%d", id);
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id, Model model) {
        if (!service.delete(id)) {
            model.addAttribute("error", "Post not found.");
            return "redirect:/error/404";
        }
        return "redirect:/posts";
    }
}
