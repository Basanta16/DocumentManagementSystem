package com.diginepal.DMS.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.diginepal.DMS.entity.User;
import com.diginepal.DMS.helper.Message;
import com.diginepal.DMS.repo.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;

	@RequestMapping("/")
	public String Home(Model model) {

		model.addAttribute("title", "DMS|Home");
		return "home";

	}

	@RequestMapping("/about")
	public String about(Model model) {

		model.addAttribute("title", "DMS|About");
		return "about";

	}

	@RequestMapping("/signup")
	public String signup(Model model) {

		model.addAttribute("title", "DMS|Signup");
		model.addAttribute("user", new User());
		return "signup";
		}
	
	@GetMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title", "DMS|Login");
		return "login";
	}

	
	  @RequestMapping(value="/do_register", method=RequestMethod.POST) 
	  
	  public String registerUser (@ModelAttribute("user") User
	  user, @RequestParam(value="agreement", defaultValue = "false") boolean
	  agreement, Model model,  HttpSession session ) 
	  { 
		 try {
			 if (!agreement) {
				  System.out.println("you have not agreed terms and conditon......");
				  throw new Exception("You have not agreed terms and conditon......");
				
			 	}
			 
			 
			  user.setRole("ROLE_ADMIN");
			  user.setEnabled(true);
			  user.setImageUrl("default.png");
			  user.setPassword(passwordEncoder.encode(user.getPassword()));
			  
			  System.out.println("agreement "+agreement);
			  System.out.println("USER "+user );
			  
			 User saveUser = this.userRepo.save(user);
			  model.addAttribute("user", new User());
			  session.setAttribute("message", new Message("Successfully Registered!! " , "alert-success"));
			  return "signup"; 
			 
		} catch (Exception e) {
			
			e.printStackTrace();
			 model.addAttribute("user", user);
			 session.setAttribute("message", new Message("Something Went Wrong!!! "+e.getMessage() , "alert-danger"));
			 return "signup"; 
		}
	  }	 
	   
}
