package bananacore.epic.interfaces.observers;

import java.sql.Timestamp;

public interface Graphable {
    Timestamp getDate();

    double getGraphValue();
}
