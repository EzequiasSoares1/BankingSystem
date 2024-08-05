package com.accenture.academico.bankingsystem;
import com.accenture.academico.bankingsystem.integrate.SuiteIntegrateTest;
import com.accenture.academico.bankingsystem.unit.SuiteUnitTest;
import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Suite
@Testable
@SelectClasses({
        SuiteIntegrateTest.class,
        SuiteUnitTest.class
})
class BankingSystemApplicationTests {

}
