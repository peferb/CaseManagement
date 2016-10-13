package se.plushogskolan.casemanagement.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.sql.SQLDataException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import se.plushogskolan.casemanagement.exception.RepositoryException;
import se.plushogskolan.casemanagement.exception.ServiceException;
import se.plushogskolan.casemanagement.model.User;
import se.plushogskolan.casemanagement.repository.IssueRepository;
import se.plushogskolan.casemanagement.repository.TeamRepository;
import se.plushogskolan.casemanagement.repository.UserRepository;
import se.plushogskolan.casemanagement.repository.WorkItemRepository;

@RunWith(MockitoJUnitRunner.class)
public final class TestCaseServiceExceptionHandling {
	User user;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private WorkItemRepository workItemRepository;
    @Mock
    private IssueRepository issueRepository;
    @InjectMocks
    private CaseService caseService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setup(){
    	user = User.builder().setId(1001).build("Cool username");
    }
    
    @Test
    public void saveUserShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        doThrow(RepositoryException.class).when(userRepository).saveUser(user);
        expectedException.expect(ServiceException.class);
        caseService.saveUser(user);
    }
    
    @Test
    public void updateUserFirstNameShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
    	String newFirstName = "New first name";
        doThrow(RepositoryException.class).when(userRepository).getUserById(user.getId());
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not update user with id: " + user.getId() + ", new first name: " + newFirstName);
        caseService.updateUserFirstName(user.getId(), newFirstName);
    }
    
    @Test
    public void updateUserLastNameShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
    	String newLastName = "New last name";
        doThrow(RepositoryException.class).when(userRepository).getUserById(user.getId());
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not update user with id: " + user.getId() + ", new last name: " + newLastName);
        caseService.updateUserLastName(user.getId(), newLastName);
    }
    
    @Test
    public void updateUsernameShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
    	String newUsername = "New username";
        doThrow(RepositoryException.class).when(userRepository).getUserById(user.getId());
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not update user with id: " + user.getId() + ", new username: " + newUsername);
        caseService.updateUserUsername(user.getId(), newUsername);
    }
    
    @Test
    public void updateUserameShouldThrowServiceException() throws RepositoryException{
    	String toShortUsername = ">10chars";
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Username not long enough. Username was " + toShortUsername);
        caseService.updateUserUsername(user.getId(), toShortUsername);
    }
}
