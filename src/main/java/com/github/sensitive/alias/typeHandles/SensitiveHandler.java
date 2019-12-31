package com.github.sensitive.alias.typeHandles;

import com.github.sensitive.alias.typeAliases.Sensitive;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

/**
 * 对应的JavaType的字段,必须是保证需要加密解密的
 *
 * 版本号怎么控制？？
 */
@MappedTypes(Sensitive.class)
public class SensitiveHandler extends BaseTypeHandler<String> {

     /**
     * 用于定义在Mybatis设置参数时该如何把Java类型的参数转换为对应的数据库类型
     *
     * @param ps        当前的PreparedStatement对象
     * @param i         当前参数的位置
     * @param parameter 当前参数的Java对象
     * @param jdbcType  当前参数的数据库类型
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        // 不对入参做处理，无法丈量
        //ps.setString(i, "1111");
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {

        String columnValue = rs.getString(columnName);

        columnValue = obtainResult(columnValue);

        return columnValue;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        String columnValue = rs.getString(columnIndex);

        columnValue = obtainResult(columnValue);

        return columnValue;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);

        columnValue = obtainResult(columnValue);

        return columnValue;

    }


    private String obtainResult(String columnValue){

        if (columnValue == null)
            return null;

        try {
            boolean whetherEncrypted = false;
            if (whetherEncrypted){
                // 解密
                columnValue = "解密后";
            }
        } catch (Exception e){
            // Occur Exception:使用原始值
        }
        return columnValue;
    }


}