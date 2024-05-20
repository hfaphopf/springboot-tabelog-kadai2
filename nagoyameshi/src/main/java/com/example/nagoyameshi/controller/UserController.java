package com.example.nagoyameshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.form.UserEditPaidForm;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.StripeService;
import com.example.nagoyameshi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final StripeService stripeService;

    public UserController(UserRepository userRepository, UserService userService, StripeService stripeService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.stripeService = stripeService;
    }

    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        User user = userRepository.findUserById(userDetailsImpl.getUser().getId());
        model.addAttribute("user", user);
        return "user/index";
    }

    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
        UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(),
            user.getPostalCode(), user.getAddress(), user.getPhoneNumber(), user.getEmail());
        model.addAttribute("userEditForm", userEditForm);
        return "user/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        userService.update(userEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        return "redirect:/user";
    }

    // 無料→有料会員変更画面に遷移
    @GetMapping("/changepaid")
    public String changepaid(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, HttpServletRequest httpServletRequest, Model model) {
        UserEditPaidForm up = new UserEditPaidForm(userDetailsImpl.getUser().getId());
        String sessionId = stripeService.createStripeSession(userDetailsImpl.getUser().getId().toString(), httpServletRequest);
        model.addAttribute("userEditPaidForm", up);
        model.addAttribute("sessionId", sessionId);
        return "user/changepaid";
    }

    // 支払い成功後のリダイレクト先
    @GetMapping("/changepaid/success")
    public String changePaidSuccess(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes) {
        userService.updatePaid(userDetailsImpl.getUser().getId());
        redirectAttributes.addFlashAttribute("successMessage", "会員ステータスを変更しました。最新の会員情報を確認するには、再ログインをお願いします。");
        return "redirect:/login";
    }

    // 支払いキャンセル後のリダイレクト先
    @GetMapping("/changepaid/cancel")
    public String changePaidCancel(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "支払いがキャンセルされました。");
        return "redirect:/user/changepaid";
    }

    // 有料→無料変更画面に遷移
    @GetMapping("/changefree")
    public String changefree(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        UserEditPaidForm up = new UserEditPaidForm(userDetailsImpl.getUser().getId());
        model.addAttribute("userEditPaidForm", up);
        return "user/changefree";
    }

    // 有料→無料会員ステータス変更
    @PostMapping("/editfree")
    public String editFree(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                           @ModelAttribute @Validated UserEditPaidForm userEditPaidForm,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/changefree";
        }

        userService.updatePaid(userDetailsImpl.getUser().getId());
        redirectAttributes.addFlashAttribute("successMessage", "会員ステータスを変更しました。最新の会員情報を確認するには、再ログインをお願いします。");
        return "redirect:/user";
    }

    @GetMapping("/company")
    public String company() {
        return "auth/company";
    }

    @GetMapping("/subscription")
    public String subscription() {
        return "user/subscription";
    }
}
