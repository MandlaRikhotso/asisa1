package za.co.oldmutual.asisa.common.validation;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class CustomBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {

    public CustomBeanPropertyRowMapper(Class<T> mappedClass) {
        super(mappedClass);
    }
}
