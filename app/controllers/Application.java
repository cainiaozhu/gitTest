package controllers;

import java.io.File;

import org.mindrot.jbcrypt.BCrypt;

import models.User;
import play.*;
import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
    	String email = session("email");
    	if (email != null){
    		return ok(email);
    	} else {
    		return ok("not login");
    	}
    }
    
    public static Result bcrypt() {
    	String passwordHash = BCrypt.hashpw("Hello", BCrypt.gensalt());
    	boolean correct = BCrypt.checkpw("Hello", passwordHash);
    	boolean wrong = BCrypt.checkpw("World", passwordHash);
    	return ok(passwordHash + " " + correct + " " + wrong);
    }

    public static class Registration {
    	@Email
    	public String email;
    	@Required
    	public String password;
    }
    
    public static Result register() {
    	Form<Registration> userForm = Form.form(Registration.class);
    	return ok(register.render(userForm));
    }
    
    public static Result postRegister() {
    	Form<Registration> userForm = Form.form(Registration.class).bindFromRequest();
    	User user = new User(userForm.get().email, userForm.get().password);
    	user.save();
    	return ok("registered");
    }
    
    public static class Login {
    	@Email
    	public String email;
    	@Required
    	public String password;
    	
    	public String validate() {
    		if (User.authenticate(email, password) == null) {
    			return "Invalid user or password";
    		} 
    		return null;
    	}
    }
    
    public static Result login() {
    	Form<Login> userForm = Form.form(Login.class);
    	return ok(login.render(userForm));
    }
    
    public static Result postLogin() {
    	Form<Login> userForm = Form.form(Login.class).bindFromRequest();
    	if (userForm.hasErrors()) {
    		return badRequest("Wrong user/password");
    	} else {
    		session().clear();
    		session("email", userForm.get().email);
    		return redirect("/");
    	}
    }
    
    public static Result uploadForm() {
    	return ok(upload.render());
    }
    
    public static Result upload() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart picture = body.getFile("picture");
        if (picture != null) {
          String fileName = picture.getFilename();
          String contentType = picture.getContentType(); 
          File file   = picture.getFile();
          // get the root path of the Play project
          File root = Play.application().path();
          // save file to the disk
          file.renameTo(new File(root, "/public/uploads/" + fileName));
          return ok(fileName + " " + contentType + " uploaded");
        } else {
          return badRequest("not a valid file");    
        }
  }
}
