package se.plushogskolan.casemanagement.model;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public final class TestTeam {
    private final int defaultId = 1;
    private final String defaultName = "Regular ordinary team";
    private final String anotherName = "We are unique!";
    private Team teamTwin1;
    private Team teamTwin2;
    private Team anotherTeam;

    @Before
    public void setup() {
        teamTwin1 = Team.builder().setId(defaultId).build(defaultName);
        teamTwin2 = Team.builder().setId(defaultId).build(defaultName);
    }

    @Test
    public void testTeamEqual() {

        assertEquals(teamTwin1, teamTwin2);

        anotherTeam = Team.builder().build(defaultName);
        assertThat(teamTwin1, not(anotherTeam));

        anotherTeam = Team.builder().build(anotherName);
        assertThat(teamTwin1, not(anotherTeam));
    }

    @Test
    public void testTeamHashCode() {
        assertEquals(teamTwin1.hashCode(), teamTwin2.hashCode());

        anotherTeam = Team.builder().build(defaultName);
        assertThat(teamTwin1.hashCode(), not(anotherTeam.hashCode()));

        anotherTeam = Team.builder().build(anotherName);
        assertThat(teamTwin1.hashCode(), not(anotherTeam.hashCode()));
    }

    @Test
    public void testTeamBuilder() {
        List<User> users = new ArrayList<>();
        users.add(User.builder().setId(1001).build("long username"));
        Team.builder().setActive(false).build(defaultName);
    }
}
