package com.accenture.academico.bankingsystem.unit;

import com.accenture.academico.bankingsystem.unit.config.GlobalExceptionHandlerTest;
import com.accenture.academico.bankingsystem.unit.model.UserTest;
import com.accenture.academico.bankingsystem.unit.security.CodeAccessTest;
import com.accenture.academico.bankingsystem.unit.security.CryptServiceTest;
import com.accenture.academico.bankingsystem.unit.security.TokenServiceTest;
import com.accenture.academico.bankingsystem.unit.service.UserServiceTest;
import com.accenture.academico.bankingsystem.unit.service.converter.UserConverterTest;
import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@Testable
@SelectClasses({
    UserTest.class,
    GlobalExceptionHandlerTest.class,
    UserServiceTest.class,
    UserConverterTest.class,
    CodeAccessTest.class,
    CryptServiceTest.class,
    TokenServiceTest.class
})
public class SuiteUnitTest {
}
