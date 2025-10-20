package [packageName].repository;

import [packageName].model.User;
import [packageName].util.IBase;

public interface UserRepository extends IBase<User> {
	
	public User findByUserName(String userName, String password);

}
