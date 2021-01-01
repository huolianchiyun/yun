package com.hlcy.yun.common.mybatis.audit.plugin;

import com.hlcy.yun.common.mybatis.audit.annotation.CreatedBy;
import com.hlcy.yun.common.mybatis.audit.annotation.CreatedDate;
import com.hlcy.yun.common.mybatis.audit.annotation.LastModifiedBy;
import com.hlcy.yun.common.mybatis.audit.annotation.LastModifiedDate;
import com.hlcy.yun.common.spring.security.SecurityUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * 审计拦截
 * insert or update 时对 sql执行进行拦截，填充 creator、create time、updater、update time
 */
@Data
@Accessors(chain = true)
// @intercepts声明该类为拦截器，@signature声明拦截对象。type为所要拦截的接口类， method为需要拦截的方法，args为 update方法的参数。
@Intercepts({@Signature(type = org.apache.ibatis.executor.Executor.class,
        method = "update", args = {MappedStatement.class, Object.class})})
public class AuditInterceptor implements Interceptor {

    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // 获取 SQL 命令
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Audit_Handle:
        {
            if (isSkip(sqlCommandType)) {
                break Audit_Handle;
            }
            // 获取将要持久化的实体对象
            Object entity = invocation.getArgs()[1];
            if (!processCollectionEntity(sqlCommandType, entity)) {
                Field[] declaredFields = getFieldsWithSuperFields(entity.getClass());
                // 是否为mybatis plug
                boolean isPlugUpdate = declaredFields.length == 1 && declaredFields[0].getName().equals("serialVersionUID");
                //兼容mybatis plus的update
                if (isPlugUpdate) {
                    Map updateParam = (Map) entity;
                    declaredFields = getFieldsWithSuperFields(updateParam.get("param1").getClass());
                }
                auditEntity(sqlCommandType, entity, declaredFields, isPlugUpdate);
            }
        }
        return invocation.proceed();
    }

    private boolean processCollectionEntity(SqlCommandType sqlCommandType, Object entity) throws IllegalAccessException {
        if (entity instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) entity;
            final Object[] entities = ((Collection) paramMap.get("collection")).toArray();
            final Field[] declaredFields = getFieldsWithSuperFields(entities[0].getClass());
            for (Object o : entities) {
                auditEntity(sqlCommandType, o, declaredFields, false);
            }
            return true;
        }
        return false;
    }

    private void auditEntity(SqlCommandType sqlCommandType, Object entity, Field[] declaredFields,
                             boolean isPlugUpdate) throws IllegalAccessException {
        LocalDateTime now = LocalDateTime.now();
        for (Field field : declaredFields) {
            fillTimeOnInsertOrUpdate(sqlCommandType, entity, isPlugUpdate, field, now);
            fillOperatorOnInsertOrUpdate(sqlCommandType, entity, isPlugUpdate, field, SecurityUtils.getCurrentUsername());
        }
    }

    private Field[] getFieldsWithSuperFields(Class<?> entityClass) {
        Field[] declaredFields = entityClass.getDeclaredFields();
        if (entityClass.getSuperclass() != null) {
            Field[] superField = entityClass.getSuperclass().getDeclaredFields();
            declaredFields = ArrayUtils.addAll(declaredFields, superField);
        }
        return declaredFields;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof org.apache.ibatis.executor.Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * 插入或更新时，填充创建时间或更新时间
     *
     * @param sqlCommandType sql 类型
     * @param entity         将要持久化的实体对象
     * @param isPlugUpdate   兼容mybatis plus的 update
     * @param now            将要填充的时间
     * @param field          实体对象属性对应得 Field
     * @throws IllegalAccessException
     */
    private void fillTimeOnInsertOrUpdate(SqlCommandType sqlCommandType, Object entity, boolean isPlugUpdate,
                                          Field field, LocalDateTime now) throws IllegalAccessException {
        // insert
        if (field.getAnnotation(CreatedDate.class) != null) {
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                field.setAccessible(true);
                if (field.get(entity) == null) {
                    field.set(entity, now);
                }
                field.setAccessible(false);
            }
        }

        // update
        if (field.getAnnotation(LastModifiedDate.class) != null) {
            if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                field.setAccessible(true);
                if (field.get(entity) == null) {
                    //兼容mybatis plus的update
                    if (isPlugUpdate) {
                        Map updateParam = (Map) entity;
                        field.set(updateParam.get("param1"), now);
                    } else {
                        field.set(entity, now);
                    }
                }
                field.setAccessible(false);
            }
        }
    }

    /**
     * 插入或更新时，填充创建人或更新人
     *
     * @param sqlCommandType sql 类型
     * @param entity         将要持久化的实体对象
     * @param isPlugUpdate   兼容mybatis plus的 update
     * @param operator       将要填充的操作人
     * @param field          实体对象属性对应得 Field
     * @throws IllegalAccessException
     */
    private void fillOperatorOnInsertOrUpdate(SqlCommandType sqlCommandType, Object entity, boolean isPlugUpdate,
                                              Field field, String operator) throws IllegalAccessException {
        // insert
        if (field.getAnnotation(CreatedBy.class) != null && SqlCommandType.INSERT.equals(sqlCommandType)) {
            field.setAccessible(true);
            field.set(entity, operator);
        }
        // update
        if (field.getAnnotation(LastModifiedBy.class) != null && SqlCommandType.UPDATE.equals(sqlCommandType)) {
            field.setAccessible(true);
            //兼容mybatis plus的 update
            if (isPlugUpdate) {
                Map updateParam = (Map) entity;
                field.set(updateParam.get("param1"), operator);
            } else {
                field.set(entity, operator);
            }
        }
    }

    private boolean isSkip(SqlCommandType sqlCommandType) {
        return !(SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType));
    }
}
