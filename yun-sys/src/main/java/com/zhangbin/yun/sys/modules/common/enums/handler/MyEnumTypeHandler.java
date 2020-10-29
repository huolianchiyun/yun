package com.zhangbin.yun.sys.modules.common.enums.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyEnumTypeHandler<E extends Enum<E> & BaseEnumValue<Integer>> extends BaseTypeHandler<E> {

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


    static class EnumValueTypeHandler<E extends Enum<E> & BaseEnumValue<Integer>> extends BaseTypeHandler<BaseEnumValue<Integer>> {
        private final Class<E> type;

        public EnumValueTypeHandler(Class<E> type) {
            if (type == null) {
                throw new IllegalArgumentException("Type argument cannot be null");
            }
            this.type = type;
        }

        // 设置参数时, 把java类型参数转换为数据库对应类型
        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, BaseEnumValue<Integer> parameter, JdbcType jdbcType) throws SQLException {
            ps.setInt(i, parameter.getValue());
        }

        // 通过字段名获取字段值，并将数据库字段类型转换为java类型
        @Override
        public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
            int value = rs.getInt(columnName);
            return rs.wasNull() ? null : valueOf(value);
        }

        // 通过字段索引获取字段值，并将数据库字段类型转换为java类型
        @Override
        public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            int value = rs.getInt(columnIndex);
            return rs.wasNull() ? null : valueOf(value);
        }

        // 调用存储过程后，将数据库字段类型转换为java类型
        @Override
        public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            int value = cs.getInt(columnIndex);
            return cs.wasNull() ? null : valueOf(value);
        }

        private E valueOf(Integer value) {
            try {
                return EnumValueUtil.valueOf(type, value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not convert " + value + " to " + type.getSimpleName() + "by enum value.", e);
            }
        }

        final static class EnumValueUtil {
            static <E extends Enum<E> & BaseEnumValue<Integer>> E valueOf(Class<E> enumClass, Integer code) {
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
