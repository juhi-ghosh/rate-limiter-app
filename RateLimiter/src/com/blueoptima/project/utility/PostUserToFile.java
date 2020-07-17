package com.blueoptima.project.utility;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.blueoptima.project.model.UserProfileInput;
import com.blueoptima.project.model.UserProfileOutput;

public class PostUserToFile implements IPostUserToDataStore {

	//add the UserProfileOutput object contents to a file created in the name of User’s name and location given as input
	@Override
	public void addUserToDataStore(UserProfileOutput userProfile, UserProfileInput user) {

		String fileName = "";

		if (Validator.isNull(user.getLocation()))
			fileName = String.format("output/%1$s_%2$s.json", user.getFirstName(), user.getLastName());
		else
			fileName = String.format("output/%1$s_%2$s_%3$s.json", user.getFirstName(), user.getLastName(),
					user.getLocation());

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));) {
			writer.write(userProfile.getPublicProfileData());
			writer.write(String.format("%1$s{ ", Constants.NEWLINE));

			userProfile.getUserRepoData().forEach((repo, noOfCommits) -> {
				try {
					writer.write(String.format("%1$s:%2$s, ", repo, noOfCommits));
				} catch (IOException e) {
					System.out.println(String.format("%1$s %2$s", Constants.ERROR_WRITING_USER_FILE, e));
				}
			});
			writer.write(String.format("}%1$s", Constants.NEWLINE));

		} catch (FileNotFoundException e) {
			System.out.println(String.format("%1$s %2$s", Constants.ERROR_FILE_NOT_CREATED, e));
		} catch (IOException e) {
			System.out.println(String.format("%1$s %2$s", Constants.ERROR_WRITING_USER_FILE, e));
		}
	}
}
