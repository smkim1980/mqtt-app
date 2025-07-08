package tmoney.gbi.bms.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 필드에만 적용
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 정보 유지
public @interface DateTimeFormat {
    String value();
}
