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
		// メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
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

	//無料→有料会員変更画面に遷移
	@GetMapping("/changepaid")
	//ログイン中のユーザー情報をメソッドの引数で受け取る
	public String changepaid(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, HttpServletRequest httpServletRequest, Model model) {
		//最新のユーザー情報を取得
		//User user = userRepository.findUserById(userDetailsImpl.getUser().getId());
		UserEditPaidForm up = new UserEditPaidForm(userDetailsImpl.getUser().getId());
		String sessionId = stripeService.createStripeSession(userDetailsImpl.getUser().getId().toString(), httpServletRequest );
		//		model.addAttribute("user", user);
		model.addAttribute("userEditPaidForm", up);
		model.addAttribute("sessionId", sessionId);
		return "user/changepaid";

	}

	//有料→無料変更画面に遷移
	@GetMapping("/changefree")
	//ログイン中のユーザー情報をメソッドの引数で受け取る
	public String changefree(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		//最新のユーザー情報を取得
		//User user = userRepository.findUserById(userDetailsImpl.getUser().getId());
		UserEditPaidForm up = new UserEditPaidForm(userDetailsImpl.getUser().getId());

		//			model.addAttribute("user", user);
		model.addAttribute("userEditPaidForm", up);

		return "user/changefree";
	}

	//無料→有料会員ステータス変更
	@PostMapping("/editpaid")
    public String editPaid(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @ModelAttribute @Validated UserEditPaidForm userEditPaidForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "user/changepaid";
        }

        // 参照IDを生成
        String referenceId = userService.generateReferenceId();
        
        // ユーザ情報を更新
        userService.updatePaid(userDetailsImpl.getUser().getId());

        // 成功メッセージをフラッシュ属性に設定
        redirectAttributes.addFlashAttribute("successMessage", "会員ステータスを変更しました。決済画面に進んでください。");

        // 決済ページへのURLを生成
        String subscriptionUrl = "https://example.com/subscription?referenceId=" + referenceId;

        // 決済ページURLにリダイレクト
        return "redirect:" + subscriptionUrl;
    }

	//有料→無料会員ステータス変更
		@PostMapping("/editfree")
		public String editFree(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
				@ModelAttribute @Validated UserEditPaidForm userEditPaidForm,
				BindingResult bindingResult,
				Model model,
				RedirectAttributes redirectAttributes) {

			if (bindingResult.hasErrors()) {
				return "user/changepaid";
			}

			// 会員ステータスを更新
			userService.updatePaid(userDetailsImpl.getUser().getId());

			// 成功メッセージをフラッシュ属性に設定
			redirectAttributes.addFlashAttribute("successMessage", "会員ステータスを変更しました。最新の会員情報を確認するには、再ログインをお願いします。");
			
			// 決済ページURLにリダイレクト
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
