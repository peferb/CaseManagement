package se.plushogskolan.casemanagement.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

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
import se.plushogskolan.casemanagement.model.Team;
import se.plushogskolan.casemanagement.model.User;
import se.plushogskolan.casemanagement.model.WorkItem;
import se.plushogskolan.casemanagement.repository.IssueRepository;
import se.plushogskolan.casemanagement.repository.TeamRepository;
import se.plushogskolan.casemanagement.repository.UserRepository;
import se.plushogskolan.casemanagement.repository.WorkItemRepository;

@RunWith(MockitoJUnitRunner.class)
public final class TestCaseServiceExceptionHandling {
	private User user;
	private Team team;
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
    	team = Team.builder().setId(100).build("Team name");
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
    public void inactivateUserByIdShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        doThrow(RepositoryException.class).when(userRepository).inactivateUserById(user.getId());;
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not inactivate User with id " + user.getId());
        caseService.inactivateUserById(user.getId());
    }
    
    @Test
    public void activateUserByIdShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        doThrow(RepositoryException.class).when(userRepository).activateUserById(user.getId());;
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not activate User with id " + user.getId());
        caseService.activateUserById(user.getId());
    }
    
    @Test
    public void getUserByIdShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        doThrow(RepositoryException.class).when(userRepository).getUserById(user.getId());;
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not get User by id " + user.getId());
        caseService.getUserById(user.getId());
    }
    
    @Test
    public void searchUsersByShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        doThrow(RepositoryException.class).when(userRepository).searchUsersBy(user.getFirstName(), user.getLastName(), user.getUsername());
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not get User by first name, last name, username. " 
        		+ user.getFirstName() + ", " + user.getLastName() + ", " + user.getUsername());
        caseService.searchUsersBy(user.getFirstName(), user.getLastName(), user.getUsername());
    }
    
    @Test
    public void updateUsernameWith9CharsUsernameShouldThrowServiceException() throws RepositoryException{
    	String toShortUsername = ">10chars";
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Username not long enough. Username was " + toShortUsername);
        caseService.updateUserUsername(user.getId(), toShortUsername);
    }
    
    @Test
    public void getUsersByTeamIdShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not get User by TeamId, teamId=" + team.getId());
        doThrow(RepositoryException.class).when(userRepository).getUsersByTeamId(team.getId());
        caseService.getUsersByTeamId(team.getId());
    }
    
    @Test
    public void saveTeamShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not save Team: " + team.toString());
        doThrow(RepositoryException.class).when(teamRepository).saveTeam(team);
        caseService.saveTeam(team);
    }
    
    @Test
    public void updateTeamShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not update Team with id " + team.getId());
        doThrow(RepositoryException.class).when(teamRepository).updateTeam(team);
        caseService.updateTeam(team);
    }
    
    @Test
    public void inactivateTeamShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not inactivate Team with id " + team.getId());
        doThrow(RepositoryException.class).when(teamRepository).inactivateTeam(team.getId());
        caseService.inactivateTeam(team.getId());
    }
    
    @Test
    public void activateTeamShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not activate Team with id " + team.getId());
        doThrow(RepositoryException.class).when(teamRepository).activateTeam(team.getId());
        caseService.activateTeam(team.getId());
    }
    
    @Test
    public void getAllTeamsShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Could not get all Teams");
        doThrow(RepositoryException.class).when(teamRepository).getAllTeams();
        caseService.getAllTeams();
    }
    
    @Test
    public void addUserToTeamShouldCatchRepositoryExceptionAndThrowServiceException() throws RepositoryException{
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("No space in team for user. userId = " + user.getId() + ", teamId = " + team.getId());
        doThrow(RepositoryException.class).when(userRepository).getUsersByTeamId(team.getId());
        caseService.addUserToTeam(user.getId(), team.getId());
    }

    @Test
    public void addingEleventhUserToTeamShouldThrowException() throws RepositoryException{
    	int teamId = 100;
    	List<User> tenUsersInTeam = new ArrayList();
    	for (int i = 0; i < 10; i++) tenUsersInTeam.add(User.builder().setId(i).setTeamId(teamId).build("Bulk user"));
    	when(userRepository.getUsersByTeamId(teamId)).thenReturn(tenUsersInTeam);
        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("No space in team for user. userId = " + user.getId() + ", teamId = " + teamId);
    	caseService.addUserToTeam(user.getId(), teamId);
    }

    @Test
    public void addingSixthWorkItemToUserShouldThrowException() throws RepositoryException {
        List<WorkItem> fiveWorkItems = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            fiveWorkItems.add(WorkItem.builder().setId(i).setUserId(user.getId()).setDescription("Bulk WorkItem").build());
        }
        when(workItemRepository.getWorkItemsByUserId(user.getId())).thenReturn(fiveWorkItems);
        when(userRepository.getUserById(user.getId())).thenReturn(user);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Could not add work item to user, either user is inactive or there is no space " +
                "for additional work items");
        
        int workItemId = 6;
        caseService.addWorkItemToUser(workItemId, user.getId());
    }
}
