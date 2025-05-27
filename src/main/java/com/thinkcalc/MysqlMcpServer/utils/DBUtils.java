package com.thinkcalc.MysqlMcpServer.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.*;
import java.util.*;

@Component
public class DBUtils {

    private static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public Boolean insert(String sql) throws SQLException {
        if (StrUtil.isBlank(sql)) {
            return false;
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false); // 关闭自动提交，开启事务
            ps = conn.prepareStatement(sql);

            if (ps == null) {
                throw new SQLException("PreparedStatement 创建失败，SQL: " + sql);
            }

            int rowsAffected = ps.executeUpdate();
            conn.commit();
            return rowsAffected > 0;
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void setColumns(PreparedStatement ps, Map<String, String> columnMap, String columns, JSONObject object) {
        String[] split = columns.split(",");
        try {
            for (int i = 1; i <= split.length; i++) {
                String key = split[i - 1];
                switch (columnMap.get(key)) {
                    case "VARCHAR":
                    case "TEXT":
                    case "CHAR":
                    case "LONGTEXT":
                        ps.setString(i, object.getString(key));
                        break;
                    case "INT":
                        ps.setInt(i, object.getIntValue(key));
                        break;
                    case "TIME":
                    case "DATETIME":
                        Date date = new Date(object.getDate(key).getTime());
                        ps.setDate(i, date);
                        break;
                    case "TIMESTAMP":
                        ps.setTimestamp(i, object.getTimestamp(key));
                        break;
                    case "DOUBLE":
                        ps.setDouble(i, object.getDouble(key));
                        break;
                    case "BIGINT":
                        ps.setLong(i, object.getLong(key));
                        break;
                    case "FLOAT":
                        ps.setFloat(i, object.getFloat(key));
                        break;
                    case "DECIMAL":
                    case "NUMERIC":
                        ps.setBigDecimal(i, object.getBigDecimal(key));
                        break;
                    case "VARBINARY":
                    case "BINARY":
                    case "LONGBLOB":
                    case "BLOB":
                        ps.setBytes(i, object.getBytes(key));
                        break;
                    default:
                }
            }
        } catch (Exception e) {

        }

    }

    /**
     * 验证字段是否与数据库表一致
     *
     * @param columnMap
     * @param columns
     * @return
     */
    private Boolean verifyColumn(Map<String, String> columnMap, String columns) {
        String[] split = columns.split(",");
        for (String str : split) {
            String replace = str.replace(" ", "");
            if (!columnMap.containsKey(replace)) {
                return false;
            }
        }
        return true;
    }

    public Map<String, String> getColumns(String tableName) {
        Connection connection = null;
        PreparedStatement ps = null;
        Statement st = null;
        Map<String, String> columnMetadata = new HashMap<>();
        // 获取数据库元数据验证列名
        try {
            connection = DriverManager.getConnection(url, username, password);
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet columns = meta.getColumns(null, null, tableName, null);

            while (columns.next()) {
                String colName = columns.getString("COLUMN_NAME").toLowerCase();
                String colType = columns.getString("TYPE_NAME");  // 获取数据库原生类型名称‌:ml-citation{ref="6" data="citationList"}
                columnMetadata.put(colName, colType);
            }
        } catch (Exception e) {

        }
        return columnMetadata;
    }

    public void close(Connection connection, PreparedStatement ps, Statement st) {
        //释放资源
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Map<String, Object>> parseResultSet(ResultSet rs) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                try {
                    row.put(columnName, rs.getObject(i));
                } catch (Exception e) {
                    System.err.println("解析列失败: " + columnName + ", 错误: " + e.getMessage());
                }
            }
            resultList.add(row);
        }
        return resultList;
    }

    public String query(String sql) {
        return getString(sql);
    }

    /**
     * 执行SQL查询语句并返回结果
     * 该方法用于执行不需要参数绑定的简单SQL查询
     *
     * @param sql 要执行的SQL查询语句
     * @return 查询结果的字符串表示
     */
    private String getString(String sql) {
        Connection connection = null;
        PreparedStatement ps = null;
        Statement st = null;
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            logger.info("尝试连接数据库: url={}, username={}", url, username);
            connection = DriverManager.getConnection(url, username, password);
            logger.info("数据库连接成功");
            logger.info("执行的 SQL: {}", sql);
            ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            result = parseResultSet(resultSet);
            logger.info("查询结果: {}", result);
        } catch (Exception e) {
            logger.error("查询失败: {}", e.getMessage(), e);
            return "查询失败: " + e.getMessage(); // 返回错误信息
        } finally {
            close(connection, ps, st);
        }
        return String.valueOf(result);
    }
}
