package se.plushogskolan.casemanagement.service;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.plushogskolan.casemanagement.exception.RepositoryException;
import se.plushogskolan.casemanagement.exception.ServiceException;
import se.plushogskolan.casemanagement.model.Issue;
import se.plushogskolan.casemanagement.model.Team;
import se.plushogskolan.casemanagement.model.User;
import se.plushogskolan.casemanagement.model.WorkItem;
import se.plushogskolan.casemanagement.repository.IssueRepository;
import se.plushogskolan.casemanagement.repository.TeamRepository;
import se.plushogskolan.casemanagement.repository.UserRepository;
import se.plushogskolan.casemanagement.repository.WorkItemRepository;
import se.plushogskolan.casemanagement.service.CaseService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class TestCaseService {
    private User user;
    private User extraUser;
    private WorkItem unstartedWorkItem;
    private WorkItem workItemStartedByUser;
    private WorkItem workItemDoneByExtraUser;
    private List<WorkItem> unstartedWorkItems;
    private List<WorkItem> workItemsStartedByUser;
    private List<WorkItem> workItemsDoneByExtraUser;
    private List<User> users;
    private Team team;
    private Team extraTeam;
    private List<Team> teams;
    private Issue issueToWorkItemStartedByUser;
    private Issue issueToWorkItemDoneByExtraUser;
    private Issue extraIssueToWorkItem;
    private List<Issue> issuesToWorkItem;

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
        setupUsers();
        setupTeams();
        setupWorkItems();
        setupIssues();
    }

    private void setupUsers() {
        int userId = 1001;
        String username = "Tester_Testsson";
        String firstName = "Test";
        String lastName = "Testsson";
        user = User.builder().setFirstName(firstName).setLastName(lastName).setActive(true).setId(userId).setTeamId(1)
                .build(username);
        extraUser = User.builder().setFirstName(firstName).setLastName(lastName).setId(1002).build(username);
        users = new ArrayList<>();
        users.add(user);
        users.add(extraUser);
    }

    private void setupTeams() {
        team = Team.builder().setId(1).setActive(true).build("Testing Team of testing Team repo");
        extraTeam = Team.builder().setId(2).setActive(false).build("Extra Team used to fill list of Team");
        teams = new ArrayList<>();
        teams.add(team);
        teams.add(extraTeam);
    }

    private void setupWorkItems() {
        // WorkItems should be built with status Unstarted by default
        unstartedWorkItem = WorkItem.builder().setId(1).setDescription("Test the CaseService").build();
        unstartedWorkItems = new ArrayList<>();
        unstartedWorkItems.add(unstartedWorkItem);

        workItemStartedByUser = WorkItem.builder().setId(2).setDescription("Test yourself").setUserId(user.getId())
                .setStatus(WorkItem.Status.STARTED).build();
        workItemsStartedByUser = new ArrayList<>();
        workItemsStartedByUser.add(workItemStartedByUser);

        workItemDoneByExtraUser = WorkItem.builder().setId(3).setDescription("Test the CaseService").
                setUserId(extraUser.getId()).setStatus(3).build();
        workItemsDoneByExtraUser = new ArrayList<>();
        workItemsDoneByExtraUser.add(workItemDoneByExtraUser);
    }

    private void setupIssues() {
        issueToWorkItemStartedByUser = Issue.builder(workItemStartedByUser.getId())
                .setDescription("WorkItem should have cooler name.").setId(1).build();

        extraIssueToWorkItem = Issue.builder(workItemStartedByUser.getId())
                .setDescription("WorkItem should be changed like this.").setId(2).build();

        issueToWorkItemDoneByExtraUser = Issue.builder(workItemDoneByExtraUser.getId())
                .setDescription("WorkItem is wrong this way.").setId(3).build();

        issuesToWorkItem = new ArrayList<>();
        issuesToWorkItem.add(issueToWorkItemStartedByUser);
        issuesToWorkItem.add(extraIssueToWorkItem);
    }

    @Test
    public void canSaveUser() throws RepositoryException {
        when(userRepository.getUserById(user.getId())).thenReturn(user);
        when(userRepository.getUsersByTeamId(user.getTeamId())).thenReturn(users);
        caseService.saveUser(user);
        verify(userRepository).saveUser(user);
    }

    @Test
    public void usernameMustBeTenCharactersLong() throws RepositoryException {
        String usernameWithNineChars = "Is<10char";

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("Username must be at least 10 characters long. Username was "
                + usernameWithNineChars);

        User userWithToShortName = User.builder().setFirstName(user.getFirstName()).setLastName(user.getLastName()).setId(user.getId())
                .build(usernameWithNineChars);

        caseService.saveUser(userWithToShortName);
    }

    @Test
    public void negativeUserIdShouldThrowException() throws RepositoryException {
        int negativeUserId = -1;
        User userWithNegativeId = User.builder().setFirstName(user.getFirstName()).setLastName(user.getLastName()).setId(negativeUserId)
                .build(user.getUsername());

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("User id must be positive. User id was "
                + negativeUserId);

        caseService.saveUser(userWithNegativeId);
    }

    @Test
    public void canInactivateUser() throws RepositoryException {
        List<WorkItem> workItems = new ArrayList<>();
        workItems.add(unstartedWorkItem);

        when(workItemRepository.getWorkItemsByUserId(user.getId())).thenReturn(workItems);

        caseService.inactivateUserById(user.getId());

        verify(userRepository).inactivateUserById(user.getId());
        verify(workItemRepository).updateStatusById(unstartedWorkItem.getId(), WorkItem.Status.UNSTARTED);
    }

    @Test
    public void canActivateUser() throws RepositoryException {

        caseService.activateUserById(user.getId());

        verify(userRepository).activateUserById(user.getId());
    }

    @Test
    public void canGetUserById() throws RepositoryException {
        when(userRepository.getUserById(user.getId())).thenReturn(user);

        User result = caseService.getUserById(user.getId());

        assertEquals(user, result);
    }

    @Test
    public void canGetUsersByTeamId() throws RepositoryException {
        int teamId = 1;
        when(userRepository.getUsersByTeamId(1)).thenReturn(users);

        List<User> result = caseService.getUsersByTeamId(teamId);

        assertEquals(users, result);
    }

    @Test
    public void canSaveTeam() throws RepositoryException {
        caseService.saveTeam(team);

        verify(teamRepository).saveTeam(team);
    }

    @Test
    public void canUpdateTeam() throws RepositoryException {
        caseService.updateTeam(team);

        verify(teamRepository).updateTeam(team);
    }

    @Test
    public void canActivateTeam() throws RepositoryException {
        caseService.activateTeam(team.getId());

        verify(teamRepository).activateTeam(team.getId());
    }

    @Test
    public void canInactivateTeam() throws RepositoryException {
        caseService.inactivateTeam(team.getId());

        verify(teamRepository).inactivateTeam(team.getId());
    }

    @Test
    public void canGetAllTeams() throws RepositoryException {
        when(teamRepository.getAllTeams()).thenReturn(teams);

        List<Team> teamsGotten = caseService.getAllTeams();

        assertEquals(teams, teamsGotten);
    }

    @Test
    public void canAddUserToTeam() throws RepositoryException {

        doNothing().when(teamRepository).addUserToTeam(user.getId(), team.getId());

        when(userRepository.getUserById(user.getId())).thenReturn(user);

        caseService.addUserToTeam(user.getId(), team.getId());

        verify(teamRepository, times(1)).addUserToTeam(user.getId(), team.getId());
    }

    @Test
    public void canSaveWorkItem() throws RepositoryException {
        caseService.saveWorkItem(unstartedWorkItem);

        verify(workItemRepository).saveWorkItem(unstartedWorkItem);
    }

    @Test
    public void canUpdateWorkItemStatus() throws RepositoryException {
        WorkItem.Status status = WorkItem.Status.STARTED;
        caseService.updateStatusById(unstartedWorkItem.getId(), status);

        verify(workItemRepository).updateStatusById(unstartedWorkItem.getId(), status);
    }

    @Test
    public void canDeleteWorkItem() throws RepositoryException {
        when(issueRepository.getIssuesByWorkItemId(unstartedWorkItem.getId())).thenReturn(issuesToWorkItem);

        caseService.deleteWorkItem(unstartedWorkItem.getId());

        verify(workItemRepository, times(1)).deleteWorkItemById(unstartedWorkItem.getId());

        verify(issueRepository).getIssuesByWorkItemId(unstartedWorkItem.getId());

        verify(issueRepository, times(1)).deleteIssue(issueToWorkItemStartedByUser.getId());

    }

    @Test
    public void canSaveIssue() throws RepositoryException {
        when(workItemRepository.getWorkItemById(issueToWorkItemStartedByUser.getWorkItemId())).thenReturn(workItemDoneByExtraUser);

        caseService.saveIssue(issueToWorkItemStartedByUser);
    }

    @Test
    public void canAddWorkItemToUser() throws RepositoryException {
        when(workItemRepository.getWorkItemsByUserId(user.getId())).thenReturn(workItemsStartedByUser);

        when(userRepository.getUserById(user.getId())).thenReturn(user);

        caseService.addWorkItemToUser(unstartedWorkItem.getId(), user.getId());

        verify(workItemRepository).addWorkItemToUser(unstartedWorkItem.getId(), user.getId());
    }

    @Test
    public void canGetWorkItemsByStatus() throws RepositoryException {
        when(workItemRepository.getWorkItemsByStatus(WorkItem.Status.UNSTARTED)).thenReturn(unstartedWorkItems);
        when(workItemRepository.getWorkItemsByStatus(WorkItem.Status.STARTED)).thenReturn(workItemsStartedByUser);
        when(workItemRepository.getWorkItemsByStatus(WorkItem.Status.DONE)).thenReturn(workItemsDoneByExtraUser);

        List<WorkItem> workItemsUnstarted = caseService.getWorkItemsByStatus(WorkItem.Status.UNSTARTED);
        List<WorkItem> workItemsStarted = caseService.getWorkItemsByStatus(WorkItem.Status.STARTED);
        List<WorkItem> workItemsDone = caseService.getWorkItemsByStatus(WorkItem.Status.DONE);

        verify(workItemRepository).getWorkItemsByStatus(WorkItem.Status.UNSTARTED);
        verify(workItemRepository).getWorkItemsByStatus(WorkItem.Status.STARTED);
        verify(workItemRepository).getWorkItemsByStatus(WorkItem.Status.DONE);

        assertEquals(unstartedWorkItems, workItemsUnstarted);
        assertEquals(workItemsStartedByUser, workItemsStarted);
        assertEquals(workItemsDoneByExtraUser, workItemsDone);
    }

    @Test
    public void canUpdateIssueDescription() throws RepositoryException {
        int issueId = 1001;
        Issue issueWithOldDescription = Issue.builder(1).setId(issueId).setDescription("It's like this, do that!").build();
        Issue issueWithNewDescription = Issue.builder(1).setId(issueId).setDescription("No, it's more like that actually. Do this instead.").build();
        when(issueRepository.getIssueById(issueId)).thenReturn(issueWithOldDescription);

        caseService.updateIssueDescription(issueId, issueWithNewDescription.getDescription());
        verify(issueRepository).updateIssue(issueWithNewDescription);
    }

    @Test
    public void canGetWorkItemsByTeamId() throws RepositoryException {
        when(workItemRepository.getWorkItemsByTeamId(team.getId())).thenReturn(workItemsStartedByUser);
        List<WorkItem> workItemsByTeamId = workItemRepository.getWorkItemsByTeamId(team.getId());
        assertEquals(workItemsStartedByUser, workItemsByTeamId);
    }

    @Test
    public void canGetWorkItemsByUserId() throws RepositoryException {
        when(workItemRepository.getWorkItemsByUserId(user.getId())).thenReturn(workItemsStartedByUser);
        List<WorkItem> workItemsByUserId = workItemRepository.getWorkItemsByUserId(user.getId());
        assertEquals(workItemsStartedByUser, workItemsByUserId);
    }

    @Test
    public void canGetWorkItemsWithIssue() throws RepositoryException {
        when(workItemRepository.getWorkItemsWithIssue()).thenReturn(workItemsStartedByUser);
        List<WorkItem> workItemsWithIssue = workItemRepository.getWorkItemsWithIssue();
        assertEquals(workItemsStartedByUser, workItemsWithIssue);
    }

    @Test
    public void canAssignIssueToWorkItem() throws RepositoryException {
        when(issueRepository.getIssueById(issueToWorkItemDoneByExtraUser.getId())).thenReturn(issueToWorkItemDoneByExtraUser);
        when(workItemRepository.getWorkItemById(workItemDoneByExtraUser.getId())).thenReturn(workItemDoneByExtraUser);

        caseService.assignIssueToWorkItem(issueToWorkItemDoneByExtraUser.getId(), workItemDoneByExtraUser.getId());

        verify(issueRepository).updateIssue(issueToWorkItemDoneByExtraUser);
        verify(workItemRepository).updateStatusById(workItemDoneByExtraUser.getId(), WorkItem.Status.UNSTARTED);
    }

    @Test
    public void addingSixthWorkItemToUserShouldThrowException() throws RepositoryException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Could not add work item to user, either user is inactive or there is no space " +
                "for additional work items");

        List<WorkItem> fiveWorkItems = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            fiveWorkItems.add(workItemStartedByUser);
        }
        when(workItemRepository.getWorkItemsByUserId(user.getId())).thenReturn(fiveWorkItems);
        when(userRepository.getUserById(user.getId())).thenReturn(user);

        caseService.addWorkItemToUser(workItemDoneByExtraUser.getId(), user.getId());
    }

    @Test
    public void canChangeUserFirstName() throws RepositoryException {
        String newName = "New First Name";
        User userWithNewFirstName = User.builder().setFirstName(newName).setLastName(user.getLastName())
                .setActive(user.isActive()).setId(user.getId()).build(user.getUsername());
        when(userRepository.getUserById(user.getId())).thenReturn(user);

        caseService.updateUserFirstName(user.getId(), newName);

        verify(userRepository).updateUser(userWithNewFirstName);
    }

    @Test
    public void canChangeUserLastName() throws RepositoryException {
        String newName = "New Last Name";
        User userWithNewLastName = User.builder().setFirstName(user.getFirstName()).setLastName(newName)
                .setActive(user.isActive()).setId(user.getId()).build(user.getUsername());
        when(userRepository.getUserById(user.getId())).thenReturn(user);

        caseService.updateUserLastName(user.getId(), newName);

        verify(userRepository).updateUser(userWithNewLastName);
    }

    @Test
    public void canChangeUsername() throws RepositoryException {
        String newName = "New Last Name";
        User userWithNewUsername = User.builder().setFirstName(user.getFirstName()).setLastName(user.getLastName())
                .setActive(user.isActive()).setId(user.getId()).build(newName);
        when(userRepository.getUserById(user.getId())).thenReturn(user);

        caseService.updateUserUsername(user.getId(), newName);

        verify(userRepository).updateUser(userWithNewUsername);
    }

    @Test
    public void canSearchUsersManyWays() throws RepositoryException {
        userRepository.searchUsersBy(user.getFirstName(), user.getLastName(), user.getUsername());
        verify(userRepository).searchUsersBy(user.getFirstName(), user.getLastName(), user.getUsername());

        userRepository.searchUsersBy(user.getFirstName(), user.getLastName(), "");
        verify(userRepository).searchUsersBy(user.getFirstName(), user.getLastName(), "");

        userRepository.searchUsersBy(null, user.getLastName(), "");
        verify(userRepository).searchUsersBy(null, user.getLastName(), "");
    }

    @Test
    public void addingEleventhUserToTeamShouldThrowException(){


        caseService.addUserToTeam(user.getId(), extraTeam.getId());
    }
}