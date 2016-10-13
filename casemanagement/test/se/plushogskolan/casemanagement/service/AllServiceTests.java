package se.plushogskolan.casemanagement.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.plushogskolan.casemanagement.model.AllModelTests;
import se.plushogskolan.casemanagement.model.TestIssue;
import se.plushogskolan.casemanagement.model.TestTeam;
import se.plushogskolan.casemanagement.model.TestUser;
import se.plushogskolan.casemanagement.model.TestWorkItem;
import se.plushogskolan.casemanagement.repository.sql.AllSqlRepositoryTests;
import se.plushogskolan.casemanagement.repository.sql.TestSqlIssueRepository;
import se.plushogskolan.casemanagement.repository.sql.TestSqlTeamRepository;
import se.plushogskolan.casemanagement.service.TestCaseService;

@RunWith(Suite.class)

@Suite.SuiteClasses({ TestCaseService.class, TestCaseServiceExceptionHandling.class })

public class AllServiceTests {
}