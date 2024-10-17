package org.fms.training.common.constant;

import jakarta.annotation.security.RolesAllowed;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RolesAllowed({
        Authorization.FA_MANAGER,
        Authorization.DELIVERABLES_MANAGER,
        Authorization.GROUP_ADMIN,
        Authorization.CONTENT_MANAGER,
        Authorization.TRAINER,
        Authorization.FMS_ADMIN
})
public @interface PermitAll {
}
