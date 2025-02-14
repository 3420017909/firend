package org.example.friend.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class RoleConverter implements Converter<Integer> {
    private static final String USER_STATUS = "普通用户";
    private static final String ADMIN_STATUS = "管理员";


    @Override
    public Class<?> supportJavaTypeKey() {
        // 实体类中对象属性类型
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        // Excel中对应的CellData属性类型
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty,
                                     GlobalConfiguration globalConfiguration) {
        // 从Cell中读取数据
        String gender = cellData.getStringValue();
        // 判断Excel中的值，将其转换为预期的数值
        if (USER_STATUS.equals(gender)) {
            return 0;
        } else if (ADMIN_STATUS.equals(gender)) {
            return 1;
        }
        return null;
    }

    @Override
    public CellData<?> convertToExcelData(Integer integer, ExcelContentProperty excelContentProperty,
                                          GlobalConfiguration globalConfiguration) {
        // 判断实体类中获取的值，转换为Excel预期的值，并封装为CellData对象
        if (integer == null) {
            return new CellData<>("");
        } else if (integer == 0) {
            return new CellData<>(USER_STATUS);
        } else if (integer == 1) {
            return new CellData<>(ADMIN_STATUS);
        }
        return new CellData<>("");
    }
}
