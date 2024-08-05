package com.accenture.academico.bankingsystem.integrate;
import com.accenture.academico.bankingsystem.integrate.security.CryptServiceTest;
import com.accenture.academico.bankingsystem.integrate.security.TokenServiceTest;
import com.accenture.academico.bankingsystem.integrate.service.UserServiceTest;
import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@Testable
@SelectClasses({
    UserServiceTest.class,
    CryptServiceTest.class,
    TokenServiceTest.class
})
public class SuiteIntegrateTest {
}
