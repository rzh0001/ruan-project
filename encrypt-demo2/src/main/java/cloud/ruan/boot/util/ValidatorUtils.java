package cloud.ruan.boot.util;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

public class ValidatorUtils {

    private static final Validator validatorFast = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
    private static final Validator validatorAll = Validation.byProvider(HibernateValidator.class).configure().failFast(false).buildValidatorFactory().getValidator();

    /**
     * 快速验证
     * 校验遇到第一个不合法的字段直接返回不合法字段，后续字段不再校验
     *
     * @param domain 域
     * @return {@link Set}<{@link ConstraintViolation}<{@link T}>>
     * @throws Exception 异常
     */
    public static <T> Set<ConstraintViolation<T>> validateFast(T domain) throws Exception {
        Set<ConstraintViolation<T>> validateResult = validatorFast.validate(domain);
        if (validateResult.size() > 0) {
            System.out.println(validateResult.iterator().next().getPropertyPath() + "：" + validateResult.iterator().next().getMessage());
        }
        return validateResult;
    }

    /**
     * 验证所有
     * 校验所有字段并返回不合法字段
     *
     * @param domain 域
     * @return {@link Set}<{@link ConstraintViolation}<{@link T}>>
     * @throws Exception 异常
     */
    public static <T> Set<ConstraintViolation<T>> validateAll(T domain) throws Exception {
        Set<ConstraintViolation<T>> validateResult = validatorAll.validate(domain);
        if (validateResult.size() > 0) {
            Iterator<ConstraintViolation<T>> it = validateResult.iterator();
            while (it.hasNext()) {
                ConstraintViolation<T> cv = it.next();
                System.out.println(cv.getPropertyPath() + "：" + cv.getMessage());
            }
        }
        return validateResult;
    }

}
