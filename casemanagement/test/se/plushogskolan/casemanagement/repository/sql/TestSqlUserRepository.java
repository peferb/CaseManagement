package se.plushogskolan.casemanagement.repository.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import se.plushogskolan.casemanagement.exception.RepositoryException;
import se.plushogskolan.casemanagement.model.User;
import se.plushogskolan.casemanagement.repository.mysql.SqlUserRepository;

public class TestSqlUserRepository {

    private static User user;
    private final SqlUserRepository sqlUserRepository = new SqlUserRepository();

    @BeforeClass
    public static void init() {
        user = User.builder().setFirstName("juan").setLastName("deag").build("juan_deagle");

    }
    
    // TODO getUserByFirstNameLastNameUsernameTest()
    // TODO saveUserTest()
    // TODO updateUserTest()

    @Test
    public void setUserActiveOrInactiveTest() throws RepositoryException {

        User user = sqlUserRepository.searchUsersBy("", "", "joakimlandstrom").get(0);

        sqlUserRepository.activateUserById(user.getId());

        assertTrue(sqlUserRepository.getUserById(user.getId()).isActive());

        sqlUserRepository.inactivateUserById(user.getId());

        assertFalse(sqlUserRepository.getUserById(user.getId()).isActive());

    }

    @Test
    public void getUserByIdTest() throws RepositoryException {

        int idTest = 1;

        assertEquals(idTest, sqlUserRepository.getUserById(1).getId());

    }

    @Test
    public void getUserByTeamIdTest() throws RepositoryException {

        List<User> teamList1 = sqlUserRepository.getUsersByTeamId(1);
        List<User> teamList2 = sqlUserRepository.getUsersByTeamId(2);

        assertEquals(2, teamList1.size());
        assertEquals(1, teamList2.size());

    }

}
