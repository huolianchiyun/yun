package com.hlcy.yun.sys.modules.common.enums.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyEnumTypeHandler<E extends Enum & BaseEnumValue> extends BaseTypeHandler<E> {

    private BaseTypeHandler<E> typeHandler;

    public MyEnumTypeHandler(Class<E> type) {
        if (null == type) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        if (BaseEnumValue.class.isAssignableFrom(type)) {
            typeHandler = new EnumValueTypeHandler(type);
        } else {
            typeHandler = new org.apache.ibatis.type.EnumTypeHandler<>(type);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        typeHandler.setNonNullParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return (E) typeHandler.getNullableResult(rs, columnName);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return (E) typeHandler.getNullableResult(rs, columnIndex);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (E) typeHandler.getNullableResult(cs, columnIndex);
    }


    static class EnumValueTypeHandler<E extends Enum<E> & BaseEnumValue> extends BaseTypeHandler<BaseEnumValue> {
        private final Class<E> type;

        EnumValueTypeHandler(Class<E> type) {
            if (type == null) {
                throw new IllegalArgumentException("Type argument cannot be null");
            }
            this.type = type;
        }

        // 设置参数时, 把java类型参数转换为数据库对应类型
        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, BaseEnumValue parameter, JdbcType jdbcType) throws SQLException {
            Assert.notNull(jdbcType,
                    String.format("The Entity field <%s> 没有指定 JdbcType 类型，请在相应的 mapper.xml 中的 sql 语句中为该枚举类型指定数据库列类型，如：#{xxx,jdbcType=VARCHAR}。", type));

            switch (jdbcType) {
                case VARCHAR:
                    ps.setString(i, (String) parameter.getValue());
                    break;
                case INTEGER:
                    ps.setInt(i, (Integer) parameter.getValue());
                    break;
                case BIGINT:
                    ps.setLong(i, (Long) parameter.getValue());
                    break;
                default:
            }
        }

        // 通过字段名获取字段值，并将数据库字段类型转换为java类型
        @Override
        public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
            final Object value = rs.getObject(columnName);
            return rs.wasNull() ? null : valueOf(value);
        }

        // 通过字段索引获取字段值，并将数据库字段类型转换为java类型
        @Override
        public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            final Object value = rs.getObject(columnIndex);
            return rs.wasNull() ? null : valueOf(value);
        }

        // 调用存储过程后，将数据库字段类型转换为java类型
        @Override
        public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            final Object value = cs.getObject(columnIndex);
            return cs.wasNull() ? null : valueOf(value);
        }

        private E valueOf(Object value) {
            try {
                return EnumValueUtil.valueOf(type, value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not convert " + value + " to " + type.getSimpleName() + "by enum value.", e);
            }
        }

        final static class EnumValueUtil {
            static <E extends Enum<E> & BaseEnumValue> E valueOf(Class<E> enumClass, Object code) {
                E[] enumConstants = enumClass.getEnumConstants();
                for (E e : enumConstants) {
                    if (e.getValue().equals(code)) {
                        return e;
                    }
                }
                return null;
            }
        }
    }
}
