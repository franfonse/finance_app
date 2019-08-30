package com.franfonse.app.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import com.franfonse.app.model.DaoDocument;
import com.franfonse.app.model.DaoExpense;
import com.franfonse.app.model.DaoIncome;
import com.franfonse.app.model.DaoUser;
import com.franfonse.app.model.Document;
import com.franfonse.app.model.Expense;
import com.franfonse.app.model.Income;
import com.franfonse.app.model.User;

@Controller
@SessionAttributes("iduser")
public class MainController {

	@Autowired
	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private DaoUser daoUser;

	@Autowired
	private DaoDocument daoDocument;

	@Autowired
	private DaoIncome daoIncome;
	
	@Autowired
	private DaoExpense daoExpense;

	
	@GetMapping("/")
	public String home(ModelMap model, SessionStatus status) {
		
		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			User user = daoUser.findById((Long) model.get("iduser")).get();
			model.addAttribute("documents", user.getListDocuments());
			
			return "documents";
		}
	}

	@GetMapping("/login")
	public String login(ModelMap model, @RequestParam String email, @RequestParam String password) {
		
		User user = daoUser.findByEmail(email);
		
		if (user == null || !passwordEncoder().matches(password, user.getPassword())) {
			model.addAttribute("failLogin", "Username or password invalid.");
			
			return "homepage";
		} else {
			
			model.addAttribute("documents", user.getListDocuments());
			model.put("iduser", user.getIdUser());

			return "documents";
		}
	}

	@GetMapping("/newaccount")
	public String toRegister(ModelMap model) {
		
		User user = new User();
		model.addAttribute("user", user);

		return "register";
	}

	@PostMapping("/register")
	public String register(ModelMap model, User user, @RequestParam(required = false) String password2) {
		
		if (daoUser.findByEmail(user.getEmail()) != null) {
			model.addAttribute("registered", "The chosen email has already been used for an existing account.");

			return "register";

		} else if (daoUser.findByUsername(user.getUsername()) != null) {
			model.addAttribute("takenUsername", "This username is already taken. Please, choose a different one.");

			return "register";

		} else if (!user.getPassword().equals(password2)) {
			model.addAttribute("wrongpass", "Both passwords must match.");

			return "register";

		} else {
			if (user.getEmail().equals("franfonseca8594@gmail.com")) {
				Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
				List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
				roles.add(new SimpleGrantedAuthority("ADMIN"));
				user.setRoles(roles);
				user.setPassword(passwordEncoder().encode(user.getPassword()));
				user.setDate(currentDate);
				daoUser.save(user);

				return "homepage";
			} else {
				Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
				List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
				roles.add(new SimpleGrantedAuthority("USER"));
				user.setRoles(roles);
				user.setPassword(passwordEncoder().encode(user.getPassword()));
				user.setDate(currentDate);
				daoUser.save(user);

				return "homepage";
			}
		}
	}
	
	@GetMapping("/myprofile")
	public String myAccount(ModelMap model, @SessionAttribute(name = "iduser", required = false) Long iduser, 
			SessionStatus status) {
		
		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			User user = daoUser.findById(iduser).get();
			model.addAttribute("user", user);
			
			return "myprofile";
		}
	}

	@GetMapping("/newdocument")
	public String toNewDocument(ModelMap model, @SessionAttribute(name = "iduser", required = false) Long iduser, 
			SessionStatus status) {

		if (model.get("iduser") == null || status.isComplete()) {
			return "homepage";
		} else {

			return "newdocument";
		}
	}

	@PostMapping("/create_document")
	public String addDocument(ModelMap model, SessionStatus status, @SessionAttribute(name = "iduser", required = false) Long iduser,
			@RequestParam String title, @RequestParam int year, @RequestParam int month) {
		
		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			User user = daoUser.findById(iduser).get();
			Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(year, month, 1);
			Date yearMonth = new Date(cal.getTimeInMillis());
			Document doc = new Document(user, title, currentDate, yearMonth);
			if (user.getListDocuments() == null) {
				List<Document> listDocuments = new ArrayList<Document>();
				user.setListDocuments(listDocuments);
				listDocuments.add(doc);
				daoDocument.save(doc);
				model.addAttribute("documents", user.getListDocuments());

				return "documents";
			} else {
				user.getListDocuments().add(doc);
				daoDocument.save(doc);
				model.addAttribute("documents", user.getListDocuments());

				return "documents";
			}
		}
	}

	@GetMapping("/viewdocument")
	public String viewDocument(ModelMap model, @SessionAttribute(name = "iduser", required = false) Long iduser, SessionStatus status,
			long idDocument) {
		
		if (model.isEmpty() == true || status.isComplete()) {
			
			return "homepage";
		} else {
			Document doc = daoDocument.findById(idDocument).get();
			model.addAttribute("doc", doc);
			SimpleDateFormat sdf = new SimpleDateFormat("'('MMMM ' of ' yyyy')'", Locale.ENGLISH);
			model.addAttribute("yearMonth", sdf.format(doc.getMonth()));

			return "document";
		}
	}
	
	@GetMapping("/new_expense")
	public String newExpense(ModelMap model, @SessionAttribute(name = "iduser", required = false) Long iduser, SessionStatus status, 
			@RequestParam long idDocument) {
		
		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			Document doc = daoDocument.findById(idDocument).get();
			Expense exp = new Expense();
			
			model.addAttribute("idDocument", doc.getIdDocument());
			model.addAttribute("exp", exp);
			
			return "newexpense";
		}
	}
	
	@PostMapping("/add_expense")
	public String addExpense(ModelMap model, @SessionAttribute(name = "iduser", required = false) Long iduser, SessionStatus status, 
			Expense exp, long idDocument, @RequestParam int day) {
		
		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			Document doc = daoDocument.findById(idDocument).get();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(doc.getMonth().getTime());
			cal.add(Calendar.DAY_OF_MONTH, day - 1);
			Date expenseDate = new Date(cal.getTimeInMillis());
			exp.setDate(expenseDate);
			exp.setDocument(doc);
			
			if (doc.getExpenses() == null) {
				List<Expense> expenses = new ArrayList<Expense>();
				expenses.add(exp);
				doc.setExpenses(expenses);
			} else {
				doc.getExpenses().add(exp);
			}
			daoExpense.save(exp);
			doc.setBalance(doc.getBalance()-exp.getValue());
			daoDocument.save(doc);
			SimpleDateFormat sdf = new SimpleDateFormat("'('MMMM ' of ' yyyy')'", Locale.ENGLISH);
			model.addAttribute("doc", doc);
			model.addAttribute("yearMonth", sdf.format(doc.getMonth()));
			
			return "document";
		}
	}
	
	@GetMapping("/delete_expense")
	public String deleteExpense(ModelMap model, SessionStatus status, @SessionAttribute(name = "iduser", required = false) Long iduser, 
			@RequestParam long idExpense) {
		
		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			Expense exp = daoExpense.findById(idExpense).get();
			Document doc = exp.getDocument();
			doc.setBalance(doc.getBalance()+exp.getValue());
			daoExpense.delete(exp);
			daoDocument.save(doc);
			SimpleDateFormat sdf = new SimpleDateFormat("'('MMMM ' of ' yyyy')'", Locale.ENGLISH);
			model.addAttribute("doc", doc);
			model.addAttribute("yearMonth", sdf.format(doc.getMonth()));
			
			return "document";
		}
	}

	@GetMapping("/new_income")
	public String newIncome(ModelMap model, @SessionAttribute(name = "iduser", required = false) Long iduser, SessionStatus status,
			@RequestParam long idDocument) {

		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			Document doc = daoDocument.findById(idDocument).get();
			Income inc = new Income();
			
			model.addAttribute("idDocument", doc.getIdDocument());
			model.addAttribute("inc", inc);

			return "newincome";
		}
	}

	@PostMapping("/add_income")
	public String addIncome(ModelMap model, @SessionAttribute(name = "iduser", required = false) Long iduser, SessionStatus status,
			Income inc, long idDocument, @RequestParam int day) {

		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			Document doc = daoDocument.findById(idDocument).get();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(doc.getMonth().getTime());
			cal.add(Calendar.DAY_OF_MONTH, day - 1);
			Date incomeDate = new Date(cal.getTimeInMillis());
			inc.setDate(incomeDate);
			inc.setDocument(doc);
			
			if (doc.getIncomes() == null) {
				
				List<Income> incomes = new ArrayList<Income>();
				incomes.add(inc);
				doc.setIncomes(incomes);
			} else {
				doc.getIncomes().add(inc);
			}
			daoIncome.save(inc);
			doc.setBalance(doc.getBalance()+inc.getValue());
			daoDocument.save(doc);
			SimpleDateFormat sdf = new SimpleDateFormat("'('MMMM ' of ' yyyy')'", Locale.ENGLISH);
			model.addAttribute("doc", doc);
			model.addAttribute("yearMonth", sdf.format(doc.getMonth()));
			
			return "document";
		}
	}
	
	@GetMapping("/delete_income")
	public String deleteIncome(ModelMap model, SessionStatus status, @SessionAttribute(name = "iduser", required = false) Long iduser, 
			@RequestParam long idIncome) {
		
		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			Income inc = daoIncome.findById(idIncome).get();
			Document doc = inc.getDocument();
			doc.setBalance(doc.getBalance()-inc.getValue());
			daoIncome.delete(inc);
			daoDocument.save(doc);
			SimpleDateFormat sdf = new SimpleDateFormat("'('MMMM ' of ' yyyy')'", Locale.ENGLISH);
			model.addAttribute("doc", doc);
			model.addAttribute("yearMonth", sdf.format(doc.getMonth()));
			
			return "document";
		}
	}

	@GetMapping("/mydocuments")
	public String toMyDocuments(ModelMap model, @SessionAttribute(name = "iduser", required = false) Long iduser, SessionStatus status) {

		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			model.addAttribute("documents", daoUser.findById(iduser).get().getListDocuments());

			return "documents";
		}
	}

	@DeleteMapping("/delete_document")
	public String deleteDoc(ModelMap model, @SessionAttribute(name = "iduser", required = false) Long iduser, 
			SessionStatus status) {

		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			// delete an existing document.
			return "documents";
		}
	}

	@DeleteMapping("/delete_account")
	public String deleteAccount(ModelMap model, SessionStatus status, 
			@SessionAttribute(name = "iduser", required = false) Long iduser) {

		if (model.get("iduser") == null || status.isComplete()) {
			
			return "homepage";
		} else {
			User user = daoUser.findById(iduser).get();
			daoDocument.deleteAll(user.getListDocuments());
			daoUser.delete(user);

			return "homepage";
		}
	}

	@GetMapping("/logout-success")
	public String logoff(ModelMap model, SessionStatus status) {

		status.setComplete();
		model.clear();

		return "homepage";
	}
}