package com.jzarob.docmm4j;


import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.Assert;

@RunWith(MockitoJUnitRunner.class)
public class PojoTest {


    @Test
    public void testExceptionPojos() {
        Validator validator = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new GetterTester())
                .with(new SetterTester())
                .build();

        validator.validate("com.jzarob.docmm4j.exceptions", new FilterPackageInfo());

        Assert.isTrue(true, "verify that we get here.");
    }

    @Test
    public void testModelPojos() {
        Validator validator = ValidatorBuilder.create()
                .with(new GetterTester())
                .with(new SetterTester())
                .build();

        validator.validate("com.jzarob.docmm4j.models", new FilterPackageInfo());
        Assert.isTrue(true, "verify that we get here.");
    }
}
