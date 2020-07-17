/**
 * 
 */
package com.blueoptima.project.utility;

import com.blueoptima.project.model.UserProfileInput;
import com.blueoptima.project.model.UserProfileOutput;

/**
 * @author jghosh
 *an interface to update the user github profile output object to data store
 */
public interface IPostUserToDataStore {
	
	public void addUserToDataStore(UserProfileOutput userProfile, UserProfileInput user);

}
