package za.co.oldmutual.asisa.common.validation;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class HasColumnIdentifier {

  public boolean hasColumn(ResultSetMetaData columnNames, String columnName) throws SQLException {
    int columns = columnNames.getColumnCount();
    for (int x = 1; x <= columns; x++) {
      if (columnName.equals(columnNames.getColumnName(x))) {
        return true;
      }
    }
    return false;
  }
}
