package mx.com.gseguros.mesacontrol.model;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class TvaloperSQL extends TvaloperVO implements SQLData {

    public String getSQLTypeName() throws SQLException {
        return "TVALOPER";
    }

    public void readSQL(SQLInput sqlInput, String string) throws SQLException {
        setCdperson(Integer.valueOf(sqlInput.readInt()));
        setCdrol(sqlInput.readString());
        setOtvalor01(sqlInput.readString());
        setOtvalor02(sqlInput.readString());
        setOtvalor03(sqlInput.readString());
        setOtvalor04(sqlInput.readString());
        setOtvalor05(sqlInput.readString());
    }

    public void writeSQL(SQLOutput sqlOutput) throws SQLException {
        sqlOutput.writeInt(getCdperson());
        sqlOutput.writeString(getCdrol());
        sqlOutput.writeString(getOtvalor01());
        sqlOutput.writeString(getOtvalor02());
        sqlOutput.writeString(getOtvalor03());
        sqlOutput.writeString(getOtvalor04());
        sqlOutput.writeString(getOtvalor05());
    }
    
}