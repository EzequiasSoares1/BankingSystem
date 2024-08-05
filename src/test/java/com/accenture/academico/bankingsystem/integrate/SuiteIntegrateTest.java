package com.accenture.academico.bankingsystem.integrate;
import com.accenture.academico.bankingsystem.integrate.controller.AddressControllerTest;
import com.accenture.academico.bankingsystem.integrate.controller.AuthenticationControllerTest;
import com.accenture.academico.bankingsystem.integrate.controller.UserControllerTest;
import com.accenture.academico.bankingsystem.integrate.security.CryptServiceTest;
import com.accenture.academico.bankingsystem.integrate.security.TokenServiceTest;
import com.accenture.academico.bankingsystem.integrate.service.AddressServiceTest;
import com.accenture.academico.bankingsystem.integrate.service.AuthenticationServiceTest;
import com.accenture.academico.bankingsystem.integrate.service.UserServiceTest;
import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@Testable
@SelectClasses({
    AuthenticationServiceTest.class,
    UserServiceTest.class,
    CryptServiceTest.class,
    TokenServiceTest.class,
    AddressServiceTest.class,
    UserControllerTest.class,
    AuthenticationControllerTest.class,
    AddressControllerTest.class
})
public class SuiteIntegrateTest {
}
