package com.diginepal.DMS.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.diginepal.DMS.entity.Customer;
import com.diginepal.DMS.entity.User;
import com.diginepal.DMS.helper.Message;
import com.diginepal.DMS.repo.CustomerRepository;
import com.diginepal.DMS.repo.UserRepository;

@Controller
@RequestMapping("/admin")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("username "+userName);
		
		User user = userRepository.getUserByUserName(userName);
		System.out.println("User: "+user);
		model.addAttribute("user",user);
	}
	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		
		model.addAttribute("title","DMS|Admin panel");
		return "admin/user_dashboard";
	}
	
	@GetMapping("/addDetails")
	public String addDetails(Model model) {
		
		model.addAttribute("title","DMS|Add Details");
		model.addAttribute("customer", new Customer());
		return "admin/add_detail";
	}
	
	@PostMapping("/processDetails")
	public String processDetail(@ModelAttribute Customer customer, 
			@RequestParam("profileImage")MultipartFile ppImgFile,
			 Principal principal, HttpSession session) {
		
		try {
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
		
		
		//uploading pp image
		if (ppImgFile.isEmpty()) 
		{
			System.out.println("File is empty");
			customer.setImage("android-contact.png");
			
		}
		else
		{
			customer.setImage(ppImgFile.getOriginalFilename());
		File saveFile = new ClassPathResource("/static/img/").getFile();
		
	Path path =	Paths.get(saveFile.getAbsolutePath()+File.separator+ppImgFile.getOriginalFilename());
		Files.copy(ppImgFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		System.out.println("image is uploaded");
		}
		
		/*
		 * //uploading citizenship if (citiImgFile.isEmpty()) {
		 * System.out.println("File is empty");
		 * 
		 * } else { customer.setImage(citiImgFile.getOriginalFilename()); File saveFile
		 * = new ClassPathResource("/static/img/").getFile();
		 * 
		 * Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+citiImgFile.
		 * getOriginalFilename()); Files.copy(citiImgFile.getInputStream(), path,
		 * StandardCopyOption.REPLACE_EXISTING);
		 * System.out.println("citizenship is uploaded"); }
		 */
		
		
		customer.setUser(user);
		
		user.getCustomers().add(customer);
		
		this.userRepository.save(user);
		System.out.println("Data "+customer);
		System.out.println("Added to database");
		
		session.setAttribute("message", new Message("The Details are added!!!", "success"));
		}
		
		catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Whoops! Something went wrong!!!", "danger"));
		}
		return "admin/add_detail";
	}
	
	@GetMapping("/viewDetails/{page}")
	public String viewDetails(@PathVariable("page") Integer page,Model model, Principal principal)
	{
		model.addAttribute("title","DMS|View Details");
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		 	
		Pageable pageable = PageRequest.of(page, 5);
		Page<Customer> customers = this.customerRepository.findCustomersByUser(user.getId(), pageable);
		
		model.addAttribute("customers", customers);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", customers.getTotalPages());
		
		return "admin/view_detail";
	}
	
	@RequestMapping("/{cid}/customer")
	public String showDetails(@PathVariable("cid") Integer cid, Model model, Principal principal)
	{
		
		System.out.println("cid: "+cid);
		
		Optional<Customer> customerOp =	this.customerRepository.findById(cid);
		Customer customer = customerOp.get();
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		if(user.getId() == customer.getUser().getId())
		{
			model.addAttribute("customer", customer);
			model.addAttribute("title","DMS|Show Details");
		}
		
		
		return "admin/show_detail";
	}
	
	/**
	 * @param cid
	 * @param model
	 * @param principal
	 * @param session
	 * @return
	 */
	@GetMapping("/delete/{cid}")
	public String deleteDetails(@PathVariable("cid") Integer cid,
			Model model, Principal principal, HttpSession session)
	{
		Customer customer = this.customerRepository.findById(cid).get();
		 
		User user = this.userRepository.getUserByUserName(principal.getName());
		user.getCustomers().remove(customer);
		this.userRepository.save(user);
		
		session.setAttribute("message", new Message("The details have been removed..", "success"));
		
		return "redirect:/admin/viewDetails/0";
	}
	
	@PostMapping("/updateDetails/{cid}")
	public String updateDetails(@PathVariable("cid") Integer cid, Model model) {
		
		model.addAttribute("title","DMS|Update Details");
	Customer customer =	this.customerRepository.findById(cid).get();
	model.addAttribute("customer",customer);
		return "admin/update_detail";
	}
	
	@RequestMapping(value="/processUpdate", method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute Customer customer,
			@RequestParam("profileImage")MultipartFile ppImgFile,
			Model model, HttpSession session, Principal principal) 
	{
		try {
			Customer oldcustomer = this.customerRepository.findById(customer.getCid()).get();
			
			if (!ppImgFile.isEmpty()) 
			{
				//deletion of old photo
				File deleteFile = new ClassPathResource("/static/img/").getFile();
				File file1 = new File(deleteFile, oldcustomer.getImage());
				file1.delete();
				
				//updating new photo
				File saveFile = new ClassPathResource("/static/img/").getFile();
				
				Path path =	Paths.get(saveFile.getAbsolutePath()+File.separator+ppImgFile.getOriginalFilename());
					Files.copy(ppImgFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					customer.setImage(ppImgFile.getOriginalFilename());
			}
			else {
				customer.setImage(oldcustomer.getImage());
			}
			User user = this.userRepository.getUserByUserName(principal.getName());
			customer.setUser(user); 
			this.customerRepository.save(customer);
			session.setAttribute("message", new Message("The details have been updated..", "success"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "redirect:/admin/"+ customer.getCid()+"/customer/";
	}
	
	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		
		
		model.addAttribute("title","DMS|View Profile");
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		model.addAttribute("user",user);
		
		return "admin/profile";
	}
}
